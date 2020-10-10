package com.mathbot.pay.bitcoin

object AddressType extends Enumeration {
  type AddressType = Value
  val `p2sh-segwit` = Value("p2sh-segwit")
  val legacy, bech32 = Value
}
