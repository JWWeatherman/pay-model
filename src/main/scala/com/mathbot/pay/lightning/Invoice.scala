package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class Invoice(payment_hash: String, expires_at: Long, bolt11: String)

object Invoice {

  lazy implicit val formatInvoice: OFormat[Invoice] = Json.format[Invoice]

}
