package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

object OnChainBalance {
  implicit val formatOnChainBalance = Json.format[OnChainBalance]
}
case class OnChainBalance(mine: BalanceDetail, watchonly: BalanceDetail)
