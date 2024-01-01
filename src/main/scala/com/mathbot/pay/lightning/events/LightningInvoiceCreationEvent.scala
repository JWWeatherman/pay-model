package com.mathbot.pay.lightning.events

import play.api.libs.json.Json

object LightningInvoiceCreationEvent {
  implicit lazy val readsInvoiceCreation = Json.format[LightningInvoiceCreationEvent]
}

case class LightningInvoiceCreationEvent(invoice_creation: LightningEventDetails)
