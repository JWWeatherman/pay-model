package com.mathbot.pay.bitcoin

import play.api.libs.json.{Json, OFormat}

object Input {
  implicit val formatInput: OFormat[Input] = Json.format[Input]
}
case class Input(txid: TxId, vout: Int)
