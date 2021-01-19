package com.mathbot.pay.bitcoin

import com.mathbot.pay.bitcoin.AddressType.AddressType
import play.api.libs.json.Json

case class GetNewAddress(label: Option[String], addressType: Option[AddressType])
object GetNewAddress {
  implicit val formatGetNewAddress = Json.format[GetNewAddress]
}
