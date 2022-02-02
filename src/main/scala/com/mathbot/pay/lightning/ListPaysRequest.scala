package com.mathbot.pay.lightning

import com.mathbot.pay.lightning.PayStatus.PayStatus
import play.api.libs.json.{Json, OFormat}

case class ListPaysRequest(
    bolt11: Option[Bolt11] = None,
    payment_hash: Option[String] = None,
    status: Option[PayStatus] = None
) extends LightningJson

object ListPaysRequest {
  implicit val formatListPaysRequest: OFormat[ListPaysRequest] = Json.format[ListPaysRequest]

  def apply(): ListPaysRequest = ListPaysRequest(None, None)
  def apply(bolt11: Bolt11): ListPaysRequest = ListPaysRequest(Some(bolt11), None)
  def apply(payment_hash: String): ListPaysRequest = ListPaysRequest(None, Some(payment_hash))
}
