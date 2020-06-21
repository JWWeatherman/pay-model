package com.mathbot.pay.json

import java.time.Instant

import play.api.libs.json._

import scala.util.Try

// reads and writes time from epoch second
trait EpochSecondInstantFormatter {
  implicit val formatInstant: Format[Instant] = new Format[Instant] {
    override def reads(json: JsValue): JsResult[Instant] = json match {
      case JsNumber(value) => JsSuccess(Instant.ofEpochSecond(value.toLong))
      case JsString(value) =>
        Try(Instant.parse(value)).map(JsSuccess(_)) getOrElse
        Try(value.toLong)
          .map(s => Instant.ofEpochSecond(s))
          .map(JsSuccess(_))
          .getOrElse {
            JsError(s"Invalid Instant format value = $value")
          }
      case other =>
        JsError(s"Invalid Instant format value = $other")
    }

    override def writes(o: Instant): JsValue = JsNumber(o.getEpochSecond)
  }
}
