package com.mathbot.pay.bitcoin

import play.api.libs.json.{Json, OFormat}

final case class RpcResponseError(id: String, error: ResponseError)
object RpcResponseError {
  implicit val formatRpcResponseError: OFormat[RpcResponseError] = Json.format[RpcResponseError]
}
