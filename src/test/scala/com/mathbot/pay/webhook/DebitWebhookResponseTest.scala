package com.mathbot.pay.webhook

import play.api.libs.json.Json
import org.scalatest.funsuite.AnyFunSuite

class DebitWebhookResponseTest extends AnyFunSuite {

  test("json") {

    val json =
      s"""
         | {
         |  "status": "failed"
         | }
       """.stripMargin

    val r = Json.parse(json).validate[DebitWebhookResponse]
    assert(r.isSuccess)
    assert(r.get.status.toString === "failed")
  }
}
