package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class Request(method: String, id: Int, params: Array[String] = Array.empty[String])

object Request {
  lazy implicit val formatRequest: OFormat[Request] = Json.format[Request]

}
