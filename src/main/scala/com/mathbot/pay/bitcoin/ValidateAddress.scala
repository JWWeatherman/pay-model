package com.mathbot.pay.bitcoin

import play.api.libs.json.{Json, OFormat}

final case class ValidateAddress(
    address: String,
    isscript: Boolean,
    isvalid: Boolean,
    scriptPubKey: String,
    iswitness: Boolean
//    ismine: Boolean,
//    iswatchonly: Boolean,
//    pubkey: String,
//    iscompressed: Boolean,
//    account: String,
//    timestamp: Long,
//    hdkeypath: String,
//    hdmasterkeyid: String
) {
//  override def toString() = {
//    s"""isvalid: $isvalid
//       |address: $address
//       |scriptPubKey: $scriptPubKey
//       |ismine: $ismine
//       |iswatchonly: $iswatchonly
//       |isscript: $isscript
//       |pubkey: $pubkey
//       |iscompressed: $iscompressed
//       |account: $account
//       |timestamp: $timestamp
//       |hdkeypath: $hdkeypath
//       |hdmasterkeyid: $hdmasterkeyid""".stripMargin
//  }
}

object ValidateAddress {
  implicit val validateAddressFormat: OFormat[ValidateAddress] = Json.format[ValidateAddress]
}
