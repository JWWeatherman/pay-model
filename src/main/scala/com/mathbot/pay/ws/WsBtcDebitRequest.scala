package com.mathbot.pay.ws

import akka.actor.ActorPath
import com.github.dwickern.macros.NameOf.nameOf
import com.mathbot.pay.bitcoin.{BtcAddress, CallbackURL, Satoshi}
import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.json._

case class WsBtcDebitRequest(btcAddress: BtcAddress,
                             amount: Satoshi,
                             callbackURL: CallbackURL,
                             id: String,
                             onBehalfOf: ActorPath)
    extends WebsocketMessage

object WsBtcDebitRequest extends PlayJsonSupport {
  implicit val formatter: Format[WsBtcDebitRequest] = new Format[WsBtcDebitRequest] {

    private implicit val internal: OFormat[WsBtcDebitRequest] = Json.format[WsBtcDebitRequest]

    override def writes(o: WsBtcDebitRequest): JsValue =
      Json
        .toJson[WsBtcDebitRequest](o)
        .as[JsObject] + (nameOf(WsBtcDebitRequest) -> JsNull)

    override def reads(json: JsValue): JsResult[WsBtcDebitRequest] = {
      // Just checks to see if the property is in the json object to tag the json, does not care about the value
      json \ nameOf(WsBtcDebitRequest) match {
        case JsDefined(_) => json.validate[WsBtcDebitRequest]
        case undefined: JsUndefined => JsError(s"Not a ${nameOf(WsBtcDebitRequest)}")
      }
    }
  }
}
