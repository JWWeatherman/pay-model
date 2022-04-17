package com.mathbot.pay.lightning.url

import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import fr.acinq.eclair.MilliSatoshi
import com.mathbot.pay.json.FiniteDurationToSecondsFormatter
import com.mathbot.pay.lightning.LightningJson
import play.api.libs.json.Json

import java.util.Base64
import scala.concurrent.duration.FiniteDuration

object InvoiceWithDescriptionHash extends FiniteDurationToSecondsFormatter {
  implicit val formatInvoiceWithDescriptionHash = Json.format[InvoiceWithDescriptionHash]

  def apply(
      description: String,
      milliSatoshi: MilliSatoshi,
      label: String,
      expiry: FiniteDuration,
      img: Option[String], // todo: base64 validate
      preimage: Option[String]
  ): InvoiceWithDescriptionHash = {
    // validate image string is base64 encoded
    img.foreach(Base64.getDecoder.decode)
    val metadata =
      img
        .map(i => s"""[["text/plain","$description"],["image/png;base64","$i"]]""")
        .getOrElse(
          s"""[["text/plain","$description"]]"""
        )
    val descriptionHash = Hashing
      .sha256()
      .hashString(metadata, Charsets.UTF_8)
      .toString
    InvoiceWithDescriptionHash(
      msatoshi = milliSatoshi,
      label = label,
      description_hash = descriptionHash,
      expiry = expiry,
      preimage = preimage
    )
  }
}
case class InvoiceWithDescriptionHash(
    msatoshi: MilliSatoshi,
    label: String,
    description_hash: String,
    expiry: FiniteDuration,
    preimage: Option[String] = None
) extends LightningJson
