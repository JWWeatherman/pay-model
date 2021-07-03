package com.mathbot.pay.lightning

import play.api.libs.json.Json

case class ListInvoicesRequest(label: Option[String], invstring: Option[String], payment_hash: Option[String])
    extends LightningJson

object ListInvoicesRequest {
  implicit val formatListInvoicesRequest = Json.format[ListInvoicesRequest]
}
