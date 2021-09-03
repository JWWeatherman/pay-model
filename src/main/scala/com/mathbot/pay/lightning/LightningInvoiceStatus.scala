package com.mathbot.pay.lightning

import play.api.libs.json.Json

// https://lightning.readthedocs.io/lightning-listinvoices.7.html
object LightningInvoiceStatus extends Enumeration {
  type LightningInvoiceStatus = Value
  val unpaid, paid, expired = Value

  implicit val formatLightningInvoiceStatus = Json.formatEnum(this)
}
