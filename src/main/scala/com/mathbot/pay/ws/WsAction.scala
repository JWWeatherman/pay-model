package com.mathbot.pay.ws

import play.api.libs.json.Json

case class WsAction(action: String)

object WsAction {
  implicit val formatWsAction = Json.format[WsAction]
}
