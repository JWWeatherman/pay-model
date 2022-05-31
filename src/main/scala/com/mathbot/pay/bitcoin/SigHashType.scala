package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

object SigHashType extends Enumeration {
  type SigHashType = Value
  val ALL, NONE, SINGLE, `ALL|ANYONECANPAY`, `NONE|ANYONECANPAY`, `SINGLE|ANYONECANPAY` = Value
  implicit val formatSig = Json.formatEnum(this)
}
