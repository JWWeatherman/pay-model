package com.mathbot.pay.lightning

import play.api.libs.json.Json

object WaitInvoice {
  implicit val formatWaitInvoice = Json.format[WaitInvoice]
}
case class WaitInvoice(label: String) extends LightningJson

