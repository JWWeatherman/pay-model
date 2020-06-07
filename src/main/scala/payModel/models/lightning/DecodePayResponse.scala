package payModel.models.lightning

import play.api.libs.json.{Json, OFormat}

case class DecodePayResponse(id: Long, jsonrpc: String, result: DecodePay) extends Response[DecodePay]

object DecodePayResponse {
  lazy implicit val decodePayResponse: OFormat[DecodePayResponse] = Json.format[DecodePayResponse]

}
