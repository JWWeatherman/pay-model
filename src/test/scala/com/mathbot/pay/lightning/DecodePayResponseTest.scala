package com.mathbot.pay.lightning

import com.mathbot.pay.utils.ResourceHelper
import org.scalatest.funsuite.AnyFunSuite

class DecodePayResponseTest extends AnyFunSuite {

  test("decode") {
    val js = ResourceHelper.read("/decodePayResponse.json")
    val decodePay = js.validate[DecodePayResponse]
    assert(decodePay.isSuccess)
  }
}
