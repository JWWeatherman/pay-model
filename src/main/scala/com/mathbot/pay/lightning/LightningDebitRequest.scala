package com.mathbot.pay.lightning

import com.mathbot.pay.json.PlayJsonSupport
import com.mathbot.pay.webhook.CallbackURL
import com.mathbot.pay.ws.WsLightningDebitRequest
import play.api.libs.json.{Json, OFormat}

case class LightningDebitRequest(pay: Pay, callbackURL: Option[CallbackURL]) extends LightningJson

object LightningDebitRequest extends PlayJsonSupport {
  implicit val formatLnDebitReq: OFormat[LightningDebitRequest] = Json.format[LightningDebitRequest]
  def apply(req: WsLightningDebitRequest): LightningDebitRequest =
    LightningDebitRequest(pay = req.pay, callbackURL = req.callbackURL)
}
