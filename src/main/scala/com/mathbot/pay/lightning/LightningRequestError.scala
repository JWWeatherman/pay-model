package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class LightningRequestError(error: ErrorMsg) extends LightningJson

object LightningRequestError {
  lazy implicit val formatRequestError: OFormat[LightningRequestError] = Json.format[LightningRequestError]
  def apply(code: Int, message: String): LightningRequestError =
    LightningRequestError(ErrorMsg(code = code, message = message))
}
