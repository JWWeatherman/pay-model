package com.mathbot.pay.ws

import com.mathbot.pay.bitcoin.TxId
import org.scalatest.{FunSuite, Matchers}
import play.api.libs.json.{JsNull, Json}

class WsWalletTransactionInfoRequestTest extends FunSuite with Matchers {

  val txId = TxId("f4184fc596403b9d638783cf57adfe4c75c605f6356fbc91338530e9831e9e16")
  test("json serialization") {
    val r = WsWalletTransactionInfoRequest(txId)
    val json = Json.toJson(r)
    json("WsWalletTransactionInfoRequest") shouldEqual JsNull
    json("txId").as[TxId] shouldEqual txId
  }
  test("json deserialization from null key") {
    val json =
      s"""
        |{ "WsWalletTransactionInfoRequest": null, "txId": "$txId"  }
        |""".stripMargin
    val j = Json.parse(json)
    val r = j.validate[WsWalletTransactionInfoRequest]
    r.isSuccess shouldBe true
  }
  test("json deserialization from blank string key") {
    val json =
      s"""
         |{ "WsWalletTransactionInfoRequest": "", "txId": "$txId"  }
         |""".stripMargin
    val j = Json.parse(json)
    val r = j.validate[WsWalletTransactionInfoRequest]
    r.isSuccess shouldBe true
  }
  test("IllegalArgumentException thrown when valid key and invalid txId") {
    val json =
      s"""
         |{ "WsWalletTransactionInfoRequest": "", "txId": "${txId.txId + "E"}"  }
         |""".stripMargin
    val j = Json.parse(json)
    assertThrows[IllegalArgumentException] {
      j.validate[WsWalletTransactionInfoRequest]
    }
  }
  test("json deserialization error with bad key") {
    val json =
      s"""
         |{ "WalletTransactionInfoRequest": "", "txId": "$txId"  }
         |""".stripMargin
    val j = Json.parse(json)
    val r = j.validate[WsWalletTransactionInfoRequest]
    r.isError shouldBe true
  }
  test("json deserialization error with invalid key and txId") {
    val json =
      s"""
         |{ "WalletTransactionInfoRequest": "", "txId": "${txId.txId + "E"}"  }
         |""".stripMargin
    val j = Json.parse(json)
    val r = j.validate[WsWalletTransactionInfoRequest]
    r.isError shouldBe true
  }
}
