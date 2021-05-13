package com.mathbot.pay.lightning

import com.mathbot.pay.bitcoin.MilliSatoshi
import com.mathbot.pay.json.FiniteDurationToSecondsWriter
import play.api.libs.json._

import scala.concurrent.duration.{Duration, FiniteDuration}

object Pay extends FiniteDurationToSecondsWriter {
  implicit val readsRetryFo: Reads[FiniteDuration] = {
    case JsString(value) =>
      JsSuccess(Duration(value).asInstanceOf[FiniteDuration])
    case _ => JsError("Not a FiniteDuration")
  }
  implicit val formatPay = Json.format[Pay]

}

/**
 * https://lightning.readthedocs.io/lightning-pay.7.html
 * @param bolt11
 * @param msatoshi  If the bolt11 does not contain an amount, msatoshi is required, otherwise if it is specified it must be null.
 * @param label The label field is used to attach a label to payments, and is returned in lightning-listpays(7)
 * @param riskfactor
 * @param maxfeepercent  limits the money paid in fees, and defaults to 0.5.
 * @param retry_for
 * @param maxdelay
 * @param exemptfee The [[exemptfee]] option can be used for tiny payments which would be dominated by the fee leveraged by forwarding nodes.
 *                  setting [[exemptfee]] allows the [[maxfeepercent]] check to be skipped on fees that are smaller than [[exemptfee]]
 */
case class Pay(
    bolt11: Bolt11,
    msatoshi: Option[MilliSatoshi] = None,
    label: Option[String] = None,
    riskfactor: Option[String] = None,
    maxfeepercent: Option[String] = None,
    retry_for: Option[FiniteDuration] = None,
    maxdelay: Option[Int] = None,
    exemptfee: Option[String] = None
) extends LightningJson
