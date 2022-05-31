package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

object BalanceDetail {
  implicit val formatBalanceDetail = Json.format[BalanceDetail]
}
case class BalanceDetail(
    trusted: Double,
    untrusted_pending: Double,
    immature: Double
)
