package com.mathbot.pay.lightning

import play.api.libs.json.Json

case class LightningOffer(offer_id: String, active: Boolean, single_use: Boolean, bolt12: String, used: Boolean)

object LightningOffer {
  implicit val formatLightningOffer = Json.format[LightningOffer]
}
