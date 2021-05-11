package com.mathbot.pay.btcpayserver

import com.typesafe.config.{Config, ConfigFactory}

case class BTCPayServerConfig(baseUrl: String, apiKey: String, storeId: String)

object BTCPayServerConfig {

  def forConfig(config: Config = ConfigFactory.load()): BTCPayServerConfig = {
    val baseUrl = config.getString("btcpayserver.baseUrl")
    val apiKey = config.getString("btcpayserver.apiKey")
    val storeId = config.getString("btcpayserver.storeId")
    BTCPayServerConfig(baseUrl = baseUrl, apiKey = apiKey, storeId = storeId)
  }
}
