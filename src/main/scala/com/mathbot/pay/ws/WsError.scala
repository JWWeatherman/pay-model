package com.mathbot.pay.ws

import com.github.dwickern.macros.NameOf.nameOf
import play.api.libs.json._

case class WsError(errors: Seq[(String, Seq[String])])

object WsError {

  def apply(jsError: JsError): WsError =
    WsError(
      jsError.errors.map { t =>
        t._1.toString() -> t._2.map(jve => jve.message)
      }
    )

  def apply(msg: String): WsError = {
    // Note: JsPath means "root" of object a.k.a the top which looks like an empty string "" or maybe a /
    WsError(Seq((JsPath.toString(), Seq(msg))))
  }

  implicit val formatter: Format[WsError] = new Format[WsError] {

    private implicit val internal: OFormat[WsError] = Json.format[WsError]

    override def writes(o: WsError): JsValue =
      Json
        .toJson[WsError](o)
        .as[JsObject] + (nameOf(WsError) -> JsNull)

    override def reads(json: JsValue): JsResult[WsError] = {
      // Just checks to see if the property is in the json object to tag the json, does not care about the value
      json \ nameOf(WsError) match {
        case JsDefined(_) => json.validate[WsError]
        case undefined: JsUndefined => JsError(s"Not a ${nameOf(WsError)}")
      }
    }
  }
}
