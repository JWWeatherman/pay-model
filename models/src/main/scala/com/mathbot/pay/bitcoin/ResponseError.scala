package com.mathbot.pay.bitcoin

import play.api.libs.json.{Json, OFormat}

case class ResponseError(code: Int, message: String)

object ResponseError {
  implicit val formatResponseErrorPlay: OFormat[ResponseError] = Json.format[ResponseError]
}
