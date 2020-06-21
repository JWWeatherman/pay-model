package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class Pays(pays: Seq[ListPay])

object Pays {
  lazy implicit val formatPays: OFormat[Pays] = Json.format[Pays]
}
