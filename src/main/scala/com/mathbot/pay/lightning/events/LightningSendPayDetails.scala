package com.mathbot.pay.lightning.events

import com.mathbot.pay.lightning.PayStatus.PayStatus
import fr.acinq.eclair.MilliSatoshi
import play.api.libs.json.Json
// https://lightning.readthedocs.io/PLUGINS.html#event-notifications

object LightningSendPayDetails {
  implicit val formatSendPaySuccess = Json.format[LightningSendPayDetails]
}
//  The json is the same as the return value of the commands sendpay/waitsendpay
case class LightningSendPayDetails(
    id: Int,
    payment_hash: String,
    destination: String,
    amount_msat: Option[MilliSatoshi],
    amount_sent_msat: Option[MilliSatoshi],
    created_at: Long,
    status: PayStatus
)
