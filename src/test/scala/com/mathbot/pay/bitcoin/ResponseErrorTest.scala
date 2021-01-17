package com.mathbot.pay.bitcoin

import play.api.libs.json.Json
import org.scalatest.funsuite.AnyFunSuite

class ResponseErrorTest extends AnyFunSuite {
  test("json") {
    val message = "random message"
    val code = 1
    val json =
      s"""
         |{
         |  "message" : "$message",
         |  "code" : $code
         |}
      """.stripMargin

    val r = Json.parse(json).validate[ResponseError]
    assert(r.isSuccess)
  }

}
