package com.mathbot.pay.ws

import com.github.dwickern.macros.NameOf.nameOf
import com.mathbot.pay.lightning.Bolt11
import play.api.libs.json._

case class WsListPaysRequest(bolt11: Bolt11)

object WsListPaysRequest {
  implicit val formatter: Format[WsListPaysRequest] = new Format[WsListPaysRequest] {

    private implicit val internal: OFormat[WsListPaysRequest] =
      Json.format[WsListPaysRequest]

    override def writes(o: WsListPaysRequest): JsValue =
      Json
        .toJson[WsListPaysRequest](o)
        .as[JsObject] + (nameOf(WsListPaysRequest) -> JsNull)

    override def reads(json: JsValue): JsResult[WsListPaysRequest] = {
      // Just checks to see if the property is in the json object to tag the json, does not care about the value
      json \ nameOf(WsListPaysRequest) match {
        case JsDefined(_) => json.validate[WsListPaysRequest]
        case undefined: JsUndefined => JsError(s"Not a ${nameOf(WsListPaysRequest)}")
      }
    }
  }
}
