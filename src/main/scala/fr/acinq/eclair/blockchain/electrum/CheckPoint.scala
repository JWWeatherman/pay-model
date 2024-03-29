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

import java.io.InputStream

import fr.acinq.bitcoin.{encodeCompact, Block, ByteVector32}
import fr.acinq.eclair.blockchain.electrum.db.HeaderDb
import org.json4s.JsonAST.{JArray, JInt, JString}
import org.json4s.native.JsonMethods

case class CheckPoint(hash: ByteVector32, nextBits: Long)

object CheckPoint {
  import Blockchain.RETARGETING_PERIOD

  var loadFromChainHash: ByteVector32 => Vector[CheckPoint] = {
    case Block.LivenetGenesisBlock.hash =>
      load(
        classOf[CheckPoint].getResourceAsStream(
          "/electrum/checkpoints_mainnet.json"
        )
      )
    case Block.TestnetGenesisBlock.hash =>
      load(
        classOf[CheckPoint].getResourceAsStream(
          "/electrum/checkpoints_testnet.json"
        )
      )
    case Block.RegtestGenesisBlock.hash => Vector.empty[CheckPoint]
    case _ => throw new RuntimeException
  }

  def load(stream: InputStream): Vector[CheckPoint] = {
    val JArray(values) = JsonMethods.parse(stream)
    val checkpoints = values.collect {
      case JArray(JString(a) :: JInt(b) :: Nil) =>
        CheckPoint(
          ByteVector32.fromValidHex(a).reverse,
          encodeCompact(b.bigInteger)
        )
    }
    checkpoints.toVector
  }

  def load(chainHash: ByteVector32, headerDb: HeaderDb): Vector[CheckPoint] = {
    val checkpoints = CheckPoint.loadFromChainHash(chainHash)
    val checkpoints1 = headerDb.getTip match {
      case Some((height, _)) =>
        val newcheckpoints = for {
          h <- checkpoints.size * RETARGETING_PERIOD - 1 + RETARGETING_PERIOD to height - RETARGETING_PERIOD by RETARGETING_PERIOD
        } yield {
          // we * should * have these headers in our db
          val cpheader = headerDb.getHeader(h).get
          val nextDiff = headerDb.getHeader(h + 1).get.bits
          CheckPoint(cpheader.hash, nextDiff)
        }
        checkpoints ++ newcheckpoints
      case None => checkpoints
    }
    checkpoints1
  }
}
