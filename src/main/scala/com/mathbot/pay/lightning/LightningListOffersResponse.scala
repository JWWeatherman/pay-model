package com.mathbot.pay.lightning

import play.api.libs.json.Json

case class LightningListOffersResponse(jsonrpc: String, id: Int, result: Seq[LightningOffer]) extends LightningJson

object LightningListOffersResponse {
  implicit val formatLightingListOffersResponse = Json.format[LightningListOffersResponse]
}
