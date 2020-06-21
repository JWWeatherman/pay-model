package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class ListInvoicesResponse(jsonrpc: String, id: Int, result: Invoices)

object ListInvoicesResponse {

  lazy implicit val formatListInvoicesResponse: OFormat[ListInvoicesResponse] = Json.format[ListInvoicesResponse]

}
