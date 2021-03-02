package com.mathbot.pay.btcpayserver

import play.api.libs.json.Json

object InvoiceException extends Enumeration {
  type InvoiceException = Value
  val paidPartial, paidOver, paidLate, marked = Value
  implicit val formatIE = Json.formatEnum(this)
}
