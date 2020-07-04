package com.mathbot.pay.btcpayserver

import play.api.libs.json.{Json, OFormat}

case class ExRates(
    USD: Double
)

object ExRates {
  implicit val formatExRates: OFormat[ExRates] = Json.format[ExRates]
}
