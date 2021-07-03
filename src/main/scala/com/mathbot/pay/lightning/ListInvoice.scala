package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class ListInvoice(
    label: String,
    bolt11: String,
    payment_hash: String,
    msatoshi: Long,
    amount_msat: String,
    status: String,
    pay_index: Option[Long],
    msatoshi_received: Option[Long],
    amount_received_msat: Option[String],
    paid_at: Option[Long],
    description: String,
    expires_at: Long,
    bolt12: Option[Bolt12]
) extends LightningJson

object ListInvoice {
  lazy implicit val formatListInvoice: OFormat[ListInvoice] = Json.format[ListInvoice]

}
