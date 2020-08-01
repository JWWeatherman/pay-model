package com.mathbot.pay.ws

import akka.actor.ActorRef
import play.api.libs.json.{JsResult, JsValue}

object SocketMessageFactoryTypes {
  type InboundMessageFactory = PartialFunction[JsValue, (JsResult[AnyRef], ActorRef)]

  type OutboundMessageFactory = PartialFunction[AnyRef, JsValue]
}
