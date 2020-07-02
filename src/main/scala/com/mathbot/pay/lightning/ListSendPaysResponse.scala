package com.mathbot.pay.lightning

import play.api.libs.json.Json

case class ListSendPaysResponse(jsonrpc: String, id: Long, result: Payments) extends Response[Payments]

object ListSendPaysResponse {

  implicit val formatListSendPaysResponse = Json.format[ListSendPaysResponse]
}
