package payModel.models.bitcoin

import play.api.libs.json.{Json, OFormat}

case class GetNewAddressResponse(result: BtcAddress, id: String) extends RpcResponse[BtcAddress]
object GetNewAddressResponse {
  implicit val formatGetNewAddressResponse: OFormat[GetNewAddressResponse] =
    Json.format[GetNewAddressResponse]
}
