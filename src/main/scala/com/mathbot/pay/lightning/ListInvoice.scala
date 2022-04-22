package com.mathbot.pay.lightning

import fr.acinq.eclair.MilliSatoshi
import com.mathbot.pay.json.EpochSecondInstantFormatter
import com.mathbot.pay.lightning.LightningInvoiceStatus.LightningInvoiceStatus
import play.api.libs.json.{Json, OFormat}

import java.time.Instant

/**
 * @param label unique label supplied at invoice creation
 * @param description description used in the invoice
 * @param payment_hash the hash of the *payment_preimage* which will prove payment
 * @param status Whether it's paid, unpaid or unpayable
 * @param expires_at UNIX timestamp of when it will become / became unpayable
 * @param amount_msat the amount required to pay this invoice
 * @param amount_received_msat the amount actually received (could be slightly greater than *amount_msat*, since clients may overpay)
 * @param bolt11 the BOLT11 string (always present unless *bolt12* is)
 * @param bolt12 the BOLT12 string (always present unless *bolt11* is)
 * @param local_offer_id the *id* of our offer which created this invoice (**experimental-offers** only).
 * @param payer_note the optional *payer_note* from invoice_request which created this invoice (**experimental-offers** only).
 * @param pay_index Unique incrementing index for this payment
 */
case class ListInvoice(
    label: String,
    bolt11: Option[Bolt11],
    payment_hash: String,
    amount_msat: Option[MilliSatoshi],
    amount_received_msat: Option[MilliSatoshi],
    status: LightningInvoiceStatus,
    pay_index: Option[Long],
    paid_at: Option[Instant],
    description: String,
    expires_at: Instant,
    bolt12: Option[String],
    local_offer_id: Option[String],
    payer_note: Option[String],
    payment_preimage: Option[String]
) extends LightningJson {
  val isPaid: Boolean = status == LightningInvoiceStatus.paid
  val isExpired: Boolean = status == LightningInvoiceStatus.expired
  val isUnpaid: Boolean = status == LightningInvoiceStatus.unpaid

  require(bolt11.isDefined || bolt12.isDefined, "Missing 'bolt11' and 'bolt12'")
  if (isPaid) {
    require(pay_index.isDefined, "Missing 'pay_index' for paid invoice")
    require(amount_received_msat.isDefined, "Missing 'amount_received_msat' for paid invoice")
    require(paid_at.isDefined, "Missing 'paid_at' for paid invoice")
    require(payment_preimage.isDefined, "Missing 'payment_preimage' for paid invoice")

  }
}

object ListInvoice extends EpochSecondInstantFormatter {
  lazy implicit val formatListInvoice: OFormat[ListInvoice] = Json.format[ListInvoice]

}
