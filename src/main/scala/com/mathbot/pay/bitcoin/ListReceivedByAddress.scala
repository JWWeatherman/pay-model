package com.mathbot.pay.bitcoin

import fr.acinq.bitcoin.Btc
import play.api.libs.json.{Json, OFormat}

object ListReceivedByAddress {
  implicit val formatListReceivedByAddress: OFormat[ListReceivedByAddress] = Json.format[ListReceivedByAddress]
}
case class ListReceivedByAddress(
    involvesWatchonly: Option[Boolean],
    address: BtcAddress,
    amount: Btc,
    confirmations: Int,
    label: String,
    txids: List[TxId]
)
