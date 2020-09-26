package com.mathbot.pay.ws

import akka.actor.ActorPath
import com.github.dwickern.macros.NameOf.nameOf
import com.mathbot.pay.bitcoin.CallbackURL
import com.mathbot.pay.json.PlayJsonSupport
import com.mathbot.pay.lightning.{Bolt11, ErrorMsg, LightningRequestError}
import play.api.libs.json._


case class WsLightningRequestError(error: ErrorMsg, bolt11: Option[Bolt11] = None, onBehalfOf: ActorPath)extends WebsocketMessage
object WsLightningRequestError  extends PlayJsonSupport{
  implicit val formatWsLightningRequestError = Json.format[WsLightningRequestError]
  def apply(error: LightningRequestError, onBehalfOf: ActorPath): WsLightningRequestError = WsLightningRequestError(error.error, error.bolt11, onBehalfOf)
}

case class WsLightningDebitRequest(bolt11: Bolt11, callbackURL: CallbackURL, onBehalfOf: ActorPath) extends WebsocketMessage

object WsLightningDebitRequest extends PlayJsonSupport {
  implicit val formatter: Format[WsLightningDebitRequest] = new Format[WsLightningDebitRequest] {

    private implicit val internal: OFormat[WsLightningDebitRequest] =
      Json.format[WsLightningDebitRequest]

    override def writes(o: WsLightningDebitRequest): JsValue =
      Json
        .toJson[WsLightningDebitRequest](o)
        .as[JsObject] + (nameOf(WsLightningDebitRequest) -> JsNull)

    override def reads(json: JsValue): JsResult[WsLightningDebitRequest] = {
      // Just checks to see if the property is in the json object to tag the json, does not care about the value
      json \ nameOf(WsLightningDebitRequest) match {
        case JsDefined(_) => json.validate[WsLightningDebitRequest]
        case undefined: JsUndefined => JsError(s"Not a ${nameOf(WsLightningDebitRequest)}")
      }
    }
  }
}
