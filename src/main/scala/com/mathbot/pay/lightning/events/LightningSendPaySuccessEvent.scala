package com.mathbot.pay.lightning.events

import play.api.libs.json.Json

object LightningSendPaySuccessEvent {
  implicit lazy val readsSendPaySuccess = Json.format[LightningSendPaySuccessEvent]
}

case class LightningSendPaySuccessEvent(sendpay_success: LightningEventDetails)
