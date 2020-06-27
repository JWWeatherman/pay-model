package com.mathbot.pay.bitcoin

import play.api.libs.json.{Format, Json}

object TransactionCategory extends Enumeration {
  type TransactionCategory = Value
  val send, receive = Value
  implicit val formatTransactionCategory: Format[Value] = Json.formatEnum(this)
}
