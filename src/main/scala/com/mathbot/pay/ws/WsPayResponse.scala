package com.mathbot.pay.ws

import akka.actor.ActorPath
import com.mathbot.pay.json.PlayJsonSupport
import com.mathbot.pay.lightning.Payment
import play.api.libs.json.Json

case class WsPayResponse(payment: Payment, onBehalfOf: ActorPath) extends WebsocketMessage

object WsPayResponse extends PlayJsonSupport {
  implicit val formatWsLightningPayment = Json.format[WsPayResponse]
}
