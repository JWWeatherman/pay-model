package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class LightningPaymentRoute(
    pubkey: String,
    short_channel_id: String,
    fee_base_msat: Long,
    fee_proportional_millionths: Long,
    cltv_expiry_delta: Long
)

object LightningPaymentRoute {
  lazy implicit val formatRoute: OFormat[LightningPaymentRoute] = Json.format[LightningPaymentRoute]

}
