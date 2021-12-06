package com.mathbot.pay.lightning.url

import com.mathbot.pay.lightning.Bolt11
import play.api.libs.json.Json

case class LightningUrlPayResponse(pr: Bolt11)
object LightningUrlPayResponse {
  implicit val formatLightningUrlPayResponse = Json.format[LightningUrlPayResponse]
}
