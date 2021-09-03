package com.mathbot.pay.json

import play.api.libs.json.{JsError, JsString, JsSuccess, Reads}

import scala.concurrent.duration.{Duration, FiniteDuration, SECONDS}

trait FiniteDurationToSecondsReader {

  implicit val readsRetryFo: Reads[FiniteDuration] = {
    case JsString(value) =>
      JsSuccess(FiniteDuration(Duration(value).toSeconds, SECONDS))
    case _ => JsError("Not a FiniteDuration")
  }
}
