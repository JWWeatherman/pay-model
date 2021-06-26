package com.mathbot.pay.bitcoin

import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.json.Json

case class EstimateSmartFee(feeRate: Btc, blocks: Int)

object EstimateSmartFee extends PlayJsonSupport {
  implicit val formatSmartFee = Json.format[EstimateSmartFee]
}
