package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

object Softforks {
  implicit val formatSoftforks = Json.format[Softforks]
}
case class Softforks(
    bip34: Bip,
    bip66: Bip,
    bip65: Bip,
    csv: Bip,
    segwit: Bip
)
