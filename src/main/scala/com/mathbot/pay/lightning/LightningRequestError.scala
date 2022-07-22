package com.mathbot.pay.lightning

import play.api.libs.json.Json

object LightningRequestError {
  implicit val formatLightningRequestError = Json.format[LightningRequestError]
}
case class LightningRequestError(code: Int, message: String)
