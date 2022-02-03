package com.mathbot.pay

import play.api.libs.json.Json

case class Sensitive(value: String) {
  override def toString: String = "***"
}

object Sensitive {
  implicit val formatSensitive = Json.format[Sensitive]
}
