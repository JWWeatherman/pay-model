package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

case class Success(success: Boolean)
object Success {
  implicit val formatSuccess = Json.format[Success]
}
