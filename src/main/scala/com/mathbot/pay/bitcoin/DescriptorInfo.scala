package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

case class DescriptorInfo(descriptor: String, checksum: String, issolvable: Boolean, hasprivatekeys: Boolean)

object DescriptorInfo {
  implicit val formatDescriptorInfo = Json.format[DescriptorInfo]
}
