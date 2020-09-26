package com.mathbot.pay.ws

import akka.actor.ActorPath

trait WebsocketMessage {
  def onBehalfOf: ActorPath

}
