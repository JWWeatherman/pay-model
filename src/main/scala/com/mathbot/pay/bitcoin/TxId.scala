package com.mathbot.pay.bitcoin

import play.api.libs.json._

import scala.util.Try

case class TxId(txId: String) {
  require(TxId.validateTxId(txId), s"Invalid txId = $txId")
  override def toString: String = txId
}

object TxId {

  def validateTxId(id: String): Boolean = id match {
    case valid if id.length == 64 && Try(BigInt(valid, 16)).isSuccess => true
    case _ => false
  }

  implicit val formatTxId: Format[TxId] = new Format[TxId] {
    override def writes(o: TxId): JsValue = JsString(o.txId)

    override def reads(json: JsValue): JsResult[TxId] = json match {
      case JsString(t) => JsSuccess(TxId(t))
      case _ => JsError()
    }

  }

}
