package com.mathbot.pay.json

import play.api.libs.json.{JsError, JsNumber, JsString, JsSuccess, Reads}

import scala.concurrent.duration.{Duration, FiniteDuration, SECONDS}

trait FiniteDurationToSecondsReader {

  implicit val readsRetryFo: Reads[FiniteDuration] = {
    case JsString(value) =>
      JsSuccess(Duration(value).asInstanceOf[FiniteDuration])
    case JsNumber(value) => JsSuccess(FiniteDuration(value.toLong, SECONDS))
    case _ => JsError("Not a FiniteDuration")
  }
}
