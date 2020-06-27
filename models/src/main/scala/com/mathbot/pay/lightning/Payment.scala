package com.mathbot.pay.lightning

import java.time.Instant

import com.mathbot.pay.bitcoin.MilliSatoshi
import com.mathbot.pay.json.{EpochSecondInstantFormatter, PlayJsonSupport}
import com.mathbot.pay.lightning.PayStatus.PayStatus
import play.api.libs.json._

case class Payment(id: Long,
                   amount_msat: String,
                   amount_sent_msat: String,
                   bolt11: Bolt11,
                   created_at: Instant,
                   destination: String,
                   msatoshi: MilliSatoshi,
                   msatoshi_sent: MilliSatoshi,
                   payment_hash: String,
                   payment_preimage: String,
                   status: PayStatus,
                   error: Option[String])

object Payment extends PlayJsonSupport with EpochSecondInstantFormatter {
  implicit val formatPayment: OFormat[Payment] = Json.format[Payment]
}
