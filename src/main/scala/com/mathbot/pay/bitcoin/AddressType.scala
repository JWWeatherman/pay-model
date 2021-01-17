package com.mathbot.pay.bitcoin

import com.softwaremill.macwire.wireSet

object AddressType extends Enumeration {
  type AddressType = Value
  val `p2sh-segwit` = Value("p2sh-segwit")
  val legacy, bech32 = Value

  val all = wireSet[AddressType]
}
