package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

object Networks {
  implicit val formatNetworks = Json.format[Networks]
}
case class Networks(
    name: String,
    limited: Boolean,
    reachable: Boolean,
    proxy: String,
    proxy_randomize_credentials: Boolean
)
