package com.mathbot.pay.ws

import com.github.dwickern.macros.NameOf.nameOf
import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.json._

case class WsGetInvoice(id: String)

object WsGetInvoice extends PlayJsonSupport {
  implicit val formatter: Format[WsGetInvoice] = new Format[WsGetInvoice] {

    private implicit val internal: OFormat[WsGetInvoice] = Json.format[WsGetInvoice]

    override def writes(o: WsGetInvoice): JsValue =
      Json
        .toJson[WsGetInvoice](o)
        .as[JsObject] + (nameOf(WsGetInvoice) -> JsNull)

    override def reads(json: JsValue): JsResult[WsGetInvoice] = {
      // Just checks to see if the property is in the json object to tag the json, does not care about the value
      json \ nameOf(WsGetInvoice) match {
        case JsDefined(_) => json.validate[WsGetInvoice]
        case undefined: JsUndefined => JsError(s"Not a ${nameOf(WsGetInvoice)}")
      }
    }
  }
}
