package com.mathbot.pay.lightning

import org.scalatest.funsuite.AnyFunSuite
import play.api.libs.json.Json

import scala.io.Source

class DecodePayResponseTest extends AnyFunSuite {

  test("decode") {
    val js = Json.parse(Source.fromResource("decodePayResponse.json").getLines().mkString(""))
    val decodePay = js.validate[DecodePayResponse]
    assert(decodePay.isSuccess)
  }
}
