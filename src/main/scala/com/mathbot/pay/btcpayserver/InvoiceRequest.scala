package com.mathbot.pay.btcpayserver

import play.api.libs.json.Json
import sttp.model.Uri

case class InvoiceRequest(
    buyer: Buyer,
    orderId: String,
    itemDesc: String,
    redirectUrl: Option[String],
    notificationUrl: String,
    price: BigDecimal,
    currency: String = "USD"
)
object InvoiceRequest {
  implicit val formatInvoiceRequest = Json.format[InvoiceRequest]
}
