package com.mathbot.pay.webhook

import org.scalatest.FunSuite
import play.api.libs.json.Json

class DebitWebhookResponseTest extends FunSuite {

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
