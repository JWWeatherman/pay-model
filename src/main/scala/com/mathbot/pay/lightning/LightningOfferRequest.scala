package com.mathbot.pay.lightning

import com.mathbot.pay.bitcoin.MilliSatoshi
import play.api.libs.json.Json

/**
 * Create an offer for invoices of {amount} with {description}, optional {vendor}, internal {label}, {quantity_min}, {quantity_max}, {absolute_expiry}, {recurrence}, {recurrence_base}, {recurrence_paywindow}, {recurrence_limit} and {single_use}
 * offer amount description [vendor] [label] [quantity_min] [quantity_max] [absolute_expiry] [recurrence] [recurrence_base] [recurrence_paywindow] [recurrence_limit] [single_use]
 */
case class LightningOfferRequest(
    amount: String,
    description: String,
    label: Option[String],
    vendor: Option[String],
    quantity_min: Option[MilliSatoshi],
    quantity_max: Option[MilliSatoshi],
    single_use: Option[Boolean]
) extends LightningJson

object LightningOfferRequest {
  implicit val formatLightningOfferRequest = Json.format[LightningOfferRequest]
}
