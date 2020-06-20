package payModel.models.ws

import play.api.libs.json.{
  Format,
  JsDefined,
  JsError,
  JsLookupResult,
  JsNull,
  JsObject,
  JsResult,
  JsUndefined,
  JsValue,
  Json,
  OFormat
}

import payModel.models.bitcoin._

import com.github.dwickern.macros._

case class WsWalletTransactionInfoRequest(txId: TxId)

object WsWalletTransactionInfoRequest {
  implicit val formatter: Format[WsWalletTransactionInfoRequest] = new Format[WsWalletTransactionInfoRequest] {

    private implicit val internal: OFormat[WsWalletTransactionInfoRequest] = Json.format[WsWalletTransactionInfoRequest]

    override def writes(o: WsWalletTransactionInfoRequest): JsValue =
      Json
        .toJson[WsWalletTransactionInfoRequest](o)
        .as[JsObject] + (nameOf(WsWalletTransactionInfoRequest) -> JsNull)

    override def reads(json: JsValue): JsResult[WsWalletTransactionInfoRequest] = {
      // Just checks to see if the property is in the json object to tag the json, does not care about the value
      json \ nameOf(WsWalletTransactionInfoRequest) match {
        case JsDefined(_) => json.validate[WsWalletTransactionInfoRequest]
        case undefined: JsUndefined => JsError(s"Not a ${nameOf(WsWalletTransactionInfoRequest)}")
      }
    }
  }
}
