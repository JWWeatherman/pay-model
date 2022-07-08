package com.mathbot.pay.bitcoin

import java.time.Instant
import com.mathbot.pay.bitcoin.TransactionCategory.TransactionCategory
import com.mathbot.pay.json.{EpochSecondInstantFormatter, PlayJsonSupport}
import fr.acinq.bitcoin.Btc
import play.api.libs.json.{Json, OFormat}

/**
 * result from gettransaction eg. bitcoin-cli gettransaction "1075db55d416d3ca199f55b6084e2115b9345e16c5cf302fc80e9d5fbf5d48d"
 * {
 * "amount" : x.xxx,        (numeric) The transaction amount in BTC
 * "fee": x.xxx,            (numeric) The amount of the fee in BTC. This is negative and only available for the
 * 'send' category of transactions.
 * "confirmations" : n,     (numeric) The number of confirmations
 * "blockhash" : "hash",  (string) The block hash
 * "blockindex" : xx,       (numeric) The index of the transaction in the block that includes it
 * "blocktime" : ttt,       (numeric) The time in seconds since epoch (1 Jan 1970 GMT)
 * "txid" : "transactionid",   (string) The transaction id.
 * "time" : ttt,            (numeric) The transaction time in seconds since epoch (1 Jan 1970 GMT)
 * "timereceived" : ttt,    (numeric) The time received in seconds since epoch (1 Jan 1970 GMT)
 * "bip125-replaceable": "yes|no|unknown",  (string) Whether this transaction could be replaced due to BIP125 (replace-by-fee);
 * may be unknown for unconfirmed transactions not in the mempool
 * "details" : [
 * {
 * "account" : "accountname",      (string) DEPRECATED. This field will be removed in a V0.18. To see this deprecated field, start bitcoind with -deprecatedrpc=accounts. The account name involved in the transaction, can be "" for the default account.
 * "address" : "address",          (string) The bitcoin address involved in the transaction
 * "category" : "send|receive",    (string) The category, either 'send' or 'receive'
 * "amount" : x.xxx,                 (numeric) The amount in BTC
 * "label" : "label",              (string) A comment for the address/transaction, if any
 * "vout" : n,                       (numeric) the vout value
 * "fee": x.xxx,                     (numeric) The amount of the fee in BTC. This is negative and only available for the
 * 'send' category of transactions.
 * "abandoned": xxx                  (bool) 'true' if the transaction has been abandoned (inputs are respendable). Only available for the
 * 'send' category of transactions.
 * }
 * ,...
 * ],
 * "hex" : "data"         (string) Raw data for transaction
 * }
 *
 * @param amount
 * @param confirmations
 * @param txid
 * @param time
 * @param timereceived
 * @param details
 * @param hex
 */
case class WalletTransaction(
    amount: Btc,
    confirmations: Int,
    txid: TxId,
    time: Instant,
    timereceived: Instant,
    details: Option[Seq[Detail]],
    hex: Option[String]
)

object WalletTransaction extends PlayJsonSupport with EpochSecondInstantFormatter {
  implicit val formatWalletTransactionPlay: OFormat[WalletTransaction] = Json.format[WalletTransaction]
}
