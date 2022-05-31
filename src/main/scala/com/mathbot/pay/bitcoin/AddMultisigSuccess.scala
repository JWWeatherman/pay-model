package com.mathbot.pay.bitcoin

import play.api.libs.json.{Json, OFormat}

object AddMultisigSuccess {
  implicit val formatAddMultisigSuccess: OFormat[AddMultisigSuccess] = Json.format[AddMultisigSuccess]
}
case class AddMultisigSuccess(address: BtcAddress, redeemScript: String)
