package com.mathbot.pay.lightning

import com.mathbot.pay.lightning.PayStatus.PayStatus
import play.api.libs.json.{Json, OFormat}

case class ListPay(bolt11: Bolt11, status: PayStatus, amount_sent_msat: String)

object ListPay {
  lazy implicit val formatListPay: OFormat[ListPay] = Json.format[ListPay]
  def apply(bolt11: Bolt11, payment: Payment): ListPay =
    ListPay(bolt11 = bolt11, status = payment.status, amount_sent_msat = payment.amount_sent_msat)
}
