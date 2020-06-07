package payModel.models.lightning

import payModel.models.PlayJsonSupport
import payModel.models.bitcoin.CallbackURL
import play.api.libs.json.{Json, OFormat}

case class LnDebitRequest(bolt11: Bolt11, callbackURL: CallbackURL) extends LightningJson

object LnDebitRequest extends PlayJsonSupport {
  implicit val formatLnDebitReq: OFormat[LnDebitRequest] = Json.format[LnDebitRequest]
}
