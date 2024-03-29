package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class Address(`type`: String, address: Option[String], port: Int)

object Address {
  lazy implicit val formatAddress: OFormat[Address] = Json.format[Address]
}
