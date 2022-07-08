package com.mathbot.pay.lightning

import com.mathbot.pay.json.EpochSecondInstantFormatter
import play.api.libs.json.Json

import java.time.Instant

/**
 * Response from creating invoice
 * @param bolt11 the bolt11 string
 * @param payment_hash the hash of the *payment_preimage* which will prove payment
 * @param payment_secret the *payment_secret* to place in the onion
 * @param expires_at UNIX timestamp of when invoice expires
 * @param warning_capacity even using all possible channels, there's not enough incoming capacity to pay this invoice.
 * @param warning_offline there would be enough incoming capacity, but some channels are offline, so there isn't.
 * @param warning_deadends there would be enough incoming capacity, but some channels are dead-ends (no other public channels from those peers), so there isn't.
 * @param warning_private_unused there would be enough incoming capacity, but some channels are unannounced and *exposeprivatechannels* is *false*, so there isn't.
 * @param warning_mpp there is sufficient capacity, but not in a single channel, so the payer will have to use multi-part payments.
 */
case class LightningCreateInvoice(
    bolt11: Bolt11,
    payment_hash: String,
    expires_at: Instant,
    payment_secret: Option[String],
    warning_capacity: Option[String],
    warning_offline: Option[String],
    warning_deadends: Option[String],
    warning_private_unused: Option[String],
    warning_mpp: Option[String]
)

object LightningCreateInvoice extends EpochSecondInstantFormatter {
  implicit val formatLightningCreateInvoice = Json.format[LightningCreateInvoice]
}
