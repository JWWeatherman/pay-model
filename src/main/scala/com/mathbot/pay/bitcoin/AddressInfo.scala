package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

object AddressLabels {
  implicit val formatLabels = Json.format[AddressLabels]
}

case class AddressLabels(
    name: String,
    purpose: String
)

object AddressInfo {
  implicit val formatAddressInfo = Json.format[AddressInfo]
}
case class AddressInfo(
    address: BtcAddress,
    scriptPubKey: String,
    ismine: Boolean,
    solvable: Boolean,
    desc: Option[String],
    iswatchonly: Boolean,
    isscript: Boolean,
    iswitness: Boolean,
    witness_version: Option[Int],
    witness_program: Option[String],
    script: Option[String],
    hex: Option[String],
    sigsrequired: Option[Int],
    pubkeys: Option[List[String]],
    label: Option[String],
    ischange: Boolean,
    labels: List[AddressLabels]
)
