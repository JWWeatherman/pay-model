package com.mathbot.pay.bitcoin

import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.json.{Json, OFormat}

case class GetBalanceResponse(result: Btc, id: String) extends RpcResponse[Btc]
object GetBalanceResponse extends PlayJsonSupport {
  implicit val formatGetBalanceResponse: OFormat[GetBalanceResponse] = Json.format[GetBalanceResponse]
}
