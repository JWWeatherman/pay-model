package com.mathbot.pay.ws

import com.mathbot.pay.ws.SocketMessageFactoryTypes.{InboundMessageFactory, OutboundMessageFactory}

trait SocketMessageFactory {
  def inboundMessageFactory: InboundMessageFactory
  def outboundMessageFactory: OutboundMessageFactory
}
