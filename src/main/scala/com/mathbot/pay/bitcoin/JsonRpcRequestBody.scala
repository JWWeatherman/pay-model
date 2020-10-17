package com.mathbot.pay.bitcoin

import play.api.libs.json.{JsNumber, JsString, JsValue, Json, OFormat, Writes}
sealed trait BitcoinRpcParam
case class StringParam(s: String) extends BitcoinRpcParam
case class IntParam(i: Int) extends BitcoinRpcParam

object BitcoinRpcParam {

  implicit def string2BitcoinParam(s: String): BitcoinRpcParam = StringParam(s)
  implicit def int2BitcoinParam(s: Int): BitcoinRpcParam = IntParam(s)

  implicit val writesBitcoinRpcParam = new Writes[BitcoinRpcParam] {
    override def writes(o: BitcoinRpcParam): JsValue = o match {
      case StringParam(s) => JsString(s)
      case IntParam(i) => JsNumber(i)
    }
  }
}
case class JsonRpcRequestBody(method: String,
                              params: Seq[BitcoinRpcParam],
                              jsonrpc: String = "1.0",
                              id: String = "scala-jsonrpc")
object JsonRpcRequestBody {
  implicit val writeJsonRpcRequestBody = Json.writes[JsonRpcRequestBody]
}
