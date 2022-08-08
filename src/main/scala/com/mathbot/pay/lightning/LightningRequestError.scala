package com.mathbot.pay.lightning

import com.mathbot.pay.lightning.LightningRequestError.ErrorCodes
import play.api.libs.json.Json

object LightningRequestError {

  object ErrorCodes {
    val timeoutError = 904 // Timed out while waiting for invoice to be paid
  }
  implicit val formatLightningRequestError = Json.format[LightningRequestError]
}
case class LightningRequestError(code: Int, message: String) {
  lazy val istimeout = code == ErrorCodes.timeoutError
}
