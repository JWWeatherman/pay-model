package com.mathbot.pay.btcpayserver

import play.api.libs.json._

object CreditStatus extends Enumeration {
  type CreditStatus = Value

  val `new`, paid, confirmed, complete, expired, failed, underpaid, processing, invalid = Value

  implicit val formatCreditStatus = Json.formatEnum(this)
}
