package com.mathbot.pay.ws

import akka.actor.ActorPath
import com.mathbot.pay.json.PlayJsonSupport
import com.mathbot.pay.lightning.{ListPaysResponse, Pays}
import play.api.libs.json.Json

case class WsListPaysResponse(pays: Pays, onBehalfOf: ActorPath) extends WebsocketMessage

object WsListPaysResponse extends PlayJsonSupport {
  implicit val formatWsListPaysResponse = Json.format[WsListPaysResponse]
  def apply(res: ListPaysResponse, onBehalfOf: ActorPath): WsListPaysResponse =
    WsListPaysResponse(res.result, onBehalfOf)
}
