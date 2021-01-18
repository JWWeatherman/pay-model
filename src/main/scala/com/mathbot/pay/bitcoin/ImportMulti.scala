package com.mathbot.pay.bitcoin

import play.api.libs.json.{Json, OFormat}

case class ImportMulti(desc: String, range: Seq[Int], watchonly: Boolean, timestamp: String)
object ImportMulti {
  implicit val formatImportMulti: OFormat[ImportMulti] = Json.format[ImportMulti]
}
