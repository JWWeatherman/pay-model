package com.mathbot.pay.lightning

import play.api.libs.json.Json

/**
 * Represents an offer. The lightning node can use this string to request 1+ invoices.
 *
 * @param bolt12
 */
case class Bolt12(bolt12: String)
object Bolt12 {
  implicit val formatBolt12 = Json.format[Bolt12]
}
