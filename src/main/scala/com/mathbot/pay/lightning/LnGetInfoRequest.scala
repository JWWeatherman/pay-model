package com.mathbot.pay.lightning

import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.json.{Json, OFormat}

case class LnGetInfoRequest(method: String) extends LightningJson

object LnGetInfoRequest {
  implicit val formatLnGetInfoRequest: OFormat[LnGetInfoRequest] = Json.format[LnGetInfoRequest]
}