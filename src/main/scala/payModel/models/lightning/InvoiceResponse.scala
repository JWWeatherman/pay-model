package payModel.models.lightning

import play.api.libs.json.{Json, OFormat}

case class InvoiceResponse(jsonrpc: String, id: Long, result: Invoice) extends Response[Invoice]

object InvoiceResponse {

  lazy implicit val formatInvoiceResponse: OFormat[InvoiceResponse] = Json.format[InvoiceResponse]

}
