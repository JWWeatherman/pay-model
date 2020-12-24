package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

object UnspentTransaction {
  implicit val formatUnspentTx = Json.format[UnspentTransaction]
}
case class UnspentTransaction(
    txid: TxId,
    vout: Int,
    address: BtcAddress,
    label: String,
    scriptPubKey: String,
    amount: Btc,
    confirmations: Int,
    spendable: Boolean,
    solvable: Boolean,
    safe: Boolean
)
