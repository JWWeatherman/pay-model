package com.mathbot.pay.models

import com.mathbot.pay.bitcoin.{Btc, BtcAddress, TxId}
import com.mathbot.pay.json.PlayJsonSupport
import com.mathbot.pay.lightning.Bolt11
import com.mathbot.pay.lightning.PayStatus.PayStatus
import play.api.libs.json.{Json, OFormat}

case class DebitWebhookResponse(
    status: PayStatus,
    bolt11: Option[Bolt11] = None,
    btcAddress: Option[BtcAddress] = None,
    txid: Option[TxId] = None,
    btcAmount: Option[Btc] = None,
    error: Option[String] = None
)

object DebitWebhookResponse extends PlayJsonSupport {
  lazy implicit val formatDebitWebhookResponse: OFormat[DebitWebhookResponse] = Json.format[DebitWebhookResponse]

}
