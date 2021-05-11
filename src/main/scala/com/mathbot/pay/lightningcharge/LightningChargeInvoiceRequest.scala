package com.mathbot.pay.lightningcharge

import com.mathbot.pay.bitcoin.MilliSatoshi
import com.mathbot.pay.json.FiniteDurationToSecondsWriter
import play.api.libs.json.{JsValue, Json, OFormat}

import scala.concurrent.duration.FiniteDuration

/**
 * @param webhook to notify
 * @param msatoshi to charge
 * @param expiry in seconds
 * @param description of the charge
 */
case class LightningChargeInvoiceRequest(
    webhook: Option[String],
    msatoshi: MilliSatoshi,
    expiry: FiniteDuration,
    description: String,
    metadata: Option[JsValue]
)

object LightningChargeInvoiceRequest extends FiniteDurationToSecondsWriter {

  implicit val writesLightningChargeInvoiceRequset =
    Json.writes[LightningChargeInvoiceRequest]

}
