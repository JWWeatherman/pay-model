package com.mathbot.pay.lightning

import play.api.libs.json.Json

case class ListInvoicesRequest(
    label: Option[String] = None,
    invstring: Option[String] = None,
    payment_hash: Option[String] = None
) extends LightningJson

object ListInvoicesRequest {
  implicit val formatListInvoicesRequest = Json.format[ListInvoicesRequest]
}
