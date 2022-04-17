package com.mathbot.pay.lightning.url

import fr.acinq.eclair.MilliSatoshi
import play.api.libs.json.Json

object LightingUrlWithdrawRequestResponse {
  implicit val formatLightingUrlWithdrawRequestResponse = Json.format[LightingUrlWithdrawRequestResponse]
}
case class LightingUrlWithdrawRequestResponse(
    tag: String = "withdrawRequest",
    callback: String,
    k1: String,
    defaultDescription: String,
    minWithdrawable: MilliSatoshi,
    maxWithdrawable: MilliSatoshi,
    balanceCheck: Option[String] = None
)
