package com.mathbot.pay.lightning

import org.scalatest.funsuite.AnyFunSuite
import play.api.libs.json.Json

class ListInvoicesResponseTest extends AnyFunSuite {
  test("json parsing") {
    val s = scala.io.Source.fromResource("listInvoicesResponse.json").getLines().mkString("")
    val j = Json.parse(s)
    val r = j.validate[ListInvoicesResponse]
    assert(r.isSuccess)
  }
}
