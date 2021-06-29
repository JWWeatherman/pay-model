package com.mathbot.pay.lightning

import com.mathbot.pay.bitcoin.MilliSatoshi
import play.api.libs.json.Json

case class PaidOffer(
    label: String,
    bolt12: String,
    payment_hash: String,
    msatoshi: MilliSatoshi,
    amount_msat: String,
    status: String,
    pay_index: Int,
    msatoshi_received: Int,
    amount_received_msat: String,
    paid_at: Int,
    payment_preimage: String,
    description: String,
    expires_at: Int,
    local_offer_id: String
)
object PaidOffer {
  implicit val formatPaidOffer = Json.format[PaidOffer]
}
