package payModel.models.bitcoin

import payModel.models.{PlayJsonSupport, Satoshi}
import play.api.libs.json.{Json, OFormat}

case class BtcDebitRequest(btcAddress: BtcAddress, amount: Satoshi, callbackURL: CallbackURL, id: String)

object BtcDebitRequest extends PlayJsonSupport {
  implicit val formatBtcDebitRequest: OFormat[BtcDebitRequest] = Json.format[BtcDebitRequest]
}
