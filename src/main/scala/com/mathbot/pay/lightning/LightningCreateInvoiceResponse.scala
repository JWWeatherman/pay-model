package com.mathbot.pay.lightning

import play.api.libs.json.Json

case class LightningCreateInvoiceResponse(jsonrpc: String, id: Int, result: LightningCreateInvoice)
    extends LightningJson

object LightningCreateInvoiceResponse {
  implicit val formatLightingListOffersResponse = Json.format[LightningCreateInvoiceResponse]
}
