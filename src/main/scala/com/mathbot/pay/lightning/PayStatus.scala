package com.mathbot.pay.lightning

import play.api.libs.json.Json

object PayStatus extends Enumeration {
  type PayStatus = Value
  val complete, failed, pending, paid // offers can have paid status
  = Value
  implicit val formatPayStatus = Json.formatEnum(this)
}
