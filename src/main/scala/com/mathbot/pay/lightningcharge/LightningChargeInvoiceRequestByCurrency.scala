package com.mathbot.pay.lightningcharge

import play.api.libs.json.{JsValue, Json}

case class LightningChargeInvoiceRequestByCurrency(
    webhook: Option[String],
    amount: BigDecimal,
    currency: String,
    expiry: Long,
    description: String,
    metadata: Option[JsValue]
)

object LightningChargeInvoiceRequestByCurrency {
  implicit val formatLightningChargeInvoiceRequestByCurrency = Json.format[LightningChargeInvoiceRequestByCurrency]
}
