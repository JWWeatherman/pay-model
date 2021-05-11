package com.mathbot.pay.lightning

import com.mathbot.pay.bitcoin.MilliSatoshi

import scala.concurrent.duration.{Duration, FiniteDuration}

/**
 * https://github.com/ElementsProject/lightning/blob/master/doc/lightning-invoice.7.md
 * Create an invoice for {msatoshi} with {label} and {description} with
 * optional {expiry} seconds (default 1 week).
 *
 * @param msatoshi
 * @param label
 * @param description
 * @param expiry
 */
case class LightningInvoice(
    msatoshi: MilliSatoshi,
    label: String,
    description: String,
    expiry: Option[FiniteDuration] = Some(Duration("7 days").asInstanceOf[FiniteDuration])
)
//                          fallbacks: Option[String] = None,
//                          preimage: Option[String] = None,
//                          exposeprivatechannels: Option[String] = None)
    extends LightningJson
