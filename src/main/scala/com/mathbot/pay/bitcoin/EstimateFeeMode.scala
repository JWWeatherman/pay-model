package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

object EstimateFeeMode extends Enumeration {
  type EstimateFeeMode = Value
  val UNSET, ECONIMICAL, CONSERVATIVE = Value
  implicit val formatEs = Json.formatEnum(this)
}
