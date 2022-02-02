package com.mathbot.pay.lightning.url

import com.mathbot.pay.bitcoin.MilliSatoshi
import com.mathbot.pay.lightning.LightningJson
import play.api.libs.json.Json

object InvoiceWithDescriptionHash {
  implicit val formatInvoiceWithDescriptionHash = Json.format[InvoiceWithDescriptionHash]
}
case class InvoiceWithDescriptionHash(
    msatoshi: MilliSatoshi,
    label: String,
    description_hash: String,
    expiry: Int,
    preimage: Option[String] = None
) extends LightningJson
