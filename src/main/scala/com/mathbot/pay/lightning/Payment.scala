package com.mathbot.pay.lightning

import com.google.common.hash.Hashing
import com.mathbot.pay.bitcoin.MilliSatoshi
import com.mathbot.pay.json.EpochSecondInstantFormatter
import com.mathbot.pay.lightning.PayStatus.PayStatus
import play.api.libs.json._

import java.time.Instant

/**
 * @param amount_msat
 * @param amount_sent_msat
 * @param created_at
 * @param destination node
 * @param msatoshi
 * @param msatoshi_sent
 * @param payment_hash hex string of sha256 of payment_preimage
 * @param payment_preimage hex string
 * @param status
 * @param parts
 * @param error
 * @param label attached to the payment
 */
case class Payment(
    amount_msat: MilliSatoshi,
    amount_sent_msat: MilliSatoshi,
    created_at: Instant,
    destination: String,
    msatoshi: MilliSatoshi,
    msatoshi_sent: MilliSatoshi,
    payment_hash: String,
    payment_preimage: String,
    status: PayStatus,
    parts: Option[Int] = None,
    error: Option[String] = None,
    label: Option[String] = None
)

object Payment extends EpochSecondInstantFormatter {
  implicit val formatPayment: OFormat[Payment] = Json.format[Payment]
  lazy val sha256 = Hashing.sha256()
}
