package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

case class RpcSuccess(success: Boolean)
object RpcSuccess {
  implicit val formatSuccess = Json.format[RpcSuccess]
}
