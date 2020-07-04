package com.mathbot.pay.btcpayserver

object InvoiceException extends Enumeration {
  type InvoiceException = Value
  val paidPartial, paidOver, paidLate, marked = Value
}
