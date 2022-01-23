package com.mathbot.pay.lightning

import com.mathbot.pay.bitcoin.MilliSatoshi

import java.time.Instant
import com.mathbot.pay.json.EpochSecondInstantFormatter
import com.mathbot.pay.lightning.PayStatus.{complete, paid, pending, PayStatus}
import play.api.libs.json.{Json, OFormat}
// https://github.com/ElementsProject/lightning/blob/master/doc/lightning-listpays.7.md
// For old payments (pre-0.7) we didn’t save the bolt11 string, so in its place are three other fields: payment_hash,destination,amount_msat
case class ListPay(
    bolt11: Option[Bolt11],
    status: PayStatus,
    amount_msat: Option[MilliSatoshi],
    amount_sent_msat: MilliSatoshi,
    created_at: Instant,
    preimage: Option[String] = None,
    payment_hash: Option[String] = None,
    label: Option[String] = None
) {
  val isPending = status == pending
  val isPaid = status == complete || status == paid
}

object ListPay extends EpochSecondInstantFormatter {
  lazy implicit val formatListPay: OFormat[ListPay] = Json.format[ListPay]
}
