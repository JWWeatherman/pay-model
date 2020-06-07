package payModel.models.bitcoin

import payModel.models.{Btc, PlayJsonSupport}
import play.api.libs.json.{Json, OFormat}

case class GetBalanceResponse(result: Btc, id: String) extends RpcResponse[Btc]
object GetBalanceResponse extends PlayJsonSupport {
  implicit val formatGetBalanceResponse: OFormat[GetBalanceResponse] = Json.format[GetBalanceResponse]
}
