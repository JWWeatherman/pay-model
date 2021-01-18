package com.mathbot.pay.bitcoin

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.Json

import scala.io.Source

class AddressInfoTest extends AnyFunSuite with Matchers {

  test("json parse") {
    val s = Source.fromResource("addressInfo.json").getLines().mkString("")
    val j = Json.parse(s)
    val r = j.validate[AddressInfo]
    r.isSuccess shouldBe true
  }
}
