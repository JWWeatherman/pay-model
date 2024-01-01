package com.mathbot.pay.lightning.events

import play.api.libs.json.Json

object LightningSendPayFailure {
  implicit val formatSendPayFailure = Json.format[LightningSendPayFailure]
}

case class LightningSendPayFailure(code: Int, message: String, data: LightningSendPayDetails)
