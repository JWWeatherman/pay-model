package com.mathbot.pay.lightning

import play.api.libs.json.{JsObject, Json, OFormat}

case class Request(method: String, id: Int, params: JsObject = Json.obj(), jsonrpc: String = "2.0")

object Request {
  lazy implicit val formatRequest: OFormat[Request] = Json.format[Request]
}
