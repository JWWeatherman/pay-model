package com.mathbot.pay.lightning

import fr.acinq.eclair.MilliSatoshi

import java.time.Instant
import com.mathbot.pay.json.EpochSecondInstantFormatter
import com.mathbot.pay.lightning.PayStatus.{complete, paid, pending, PayStatus}
import fr.acinq.eclair.payment.Bolt11Invoice
import play.api.libs.json.{Json, OFormat}
// https://github.com/ElementsProject/lightning/blob/master/doc/lightning-listpays.7.md
// For old payments (pre-0.7) we didn’t save the bolt11 string, so in its place are three other fields: payment_hash,destination,amount_msat
/**
 * @param payment_hash the hash of the *payment_preimage* which will prove payment
 * @param status status of the payment
 * @param destination the final destination of the payment if known
 * @param created_at the UNIX timestamp showing when this payment was initiated
 * @param label the label, if given to sendpay
 * @param bolt11 the bolt11 string (if pay supplied one)
 * @param bolt12 the bolt12 string (if supplied for pay: **experimental-offers** only).
 * @param amount_msat the amount the destination received, if known
 * @param amount_sent_msat the amount we actually sent, including fees
 * @param preimage proof of payment
 * @param number_of_parts the number of parts for a successful payment (only if more than one).
 * @param erroronion the error onion returned on failure, if any.
 */
case class ListPay(
    bolt11: Option[Bolt11],
    status: PayStatus,
    destination: Option[String],
    amount_msat: Option[MilliSatoshi],
    amount_sent_msat: MilliSatoshi,
    created_at: Instant,
    preimage: Option[String] = None,
    payment_hash: Option[String] = None,
    label: Option[String] = None,
    bolt12: Option[String],
    number_of_parts: Option[Long] = None,
    erroronion: Option[String] = None
) {
  val isPending: Boolean = status == pending
  val isPaid: Boolean = status == complete || status == paid
}

object ListPay extends EpochSecondInstantFormatter {
  def apply(bolt11: Bolt11, payment: Payment, label: Option[String]): ListPay =
    ListPay(
      bolt11 = Some(bolt11),
      status = payment.status,
      destination = payment.destination,
      amount_msat = Some(payment.amount_msat),
      amount_sent_msat = payment.amount_sent_msat,
      created_at = payment.created_at,
      preimage = payment.payment_preimage,
      payment_hash = Some(payment.payment_hash),
      label = label,
      bolt12 = None,
      number_of_parts = payment.parts.map(_.toLong),
      erroronion = None
    )

  lazy implicit val formatListPay: OFormat[ListPay] = Json.format[ListPay]
}
