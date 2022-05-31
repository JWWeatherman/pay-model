package com.mathbot.pay.bitcoin

import com.mathbot.pay.json.PlayJsonSupport
import com.mathbot.pay.webhook.CallbackURL
import fr.acinq.bitcoin.Satoshi
import play.api.libs.json.{Json, OFormat}

case class BtcDebitRequest(btcAddress: BtcAddress, amount: Satoshi, callbackURL: CallbackURL, id: String)

object BtcDebitRequest extends PlayJsonSupport {
  implicit val formatBtcDebitRequest: OFormat[BtcDebitRequest] = Json.format[BtcDebitRequest]
}
