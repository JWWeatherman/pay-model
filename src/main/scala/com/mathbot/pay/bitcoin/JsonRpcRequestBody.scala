package com.mathbot.pay.bitcoin

import play.api.libs.json._

case class JsonRpcRequestBody(method: String, params: JsArray, jsonrpc: String = "1.0", id: String = "scala-jsonrpc")
object JsonRpcRequestBody {
  implicit val writeJsonRpcRequestBody = Json.writes[JsonRpcRequestBody]
}
