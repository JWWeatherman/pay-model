package com.mathbot.pay.lightning.lightningcharge

import com.mathbot.pay.json.FiniteDurationToSecondsWriter
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.duration.FiniteDuration

case class LightningChargeInvoiceRequestByCurrency(
    webhook: Option[String],
    amount: BigDecimal,
    currency: String,
    expiry: FiniteDuration,
    description: String,
    metadata: Option[JsValue]
)

object LightningChargeInvoiceRequestByCurrency extends FiniteDurationToSecondsWriter {

  implicit val formatLightningChargeInvoiceRequestByCurrency = Json.writes[LightningChargeInvoiceRequestByCurrency]
}
