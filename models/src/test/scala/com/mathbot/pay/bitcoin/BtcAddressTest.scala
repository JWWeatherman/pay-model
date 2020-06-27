package com.mathbot.pay.bitcoin

import org.scalatest.FunSuite

class BtcAddressTest extends FunSuite {
  test("create class") {
    val address = "3J98t1WpEZ73CNmQviecrnyiWrnqRhWNLy"
    val r = BtcAddress(address)
    assert(r.address === address)
  }
  test("validateAddr") {
    val address = "3J98t1WpEZ73CNmQviecrnyiWrnqRhWNLy"
    val r = BtcAddress.validateAddr(address)
    assert(r === true)
  }
  test("invalidAddr") {
    val address = "3J98t1WpEZ73CNmQviecrnyiWrnqRhWNL"
    val r = BtcAddress.validateAddr(address)
    assert(r === false)
  }
}
