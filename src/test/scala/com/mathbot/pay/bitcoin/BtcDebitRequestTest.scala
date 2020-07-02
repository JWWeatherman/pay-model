package com.mathbot.pay.bitcoin

import org.scalatest.FunSuite
import play.api.libs.json.Json

import scala.language.{implicitConversions, postfixOps}

class BtcDebitRequestTest extends FunSuite {
  test("json") {
    val btcAddress = "3J98t1WpEZ73CNmQviecrnyiWrnqRhWNLy"
    val amount = 100
    val callbackURL = "https://placeholder.com"
    val uid = "uid"
    val json =
      s"""
         |{
         |  "btcAddress" : "$btcAddress",
         |  "amount" : $amount,
         |  "callbackURL" : "$callbackURL",
         |  "id" : "$uid"
         |}
      """.stripMargin
    val r = Json.parse(json).validate[BtcDebitRequest]
    assert(r.isSuccess)
    assert(r.get.amount === (100 satoshi))
  }
}
