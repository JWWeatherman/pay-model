package payModel.models.lightning

import payModel.models.lightning.PayStatus.PayStatus
import play.api.libs.json.{Json, OFormat}

case class ListPay(bolt11: Bolt11, status: PayStatus, amount_sent_msat: String)

object ListPay {
  lazy implicit val formatListPay: OFormat[ListPay] = Json.format[ListPay]
}
