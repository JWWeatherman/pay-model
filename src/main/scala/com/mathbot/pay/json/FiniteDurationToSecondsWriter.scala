package com.mathbot.pay.json

import play.api.libs.json.{JsNumber, JsValue, Json, Writes}

import scala.concurrent.duration.FiniteDuration

trait FiniteDurationToSecondsWriter {

  implicit val formatFiniteDuration: Writes[FiniteDuration] = (o: FiniteDuration) => JsNumber(o.toSeconds)

}
