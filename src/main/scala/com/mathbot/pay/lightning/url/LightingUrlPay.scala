package com.mathbot.pay.lightning.url

import com.google.common.hash.Hashing
import com.mathbot.pay.bitcoin.MilliSatoshi
import kotlin.text.Charsets
import play.api.libs.json.Json

/**
 * https://xn--57h.bigsun.xyz/lnurl-pay-flow.txt
 * @param callback url
 * @param minSendable
 * @param maxSendable
 * @param tag use the default
 * @param metadata
 */
case class LightingUrlPay(
    callback: String,
    minSendable: MilliSatoshi,
    maxSendable: MilliSatoshi,
    tag: String = "payRequest",
    metadata: String,
    descriptionHash: String,
) {
  require(minSendable <= maxSendable, s"Invalid min and max $this")
}

object LightingUrlPay {
  implicit val formatLightingUrlPay = Json.format[LightingUrlPay]

  def makeMetadata(description: String) = s"""[["text/plain","$description"]]"""
  def makeMetadataWithImg(description: String, img: String) =
    s"""[["text/plain","$description"],["image/png;base64","$img"]]"""
  def apply(callback: String,
            max: MilliSatoshi,
            min: MilliSatoshi,
            description: String,
            base64Img: Option[String]): LightingUrlPay = {
    val metadata = base64Img match {
      case Some(value) => makeMetadataWithImg(description, value)
      case None => makeMetadata(description)
    }
    val descriptionHash = Hashing
      .sha256()
      .hashString(metadata, Charsets.UTF_8)
    LightingUrlPay(
      callback = callback,
      minSendable = min,
      maxSendable = max,
      metadata = metadata,
      descriptionHash = descriptionHash.toString
    )
  }
}
