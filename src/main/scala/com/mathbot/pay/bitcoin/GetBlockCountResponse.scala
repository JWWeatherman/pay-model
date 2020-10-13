package com.mathbot.pay.bitcoin

import play.api.libs.json.{Json, OFormat}

case class GetBlockCountResponse(result: Int, id: String) extends RpcResponse[Int]
object GetBlockCountResponse {
  implicit val GetBlockCountResponse: OFormat[GetBlockCountResponse] =
    Json.format[GetBlockCountResponse]
}
