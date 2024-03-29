/*
 * Copyright 2019 ACINQ SAS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.acinq.eclair.blockchain.electrum

import java.util.concurrent.atomic.AtomicLong

import akka.actor.{Actor, ActorLogging, ActorRef, Stash, Terminated}
import fr.acinq.bitcoin.{BlockHeader, ByteVector32, Transaction}
import fr.acinq.eclair.blockchain._
import fr.acinq.eclair.blockchain.electrum.ElectrumClient.computeScriptHash
import fr.acinq.eclair.channel.{BITCOIN_FUNDING_DEPTHOK, BITCOIN_PARENT_TX_CONFIRMED}
import fr.acinq.eclair.transactions.Scripts

import scala.collection.immutable.{Queue, SortedMap}

class ElectrumWatcher(blockCount: AtomicLong, client: ActorRef) extends Actor with Stash with ActorLogging {

  client ! ElectrumClient.AddStatusListener(self)

  def receive: Receive =
    disconnected(Set.empty, Queue.empty, SortedMap.empty, Queue.empty)

  def disconnected(
      watches: Set[Watch],
      publishQueue: Queue[PublishAsap],
      block2tx: SortedMap[Long, Seq[Transaction]],
      getTxQueue: Queue[(GetTxWithMeta, ActorRef)]
  ): Receive = {
    case ElectrumClient.ElectrumReady(_, _, _) =>
      client ! ElectrumClient.HeaderSubscription(self)
    case ElectrumClient.HeaderSubscriptionResponse(height, header) =>
      watches.foreach(self ! _)
      publishQueue.foreach(self ! _)
      getTxQueue.foreach { case (msg, origin) => self.tell(msg, origin) }
      context become running(
        height,
        header,
        Set(),
        Map(),
        block2tx,
        Queue.empty
      )
    case watch: Watch =>
      context become disconnected(
        watches + watch,
        publishQueue,
        block2tx,
        getTxQueue
      )
    case publish: PublishAsap =>
      context become disconnected(
        watches,
        publishQueue :+ publish,
        block2tx,
        getTxQueue
      )
    case getTx: GetTxWithMeta =>
      context become disconnected(
        watches,
        publishQueue,
        block2tx,
        getTxQueue :+ (getTx, sender)
      )
  }

  def running(
      height: Int,
      tip: BlockHeader,
      watches: Set[Watch],
      scriptHashStatus: Map[ByteVector32, String],
      block2tx: SortedMap[Long, Seq[Transaction]],
      sent: Queue[Transaction]
  ): Receive = {
    case ElectrumClient.HeaderSubscriptionResponse(_, newtip) if tip == newtip =>
      ()

    case ElectrumClient.HeaderSubscriptionResponse(newheight, newtip) =>
      watches collect {
        case watch: WatchConfirmed =>
          val scriptHash = computeScriptHash(watch.publicKeyScript)
          client ! ElectrumClient.GetScriptHashHistory(scriptHash)
      }
      val toPublish = block2tx.filterKeys(_ <= newheight)
      toPublish.values.flatten.foreach(publish)
      context become running(
        newheight,
        newtip,
        watches,
        scriptHashStatus,
        block2tx -- toPublish.keys,
        sent ++ toPublish.values.flatten
      )

    case watch: Watch if watches.contains(watch) => ()

    case watch @ WatchSpent(_, txid, outputIndex, publicKeyScript, _, _) =>
      val scriptHash = computeScriptHash(publicKeyScript)
      log.info(
        s"added watch-spent on output=$txid:$outputIndex scriptHash=$scriptHash"
      )
      client ! ElectrumClient.ScriptHashSubscription(scriptHash, self)
      context.watch(watch.replyTo)
      context become running(
        height,
        tip,
        watches + watch,
        scriptHashStatus,
        block2tx,
        sent
      )

    case watch @ WatchConfirmed(_, txid, publicKeyScript, _, _) =>
      val scriptHash = computeScriptHash(publicKeyScript)
      log.info(s"added watch-confirmed on txid=$txid scriptHash=$scriptHash")
      client ! ElectrumClient.ScriptHashSubscription(scriptHash, self)
      context.watch(watch.replyTo)
      context become running(
        height,
        tip,
        watches + watch,
        scriptHashStatus,
        block2tx,
        sent
      )

    case Terminated(actor) =>
      val watches1 = watches.filterNot(_.replyTo == actor)
      context become running(
        height,
        tip,
        watches1,
        scriptHashStatus,
        block2tx,
        sent
      )

    case ElectrumClient.ScriptHashSubscriptionResponse(scriptHash, status) =>
      scriptHashStatus.get(scriptHash) match {
        case Some(s) if s == status =>
          log.debug(s"already have status=$status for scriptHash=$scriptHash")
        case _ if status.isEmpty =>
          log.info(s"empty status for scriptHash=$scriptHash")
        case _ =>
          log.info(s"new status=$status for scriptHash=$scriptHash")
          client ! ElectrumClient.GetScriptHashHistory(scriptHash)
      }
      context become running(
        height,
        tip,
        watches,
        scriptHashStatus + (scriptHash -> status),
        block2tx,
        sent
      )

    case ElectrumClient.GetScriptHashHistoryResponse(_, history) =>
      // we retrieve the transaction before checking watches
      // NB: height=-1 means that the tx is unconfirmed and at least one of its inputs is also unconfirmed. we need to take them into consideration if we want to handle unconfirmed txes (which is the case for turbo channels)
      history.filter(_.height >= -1).foreach { item => client ! ElectrumClient.GetTransaction(item.txHash, Some(item)) }

    case ElectrumClient.GetTransactionResponse(
        tx,
        Some(item: ElectrumClient.TransactionHistoryItem)
        ) =>
      // this is for WatchSpent/WatchSpentBasic
      val watchSpentTriggered = tx.txIn
        .map(_.outPoint)
        .flatMap(outPoint =>
          watches.collect {
            case WatchSpent(channel, txid, pos, _, event, _) if txid == outPoint.txid && pos == outPoint.index.toInt =>
              // NB: WatchSpent are permanent because we need to detect multiple spending of the funding tx
              // They are never cleaned up but it is not a big deal for now (1 channel == 1 watch)
              log.info(s"output $txid:$pos spent by transaction ${tx.txid}")
              channel ! WatchEventSpent(event, tx)
              None
          }
        )
        .flatten

      // this is for WatchConfirmed
      val watchConfirmedTriggered = watches.collect {
        case w @ WatchConfirmed(
              channel,
              txid,
              _,
              minDepth,
              BITCOIN_FUNDING_DEPTHOK
            ) if txid == tx.txid && minDepth == 0 =>
          // special case for mempool watches (min depth = 0)
          val (dummyHeight, dummyTxIndex) =
            ElectrumWatcher.makeDummyShortChannelId(txid)
          channel ! WatchEventConfirmed(
            BITCOIN_FUNDING_DEPTHOK,
            TxConfirmedAt(dummyHeight, tx),
            dummyTxIndex
          )
          Some(w)
        case WatchConfirmed(_, txid, _, minDepth, _) if txid == tx.txid && minDepth > 0 && item.height > 0 =>
          // min depth > 0 here
          val txheight = item.height
          val confirmations = height - txheight + 1
          log.info(
            s"txid=$txid was confirmed at height=$txheight and now has confirmations=$confirmations (currentHeight=$height)"
          )
          if (confirmations >= minDepth) {
            // we need to get the tx position in the block
            client ! ElectrumClient.GetMerkle(txid, txheight, Some(tx))
          }
          None
      }.flatten

      context become running(
        height,
        tip,
        watches -- watchSpentTriggered -- watchConfirmedTriggered,
        scriptHashStatus,
        block2tx,
        sent
      )

    case ElectrumClient.GetMerkleResponse(
        tx_hash,
        _,
        txheight,
        pos,
        Some(tx: Transaction)
        ) =>
      val confirmations = height - txheight + 1
      val triggered = watches.collect {
        case w @ WatchConfirmed(channel, txid, _, minDepth, event) if txid == tx_hash && confirmations >= minDepth =>
          log.info(
            s"txid=$txid had confirmations=$confirmations in block=$txheight pos=$pos"
          )
          channel ! WatchEventConfirmed(
            event,
            TxConfirmedAt(txheight.toInt, tx),
            pos
          )
          w
      }
      context become running(
        height,
        tip,
        watches -- triggered,
        scriptHashStatus,
        block2tx,
        sent
      )

    case GetTxWithMeta(txid) =>
      client ! ElectrumClient.GetTransaction(txid, Some(sender))

    case ElectrumClient.GetTransactionResponse(tx, Some(origin: ActorRef)) =>
      origin ! GetTxWithMetaResponse(tx.txid, Some(tx), tip.time)

    case ElectrumClient.ServerError(
        ElectrumClient.GetTransaction(txid, Some(origin: ActorRef)),
        _
        ) =>
      origin ! GetTxWithMetaResponse(txid, None, tip.time)

    case PublishAsap(tx) =>
      val blockCount = this.blockCount.get()
      val cltvTimeout = Scripts.cltvTimeout(tx)
      val csvTimeouts = Scripts.csvTimeouts(tx)
      if (csvTimeouts.nonEmpty) {
        // watcher supports txs with multiple csv-delayed inputs: we watch all delayed parents and try to publish every
        // time a parent's relative delays are satisfied, so we will eventually succeed.
        csvTimeouts.foreach {
          case (parentTxId, csvTimeout) =>
            log.info(
              s"txid=${tx.txid} has a relative timeout of $csvTimeout blocks, watching parentTxId=$parentTxId tx={}",
              tx
            )
            val parentPublicKeyScript = WatchConfirmed.extractPublicKeyScript(
              tx.txIn.find(_.outPoint.txid == parentTxId).get.witness
            )
            self ! WatchConfirmed(
              self,
              parentTxId,
              parentPublicKeyScript,
              minDepth = csvTimeout,
              BITCOIN_PARENT_TX_CONFIRMED(tx)
            )
        }
      } else if (cltvTimeout > blockCount) {
        log.info(
          s"delaying publication of txid=${tx.txid} until block=$cltvTimeout (curblock=$blockCount)"
        )
        val block2tx1 = block2tx.updated(
          cltvTimeout,
          block2tx.getOrElse(cltvTimeout, Seq.empty[Transaction]) :+ tx
        )
        context become running(
          height,
          tip,
          watches,
          scriptHashStatus,
          block2tx1,
          sent
        )
      } else {
        publish(tx)
        context become running(
          height,
          tip,
          watches,
          scriptHashStatus,
          block2tx,
          sent :+ tx
        )
      }

    case WatchEventConfirmed(BITCOIN_PARENT_TX_CONFIRMED(tx), _, _) =>
      log.info(s"parent tx of txid=${tx.txid} has been confirmed")
      val blockCount = this.blockCount.get()
      val cltvTimeout = Scripts.cltvTimeout(tx)
      if (cltvTimeout > blockCount) {
        log.info(
          s"delaying publication of txid=${tx.txid} until block=$cltvTimeout (curblock=$blockCount)"
        )
        val block2tx1 = block2tx.updated(
          cltvTimeout,
          block2tx.getOrElse(cltvTimeout, Seq.empty) :+ tx
        )
        context become running(
          height,
          tip,
          watches,
          scriptHashStatus,
          block2tx1,
          sent
        )
      } else {
        publish(tx)
        context become running(
          height,
          tip,
          watches,
          scriptHashStatus,
          block2tx,
          sent :+ tx
        )
      }

    case ElectrumClient.BroadcastTransactionResponse(tx, error_opt) =>
      error_opt match {
        case None =>
          log.info(s"broadcast succeeded for txid=${tx.txid} tx={}", tx)
        case Some(error) if error.message.contains("transaction already in block chain") =>
          log.info(
            s"broadcast ignored for txid=${tx.txid} tx={} (tx was already in blockchain)",
            tx
          )
        case Some(error) =>
          log.error(
            s"broadcast failed for txid=${tx.txid} tx=$tx with error=$error"
          )
      }
      context become running(
        height,
        tip,
        watches,
        scriptHashStatus,
        block2tx,
        sent diff Seq(tx)
      )

    case ElectrumClient.ElectrumDisconnected =>
      // we remember watches and keep track of tx that have not yet been published
      // we also re-send the txes that we previously sent but hadn't yet received the confirmation
      context become disconnected(
        watches,
        sent.map(PublishAsap),
        block2tx,
        Queue.empty
      )
  }

  def publish(tx: Transaction): Unit = {
    client ! ElectrumClient.BroadcastTransaction(tx)
  }
}

object ElectrumWatcher {
  // A (blockHeight, txIndex) tuple that is extracted from the input source
  def makeDummyShortChannelId(txid: ByteVector32): (Int, Int) = {
    val txIndex = txid.bits.sliceToInt(0, 16, signed = false)
    (0, txIndex)
  }
}
