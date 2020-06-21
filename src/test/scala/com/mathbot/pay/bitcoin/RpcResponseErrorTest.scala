package com.mathbot.pay.bitcoin

import org.scalatest.FunSuite
import play.api.libs.json.Json

class RpcResponseErrorTest extends FunSuite {
  test("json") {
    val msg = "Loading block index..."
    val json =
      s"""
        |{"result":null,"error":{"code":-28,"message":"$msg"},"id":"scala-jsonrpc"}
      """.stripMargin

    val r = Json.parse(json).validate[RpcResponseError]
    assert(r.isSuccess)
    val rpc = r.get
    assert(rpc.error.code === -28)
    assert(rpc.error.message === msg)

  }
}
