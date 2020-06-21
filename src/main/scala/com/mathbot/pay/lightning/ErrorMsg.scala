package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

// {"jsonrpc":"2.0","id":0,"error":{"code":-32602,"message":"Invalid bolt11: Bad bech32 string"}}
case class ErrorMsg(code: Long, message: String)

object ErrorMsg {
  lazy implicit val formatErrorMsg: OFormat[ErrorMsg] = Json.format[ErrorMsg]
}
