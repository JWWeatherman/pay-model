package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class ListPaysRequest(bolt11: Bolt11) extends LightningJson

object ListPaysRequest {
  implicit val formatListPaysRequest: OFormat[ListPaysRequest] = Json.format[ListPaysRequest]
}
