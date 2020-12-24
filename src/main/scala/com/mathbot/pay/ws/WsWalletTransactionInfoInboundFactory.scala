package com.mathbot.pay.ws

import akka.actor.ActorRef
import com.mathbot.pay.ws.SocketMessageFactoryTypes.InboundMessageFactory
import play.api.libs.json.{JsResult, JsValue, Json}

// Not: Added "TAG" type parameter to so macwire will work in Pay
class WsWalletTransactionInfoInboundFactory(supervisor: ActorRef) extends InboundMessageFactory {

  override def isDefinedAt(x: JsValue): Boolean = WsWalletTransactionInfoRequest.isJsonForThis(x)

  override def apply(v1: JsValue): (JsResult[AnyRef], ActorRef) = {
    (Json.fromJson[WsWalletTransactionInfoRequest](v1), supervisor)
  }
}
