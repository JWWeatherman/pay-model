package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class GetInfoResponse(id: Long, jsonrpc: String, result: InfoResponse) extends Response[InfoResponse] with LightningJson

object GetInfoResponse {
  lazy implicit val formatGetInfoResponse: OFormat[GetInfoResponse] = Json.format[GetInfoResponse]
}

