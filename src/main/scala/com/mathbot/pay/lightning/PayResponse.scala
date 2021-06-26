package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class PayResponse(id: Long, jsonrpc: String, result: Payment) extends Response[Payment]

object PayResponse {
  lazy implicit val formatPayResponse: OFormat[PayResponse] = Json.format[PayResponse]
}
