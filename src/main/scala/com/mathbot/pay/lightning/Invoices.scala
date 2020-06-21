package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class Invoices(invoices: Seq[ListInvoice])

object Invoices {

  lazy implicit val formatInvoices: OFormat[Invoices] = Json.format[Invoices]

}
