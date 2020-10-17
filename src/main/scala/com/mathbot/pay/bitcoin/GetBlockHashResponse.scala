package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

case class GetBlockHashResponse(result: String, id: String) extends RpcResponse[String]

object GetBlockHashResponse {
  implicit val formatGetBlockHashResponse = Json.format[GetBlockHashResponse]
}
