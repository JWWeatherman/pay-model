package com.mathbot.pay.lightning.events

import play.api.libs.json.Json

object LightningInvoicePaymentEvent {
  implicit lazy val readsInvoicePayment = Json.format[LightningInvoicePaymentEvent]
}

case class LightningInvoicePaymentEvent(invoice_payment: LightningEventDetails)
