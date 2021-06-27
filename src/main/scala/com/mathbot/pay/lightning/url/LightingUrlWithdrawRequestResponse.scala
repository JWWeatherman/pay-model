package com.mathbot.pay.lightning.url

import play.api.libs.json.Json

object LightingUrlWithdrawRequestResponse {
  implicit val formatLightingUrlWithdrawRequestResponse = Json.format[LightingUrlWithdrawRequestResponse]
}
case class LightingUrlWithdrawRequestResponse(
    tag: String = "withdrawRequest",
    callback: String,
    k1: String,
    defaultDescription: String,
    minWithdrawable: Long,
    maxWithdrawable: Long,
    balanceCheck: Option[String] = None
)
