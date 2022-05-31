package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

case class EstimateSmartFeeResponse(result: EstimateSmartFee, id: String) extends RpcResponse[EstimateSmartFee]

object EstimateSmartFeeResponse {
  implicit val formatEstimateSmartFeeResponse = Json.format[EstimateSmartFeeResponse]
}
