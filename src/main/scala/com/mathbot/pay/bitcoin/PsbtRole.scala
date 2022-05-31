package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

object PsbtRole extends Enumeration {
  type PsbtRole = Value
  val creator, updater, signer, finalizer = Value
  implicit val formatPsbtRole = Json.formatEnum(this)
}
