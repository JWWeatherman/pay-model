package com.mathbot.pay.lightning.url

import com.google.common.hash.Hashing
import fr.acinq.eclair.MilliSatoshi
import kotlin.text.Charsets
import play.api.libs.json.Json

/**
 * https://xn--57h.bigsun.xyz/lnurl-pay-flow.txt
 * @param callback client respond to this url
 * @param minSendable
 * @param maxSendable
 * @param tag use the default
 * @param metadata
 * @param commentAllowed number of characters someone can attach the the payment request
 */
case class LightningUrlPay(
    callback: String,
    minSendable: MilliSatoshi,
    maxSendable: MilliSatoshi,
    metadata: String,
    descriptionHash: String,
    tag: String = "payRequest",
    commentAllowed: Option[Int] = None
) {
  require(minSendable <= maxSendable, s"Invalid min and max $this")
}

object LightningUrlPay {
  implicit val formatLightingUrlPay = Json.format[LightningUrlPay]

  def makeMetadata(description: String) = s"""[["text/plain","$description"]]"""
  def makeMetadataWithImg(description: String, img: String) =
    s"""[["text/plain","$description"],["image/png;base64","$img"]]"""
  def apply(
      callback: String,
      max: MilliSatoshi,
      min: MilliSatoshi,
      description: String,
      base64Img: Option[String]
  ): LightningUrlPay = {
    val metadata = base64Img match {
      case Some(value) => makeMetadataWithImg(description, value)
      case None => makeMetadata(description)
    }
    val descriptionHash = Hashing
      .sha256()
      .hashString(metadata, Charsets.UTF_8)
    LightningUrlPay(
      callback = callback,
      minSendable = min,
      maxSendable = max,
      metadata = metadata,
      descriptionHash = descriptionHash.toString
    )
  }
}
