package com.mathbot.pay.bitcoin

import play.api.libs.json.Json
import org.scalatest.funsuite.AnyFunSuite

class DetailTest extends AnyFunSuite {
  test("json parse") {
    val address = "3J98t1WpEZ73CNmQviecrnyiWrnqRhWNLy"
    val amount = 2.3
    val category = "send"
    val vout = 0
    val json =
      s"""
         |{
         |  "address" : "$address",
         |  "amount" : $amount,
         |  "category" : "$category",
         |  "vout" : $vout
         |}
      """.stripMargin

    val r = Json.parse(json).validate[Detail]
    assert(r.isSuccess)
  }
}
