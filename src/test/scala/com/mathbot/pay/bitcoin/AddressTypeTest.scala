package com.mathbot.pay.bitcoin

import org.scalatest.funsuite.AnyFunSuite

class AddressTypeTest extends AnyFunSuite {
  test("toString") {
    assert(AddressType.`p2sh-segwit`.toString === "p2sh-segwit")
  }
}
