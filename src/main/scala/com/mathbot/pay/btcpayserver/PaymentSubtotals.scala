package com.mathbot.pay.btcpayserver

import com.mathbot.pay.bitcoin._
import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

case class PaymentSubtotals(
    BTC: Satoshi,
    BTC_LightningLike: Satoshi
)

object PaymentSubtotals extends PlayJsonSupport {

  implicit val readsPaymentSubtotals: Reads[PaymentSubtotals] = (
    (__ \ 'BTC).read[Long](min(0L)).map(Satoshi) and
    (__ \ 'BTC_LightningLike).read[Long](min(0L)).map(Satoshi)
  )(PaymentSubtotals.apply _)

  implicit val writesPaymentSubtotals: OWrites[PaymentSubtotals] = Json.writes[PaymentSubtotals]
}
