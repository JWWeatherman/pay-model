package com.mathbot.pay.bitcoin

import play.api.libs.json.Json
import org.scalatest.funsuite.AnyFunSuite

class GetBalanceResponseTest extends AnyFunSuite {
  test("json") {
    val result = BigDecimal(0.0002)
    val id = "id"
    val json =
      s"""
         |{
         |  "result" : $result,
         |  "id" : "$id"
         |}
      """.stripMargin
    val r = Json.parse(json).validate[GetBalanceResponse]
    assert(r.isSuccess)
  }
}
