package com.mathbot.pay.lightning.url

import play.api.libs.json.Json

case class LightningUrlError(reason: String, status: String = "ERROR")
object LightningUrlError {
  implicit val froamtLightningUrlError = Json.format[LightningUrlError]
}
