package com.mathbot.pay.bitcoin

import org.scalatest.FunSuite
import play.api.libs.json.Json

class SendToAddressResponseTest extends FunSuite {
  test("json") {
    val txid = "f4184fc596403b9d638783cf57adfe4c75c605f6356fbc91338530e9831e9e16"
    val id = "id"
    val json =
      s"""
         |{
         |  "result" : "$txid",
         |  "id" : "$id"
         |}
      """.stripMargin
    val r = Json.parse(json).validate[SendToAddressResponse]
    assert(r.isSuccess)
  }
  test("json fail") {
    val txid = "f4184fc596403b9d638783cf57adfe4c75c605f63c91338530e9831e9e16"
    val id = "id"
    val json =
      s"""
         |{
         |  "result" : "$txid",
         |  "id" : "$id"
         |}
      """.stripMargin
    assertThrows[IllegalArgumentException] {
      val r = Json.parse(json).validate[SendToAddressResponse]
    }
  }
}
