package com.mathbot.pay.ws

import akka.actor.ActorPath
import com.mathbot.pay.bitcoin.{ResponseError, RpcResponseError}
import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.json.{Json, OFormat}

case class WsRpcResponseError(id: String, error: ResponseError, onBehalfOf: ActorPath) extends WebsocketMessage

object WsRpcResponseError extends PlayJsonSupport {
  implicit val formatWsRpcResponseError: OFormat[WsRpcResponseError] = Json.format[WsRpcResponseError]
  def apply(error: RpcResponseError, onBehalfOf: ActorPath): WsRpcResponseError =
    WsRpcResponseError(error.id, error.error, onBehalfOf)
}
