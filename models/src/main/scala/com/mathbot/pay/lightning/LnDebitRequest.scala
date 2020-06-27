package com.mathbot.pay.lightning

import com.mathbot.pay.bitcoin.CallbackURL
import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.json.{Json, OFormat}

case class LnDebitRequest(bolt11: Bolt11, callbackURL: CallbackURL) extends LightningJson

object LnDebitRequest extends PlayJsonSupport {
  implicit val formatLnDebitReq: OFormat[LnDebitRequest] = Json.format[LnDebitRequest]
}
