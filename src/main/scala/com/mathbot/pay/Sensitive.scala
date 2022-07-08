package com.mathbot.pay

import play.api.libs.json.{Format, JsResult, JsString, JsValue, Json}

case class Sensitive(value: String) {
  override def toString: String = "***"
}

object Sensitive
