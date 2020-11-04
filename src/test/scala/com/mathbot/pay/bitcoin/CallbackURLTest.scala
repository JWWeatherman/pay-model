package com.mathbot.pay.bitcoin

import com.mathbot.pay.webhook.CallbackURL
import org.scalatest.FunSuite
import play.api.libs.json.Json

class CallbackURLTest extends FunSuite {
  test("json") {
    val callbackURL = "https://example.com"
    val json =
      s"""
           |{
           |  "callbackURL" : "$callbackURL"
           |}
      """.stripMargin
    val r = Json.parse(json)("callbackURL").validate[CallbackURL]
    assert(r.isSuccess)
  }

  test("toString") {
    val url = "https://mathbot.com"
    val c = CallbackURL(url)
    assert(c.toString == url)
  }
}
