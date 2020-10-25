package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class LightningGetInfoRequest(method: String = "getinfo") extends LightningJson

object LightningGetInfoRequest {
  implicit val formatLnGetInfoRequest: OFormat[LightningGetInfoRequest] = Json.format[LightningGetInfoRequest]
}
