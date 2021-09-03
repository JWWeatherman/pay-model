package com.mathbot.pay.lightning

import com.mathbot.pay.json.EpochSecondInstantFormatter
import play.api.libs.json.Json

import java.time.Instant

/**
 * Response from invoice [[LightningCreateInvoice]]
 * @param bolt11
 * @param payment_hash
 * @param expires_at
 */
case class LightningCreateInvoice(bolt11: Bolt11, payment_hash: String, expires_at: Instant)

object LightningCreateInvoice extends EpochSecondInstantFormatter {
  implicit val formatLightningCreateInvoice = Json.format[LightningCreateInvoice]
}
