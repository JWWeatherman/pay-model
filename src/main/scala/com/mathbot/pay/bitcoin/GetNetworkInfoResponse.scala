package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

case class GetNetworkInfoResponse(result: NetworkInfo, id: String) extends RpcResponse[NetworkInfo]

object GetNetworkInfoResponse {
  implicit val formatGetNetworkInfoResponse = Json.format[GetNetworkInfoResponse]
}
