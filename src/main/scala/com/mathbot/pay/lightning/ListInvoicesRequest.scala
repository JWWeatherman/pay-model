package com.mathbot.pay.lightning

import play.api.libs.json.Json

case class ListInvoicesRequest(
    label: Option[String] = None,
    invstring: Option[String] = None,
    payment_hash: Option[String] = None,
    offer_id: Option[String] = None
) extends LightningJson {
  val nonEmptyParams = List(label, invstring, payment_hash, offer_id).flatten
  require(nonEmptyParams.length <= 1, "Invalid request")
}

object ListInvoicesRequest {
  implicit val formatListInvoicesRequest = Json.format[ListInvoicesRequest]
}
