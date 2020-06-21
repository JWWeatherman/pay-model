package com.mathbot.pay.bitcoin

import play.api.libs.json.{Json, OFormat}

case class SendToAddressResponse(result: TxId, id: String) extends RpcResponse[TxId]
object SendToAddressResponse {
  implicit val formatSendToAddressResponse: OFormat[SendToAddressResponse] = Json.format[SendToAddressResponse]
}
