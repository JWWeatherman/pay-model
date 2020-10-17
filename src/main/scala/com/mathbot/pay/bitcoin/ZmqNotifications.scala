package com.mathbot.pay.bitcoin

object ZmqNotifications extends Enumeration {
  type ZmqNotifications = Value
  val hashblock, hashtx, rawblock, rawtx = Value
}
