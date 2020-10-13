package com.mathbot.pay.bitcoin

import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.json.{Json, OFormat}

object BalanceDetail {
  implicit val formatBalanceDetail = Json.format[BalanceDetail]
}
case class BalanceDetail(
    trusted: Double,
    untrusted_pending: Double,
    immature: Double
)

object OnChainBalance {
  implicit val formatOnChainBalance = Json.format[OnChainBalance]
}
case class OnChainBalance(mine: BalanceDetail, watchonly: BalanceDetail)
case class GetBalancesResponse(
    id: String,
    result: OnChainBalance
) extends RpcResponse[OnChainBalance]

object GetBalancesResponse extends PlayJsonSupport {
  implicit val formatGetBalancesResponse: OFormat[GetBalancesResponse] = Json.format[GetBalancesResponse]
}
