package com.mathbot.pay.btcpayserver

import play.api.libs.json._

object CreditStatus extends Enumeration {
  type CreditStatus = Value

  val `new`, paid, confirmed, complete, expired, failed, underpaid, processing, invalid = Value

  implicit val formatCreditStats: Format[CreditStatus] = new Format[CreditStatus] {
    override def reads(json: JsValue): JsResult[CreditStatus] = json match {
      case JsString(v) => JsSuccess(CreditStatus.withName(v))
      case _ => JsError("Invalid credit status")
    }
    override def writes(o: CreditStatus): JsValue = JsString(o.toString)
  }
}
