package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class ListPaysResponse(jsonrpc: String, id: Long, result: Pays) extends Response[Pays]

object ListPaysResponse {
  lazy implicit val formatListPaysResponse: OFormat[ListPaysResponse] = Json.format[ListPaysResponse]

}
