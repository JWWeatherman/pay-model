package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

object Bip {
  implicit val formatBip = Json.format[Bip]
}
case class Bip(
    `type`: String,
    active: Boolean,
    height: Double
)
