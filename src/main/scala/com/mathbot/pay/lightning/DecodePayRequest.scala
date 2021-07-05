package com.mathbot.pay.lightning

import play.api.libs.json.Json

case class DecodePayRequest(bolt11: Bolt11) extends LightningJson

object DecodePayRequest {
  implicit val formatDecodePayRequest = Json.format[DecodePayRequest]
}
