package com.mathbot.pay.lightning

import play.api.libs.json.Json

case class LightningOffers(offers: Seq[LightningOffer])

object LightningOffers {
  implicit val formatLightningOffers = Json.format[LightningOffers]
}
