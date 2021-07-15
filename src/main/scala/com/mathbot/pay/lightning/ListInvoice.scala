package com.mathbot.pay.lightning

import com.mathbot.pay.bitcoin.MilliSatoshi
import com.mathbot.pay.json.EpochSecondInstantFormatter
import com.mathbot.pay.lightning.LightningInvoiceStatus.LightningInvoiceStatus
import play.api.libs.json.{Json, OFormat}

import java.time.Instant

case class ListInvoice(
    label: String,
    bolt11: Option[Bolt11],
    payment_hash: String,
    msatoshi: Option[MilliSatoshi],
    amount_msat: Option[String],
    status: LightningInvoiceStatus,
    pay_index: Option[Long],
    msatoshi_received: Option[MilliSatoshi],
    amount_received_msat: Option[String],
    paid_at: Option[Instant],
    description: String,
    expires_at: Long,
    bolt12: Option[String],
    local_offer_id: Option[String]
) extends LightningJson

object ListInvoice extends EpochSecondInstantFormatter {
  lazy implicit val formatListInvoice: OFormat[ListInvoice] = Json.format[ListInvoice]

}
