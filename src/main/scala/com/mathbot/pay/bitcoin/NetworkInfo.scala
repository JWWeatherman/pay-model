package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

object NetworkInfo {
  implicit val formatNetworkInfo = Json.format[NetworkInfo]
}
case class NetworkInfo(
    version: Double,
    subversion: String,
    protocolversion: Double,
    localservices: String,
    localservicesnames: Option[List[String]],
    localrelay: Boolean,
    timeoffset: Double,
    networkactive: Boolean,
    connections: Double,
    networks: List[Networks],
    relayfee: Double,
    incrementalfee: Double,
//    localaddresses: List[Localaddresses],
    warnings: String
)
