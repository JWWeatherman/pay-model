package com.mathbot.pay.ws

import akka.actor.ActorPath
import com.mathbot.pay.json.PlayJsonSupport
import com.mathbot.pay.lightning.{Bolt11, ErrorMsg, LightningRequestError}
import play.api.libs.json.Json

case class WsLightningRequestError(error: ErrorMsg, bolt11: Option[Bolt11] = None, onBehalfOf: ActorPath)
    extends WebsocketMessage

object WsLightningRequestError extends PlayJsonSupport {
  implicit val formatWsLightningRequestError = Json.format[WsLightningRequestError]
  def apply(error: LightningRequestError, onBehalfOf: ActorPath): WsLightningRequestError =
    WsLightningRequestError(error.error, error.bolt11, onBehalfOf)
}
