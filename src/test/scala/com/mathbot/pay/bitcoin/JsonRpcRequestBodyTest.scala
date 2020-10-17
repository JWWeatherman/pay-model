package com.mathbot.pay.bitcoin

import org.scalatest.FunSuite
import play.api.libs.json.Json

class JsonRpcRequestBodyTest extends FunSuite {

  test("format getbalance") {
    val json1 = s"""{"method":"getbalance","params":[],"jsonrpc":"1.0","id":"scala-jsonrpc"}"""
    val json2 = Json.toJson(JsonRpcRequestBody("getbalance", Seq(), "1.0", "scala-jsonrpc")).toString
    assert(json1 == json2)
  }

  test("format sendtoaddress 2") {
    val addr = "adress"
    val amount = "0.000001"
    val json1 = s"""{"method":"sendtoaddress","params":["$addr","$amount"],"jsonrpc":"1.0","id":"scala-jsonrpc"}"""
    val json2 = Json.toJson(JsonRpcRequestBody("sendtoaddress", Seq(addr, amount), "", "")).toString
    assert(json1 === json2)
  }
}
