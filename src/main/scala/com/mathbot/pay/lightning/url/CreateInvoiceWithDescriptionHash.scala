package com.mathbot.pay.lightning.url

import com.mathbot.pay.json.{FiniteDurationToSecondsReader, FiniteDurationToSecondsWriter}
import com.mathbot.pay.lightning.Bolt11
import play.api.libs.json.Json

import java.time.Instant

object CreateInvoiceWithDescriptionHash extends FiniteDurationToSecondsReader with FiniteDurationToSecondsWriter {
  implicit val formatCreateInvoiceWithDescriptionHash = Json.format[CreateInvoiceWithDescriptionHash]
}

case class CreateInvoiceWithDescriptionHash(
    bolt11: Bolt11,
    description_hash: String,
    expires_at: Instant,
    payment_hash: String,
    preimage: String
)
