package com.mathbot.pay.lightning

import play.api.libs.json.Json

case class LightningListOffersResponse(jsonrpc: String, id: Int, result: LightningOffers) extends LightningJson

object LightningListOffersResponse {
  implicit val formatLightingListOffersResponse = Json.format[LightningListOffersResponse]
}
