package com.mathbot.pay.lightningcharge

import play.api.libs.json.Json

// https://lightning.readthedocs.io/lightning-listinvoices.7.html
object LightningChargeInvoiceStatus extends Enumeration {
  type LightningChargeInvoiceStatus = Value
  val unpaid, paid, expired = Value

  implicit val formatInvoiceStatus = Json.formatEnum(this)
}
