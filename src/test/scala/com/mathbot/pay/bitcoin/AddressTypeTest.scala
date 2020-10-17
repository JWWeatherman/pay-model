package com.mathbot.pay.bitcoin

import org.scalatest.FunSuite

class AddressTypeTest extends FunSuite {
  test("toString") {
    assert(AddressType.`p2sh-segwit`.toString === "p2sh-segwit")
  }
}
