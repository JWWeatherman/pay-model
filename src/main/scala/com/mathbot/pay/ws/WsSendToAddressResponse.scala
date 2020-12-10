package com.mathbot.pay.ws

import akka.actor.ActorPath
import com.mathbot.pay.bitcoin.TxId
import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.json.Json

case class WsSendToAddressResponse(result: TxId, onBehalfOf: ActorPath) extends WebsocketMessage

object WsSendToAddressResponse extends PlayJsonSupport {
  implicit val formatWsSendToAddressResponse = Json.format[WsSendToAddressResponse]
}
