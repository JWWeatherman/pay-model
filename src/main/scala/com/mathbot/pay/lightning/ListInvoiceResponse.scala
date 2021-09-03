package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class ListInvoiceResponse(jsonrpc: String, id: Int, result: ListInvoice) extends LightningJson

object ListInvoiceResponse {

  lazy implicit val formatListInvoicesResponse: OFormat[ListInvoiceResponse] = Json.format[ListInvoiceResponse]

}
