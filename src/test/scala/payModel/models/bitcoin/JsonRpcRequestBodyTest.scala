package payModel.models.bitcoin

import org.scalatest.FunSuite
import play.api.libs.json.Json

class JsonRpcRequestBodyTest extends FunSuite {
  test("format sendtoaddress") {
    val method = "sendtoaddress"
    val params = "btcaddress, amount"
    val jsonrpc = "jsonrpc"
    val id = "scala-jsonrpc"
    val json =
      s"""
         |{
         |  "method" : "$method",
         |  "params" : ["$params"],
         |  "jsonrpc" : "$jsonrpc",
         |  "id" : "$id"
         |}
      """.stripMargin

    val r = Json.parse(json).validate[JsonRpcRequestBody]
    assert(r.isSuccess)
  }
  test("format getbalance") {
    val json1 = s"""{"method":"getbalance","params":[],"jsonrpc":"1.0","id":"scala-jsonrpc"}"""
    val json2 = Json.toJson(JsonRpcRequestBody("getbalance", Seq(), "1.0", "scala-jsonrpc")).toString
    assert(json1 == json2)
  }

  test("format sendtoaddress 2") {
    val addr = "adress"
    val amount = "0.000001"
    val json1 = s"""{"method":"sendtoaddress","params":["$addr","$amount"],"jsonrpc":"1.0","id":"scala-jsonrpc"}"""
    val json2 = Json.toJson(JsonRpcRequestBody("sendtoaddress", Seq(addr, amount), "1.0", "scala-jsonrpc")).toString
    assert(json1 === json2)
  }
}
