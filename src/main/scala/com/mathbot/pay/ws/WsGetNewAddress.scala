package com.mathbot.pay.ws

import com.github.dwickern.macros.NameOf.nameOf
import com.mathbot.pay.bitcoin.AddressType.AddressType
import play.api.libs.json._

case class WsGetNewAddress(label: Option[String], addressType: Option[AddressType])

object WsGetNewAddress {
  implicit val formatter: Format[WsGetNewAddress] = new Format[WsGetNewAddress] {

    private implicit val internal: OFormat[WsGetNewAddress] = Json.format[WsGetNewAddress]

    override def writes(o: WsGetNewAddress): JsValue =
      Json
        .toJson[WsGetNewAddress](o)
        .as[JsObject] + (nameOf(WsGetNewAddress) -> JsNull)

    override def reads(json: JsValue): JsResult[WsGetNewAddress] = {
      // Just checks to see if the property is in the json object to tag the json, does not care about the value
      json \ nameOf(WsGetNewAddress) match {
        case JsDefined(_) => json.validate[WsGetNewAddress]
        case undefined: JsUndefined => JsError(s"Not a ${nameOf(WsGetNewAddress)}")
      }
    }
  }
}
