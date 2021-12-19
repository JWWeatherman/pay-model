package com.mathbot.pay.lightning.url

import com.mathbot.pay.lightning.Bolt11
import play.api.libs.json.Json

import java.time.Instant

object CreateInvoiceWithDescriptionHash {
  implicit val formatCreateInvoiceWithDescriptionHash = Json.format[CreateInvoiceWithDescriptionHash]
}

case class CreateInvoiceWithDescriptionHash(
    bolt11: Bolt11,
    description_hash: String,
    expires_at: Instant,
    payment_hash: String,
    preimage: String
) {
  override def toString: String = Json.toJson(this).toString()
}
