package com.mathbot.pay.btcpayserver

import play.api.libs.json.{Json, OFormat}

case class PaymentUrls(
    BIP21: Option[String] = None,
    BIP72: Option[String] = None,
    BIP72b: Option[String] = None,
    BIP73: Option[String] = None,
    BOLT11: Option[String] = None
)

object PaymentUrls {

  implicit val formatPaymentUrls: OFormat[PaymentUrls] = Json.format[PaymentUrls]
}
