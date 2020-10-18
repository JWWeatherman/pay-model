package com.mathbot.pay.bitcoin

import com.mathbot.pay.utils.ResourceHelper
import org.scalatest.FunSuite

class NetworkInfoTest extends FunSuite {
  test("reading json") {
    val js = ResourceHelper.read("/networkInfoTn.json")
    val decodePay = js("result").validate[NetworkInfo]
    assert(decodePay.isSuccess)
  }
}
