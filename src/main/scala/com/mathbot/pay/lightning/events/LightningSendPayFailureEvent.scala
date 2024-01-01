package com.mathbot.pay.lightning.events

import play.api.libs.json.Json

object LightningSendPayFailureEvent {
  implicit lazy val readsSendPaySuccess = Json.format[LightningSendPayFailureEvent]
}

case class LightningSendPayFailureEvent(sendpay_failure: LightningEventDetails)
