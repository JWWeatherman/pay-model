package com.mathbot.pay.lightningcharge

import com.mathbot.pay.bitcoin.MilliSatoshi
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
    expiry: Long,
    description: String,
    metadata: Option[JsValue]
)

object LightningChargeInvoiceRequest {

  implicit val formatInvoiceRequest: OFormat[LightningChargeInvoiceRequest] =
    Json.format[LightningChargeInvoiceRequest]

  def apply(
      webhook: Option[String],
      msatoshi: MilliSatoshi,
      expiry: FiniteDuration,
      description: String,
      metadata: Option[JsValue]
  ): LightningChargeInvoiceRequest =
    LightningChargeInvoiceRequest(
      webhook = webhook,
      msatoshi = msatoshi,
      expiry = expiry.toSeconds,
      description = description,
      metadata = metadata
    )

}
