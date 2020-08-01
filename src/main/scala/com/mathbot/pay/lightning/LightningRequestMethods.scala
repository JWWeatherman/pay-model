package com.mathbot.pay.lightning

object LightningRequestMethods extends Enumeration {
  type LightningRequestMethods = Value
  val listpays, pay, getinfo = Value
}
