package com.mathbot.pay.bitcoin

import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.json.{Json, OFormat}

case class GetBalancesResponse(
    id: String,
    result: OnChainBalance
) extends RpcResponse[OnChainBalance]

object GetBalancesResponse extends PlayJsonSupport {
  implicit val formatGetBalancesResponse: OFormat[GetBalancesResponse] = Json.format[GetBalancesResponse]
}
