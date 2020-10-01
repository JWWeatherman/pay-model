package com.mathbot.pay.ws

import akka.actor.ActorPath
import com.github.dwickern.macros.NameOf.nameOf
import com.mathbot.pay.bitcoin.TxId
import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.json._

case class WsWalletTransactionInfoRequest(txId: TxId, onBehalfOf: ActorPath) extends WebsocketMessage

object WsWalletTransactionInfoRequest extends PlayJsonSupport {

  def isJsonForThis(js: JsValue): Boolean = (js \ nameOf(WsWalletTransactionInfoRequest)).isDefined

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
