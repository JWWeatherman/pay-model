package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class GetInfoResponse(result: LightningNodeInfo)

object GetInfoResponse {
  lazy implicit val formatGetInfoResponse: OFormat[GetInfoResponse] = Json.format[GetInfoResponse]
}
