package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class DecodePay(
    currency: String,
    created_at: Long,
    expiry: Long,
    payee: String,
    msatoshi: Option[Long],
    amount_msat: Option[String],
    description: String,
    min_final_cltv_expiry: Long,
    routes: Option[Seq[Seq[Route]]],
    fallbacks: Option[Seq[Fallback]],
    payment_hash: String,
    signature: String
)

object DecodePay {

  lazy implicit val formatDecodePay: OFormat[DecodePay] = Json.format[DecodePay]

}
