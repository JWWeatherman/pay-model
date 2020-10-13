package com.mathbot.pay.bitcoin

import play.api.libs.json.{Json, OFormat}

case class JsonRpcRequestBody(method: String,
                              params: Seq[String],
                              jsonrpc: String = "1.0",
                              id: String = "scala-jsonrpc")
object JsonRpcRequestBody {
  implicit val formatJsonRpcRequestBody: OFormat[JsonRpcRequestBody] = Json.format[JsonRpcRequestBody]
}
