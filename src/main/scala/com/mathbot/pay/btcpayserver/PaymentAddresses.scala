package com.mathbot.pay.btcpayserver

import com.mathbot.pay.bitcoin.BtcAddress
import com.mathbot.pay.json.PlayJsonSupport
import com.mathbot.pay.lightning.Bolt11
import play.api.libs.json.Json

case class PaymentAddresses(BTC: Option[BtcAddress], BTC_LightningLike: Option[Bolt11])

object PaymentAddresses extends PlayJsonSupport {
  implicit val formatPaymentAddresses = Json.format[PaymentAddresses]
}
