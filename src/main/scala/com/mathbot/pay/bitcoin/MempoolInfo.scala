package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

object MempoolInfo {
  implicit val formatMempoolInfo = Json.format[MempoolInfo]
}
case class MempoolInfo(
    loaded: Boolean,
    size: Long,
    bytes: Long,
    usage: Long,
    maxmempool: Double,
    mempoolminfee: Double,
    minrelaytxfee: Double
)
