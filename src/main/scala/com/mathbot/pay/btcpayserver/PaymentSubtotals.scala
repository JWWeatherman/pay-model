package com.mathbot.pay.btcpayserver

import com.mathbot.pay.bitcoin._
import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.json._

case class PaymentSubtotals(
    BTC: Option[Satoshi],
    BTC_LightningLike: Option[Satoshi]
)

object PaymentSubtotals extends PlayJsonSupport {
  implicit val formatPaymentSubtotals: OFormat[PaymentSubtotals] = Json.format[PaymentSubtotals]
}
