package com.mathbot.pay.bitcoin

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class AddressTypeTest extends AnyFunSuite with Matchers {
  test("toString") {
    assert(AddressType.`p2sh-segwit`.toString === "p2sh-segwit")
  }
  test("all") {
    AddressType.all should contain theSameElementsAs AddressType.`p2sh-segwit` :: AddressType.legacy :: AddressType.bech32 :: Nil
  }
}
