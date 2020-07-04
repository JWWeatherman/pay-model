package com.mathbot.pay.btcpayserver

import play.api.libs.json.{Json, OFormat}

case class ChargeInfoResponse(
    data: ChargeInfoData
)

object ChargeInfoResponse {

  implicit val formatChargeInfoResponse: OFormat[ChargeInfoResponse] = Json.format[ChargeInfoResponse]
}
