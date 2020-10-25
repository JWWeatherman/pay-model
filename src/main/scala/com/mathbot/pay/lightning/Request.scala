package com.mathbot.pay.lightning

import play.api.libs.json.{JsObject, Json, OFormat}

case class Request(method: String, id: Int, params: JsObject = Json.obj(), jsonrpc: String = Request.json2)

object Request {
  val json2 = "2.0"
  lazy implicit val formatRequest: OFormat[Request] = Json.format[Request]
}
