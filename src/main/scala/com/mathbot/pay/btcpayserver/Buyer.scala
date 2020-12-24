package com.mathbot.pay.btcpayserver

import play.api.libs.json.{Json, OFormat}

case class Buyer(
    email: String,
    name: String,
    address1: Option[String] = None,
    address2: Option[String] = None,
    locality: Option[String] = None,
    city: Option[String] = None,
    state: Option[String] = None,
    region: Option[String] = None,
    zip: Option[String] = None,
    country: Option[String] = None,
    phone: Option[String] = None,
    sessionId: Option[String] = None
)

object Buyer {
  implicit val formatBuyer: OFormat[Buyer] = Json.format[Buyer]
}
