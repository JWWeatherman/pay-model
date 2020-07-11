package com.mathbot.pay.bitcoin

import com.mathbot.pay.lightning.PayStatus.PayStatus
import org.scalatest.FunSuite
import play.api.libs.json.Json

class PayStatusTest extends FunSuite {

  val c = s"""{"status": "complete"}"""
  val f = s"""{"status": "failed"}"""
  val p = s"""{"status": "pending"}"""

  test("json") {
    var r = Json.parse(c)("status").validate[PayStatus]
    assert(r.isSuccess)

    r = Json.parse(f)("status").validate[PayStatus]
    assert(r.isSuccess)

    r = Json.parse(p)("status").validate[PayStatus]
    assert(r.isSuccess)
  }
  //  test("json string") {
  //
  //    var r = Json.toJson(PayStatus.complete)
  //    assert(r === "complete")
  //
  //    r = Json.toJson(PayStatus.failed)
  //    assert(r === "failed")
  //
  //    r = Json.toJson(PayStatus.pending)
  //    assert(r === "pending")
  //  }
}
