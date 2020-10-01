package com.mathbot.pay.ws

import com.mathbot.pay.ws.SocketMessageFactoryTypes.{InboundMessageFactory, OutboundMessageFactory}

trait MessageFactory {

  def inboundFactories: InboundMessageFactory
  def outboundFactories: OutboundMessageFactory

}
