package com.mathbot.pay.lightning.events

import fr.acinq.eclair.MilliSatoshi
import play.api.libs.json.Json
// https://lightning.readthedocs.io/PLUGINS.html#event-notifications

case class LightningEventDetails(
    label: String,
    preimage: Option[String],
    amount_msat: Option[MilliSatoshi],
    msat: Option[MilliSatoshi]
)
object LightningEventDetails {
  implicit lazy val readsInv = Json.format[LightningEventDetails]
}
