package com.mathbot.pay.lightning

import play.api.libs.json.Json

case class LightningListOffersRequest(offer_id: Option[String], only_active: Option[Boolean]) extends LightningJson

object LightningListOffersRequest {
  implicit val formatLightningListOffersRequest = Json.format[LightningListOffersRequest]
}
