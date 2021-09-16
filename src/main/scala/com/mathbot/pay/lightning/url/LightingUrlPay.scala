package com.mathbot.pay.lightning.url

import com.mathbot.pay.bitcoin.MilliSatoshi
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
    metadata: String = LightingUrlPay.makeMetadata("testing")
) {
  require(minSendable <= maxSendable, s"Invalid min and max $this")
}

object LightingUrlPay {
  implicit val formatLightingUrlPay = Json.format[LightingUrlPay]

  def makeMetadata(description: String) = s"""[["text/plain","$description"]]"""
  def apply(callback: String, max: MilliSatoshi, min: MilliSatoshi, descrption: String): LightingUrlPay =
    LightingUrlPay(
      callback = callback,
      minSendable = min,
      maxSendable = max,
      metadata = makeMetadata(descrption)
    )
}
