package com.mathbot.pay.lightning

import com.mathbot.pay.bitcoin.MilliSatoshi
import play.api.libs.json.Json

object Pay {
  implicit val formatPay = Json.format[Pay]
}

/**
 * https://lightning.readthedocs.io/lightning-pay.7.html
 * @param bolt11
 * @param msatoshi
 * @param label
 * @param riskfactor
 * @param maxfeepercent
 * @param retry_for
 * @param maxdelay
 * @param exemptfee
 */
case class Pay(bolt11: Bolt11,
               msatoshi: Option[MilliSatoshi],
               label: Option[String],
               riskfactor: Option[String],
               maxfeepercent: Option[String],
               retry_for: Option[String],
               maxdelay: Option[String],
               exemptfee: Option[String],
) extends LightningJson
