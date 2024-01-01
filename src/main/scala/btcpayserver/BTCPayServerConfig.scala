package btcpayserver

import com.typesafe.config.{Config, ConfigFactory}

case class BTCPayServerConfig(baseUrl: String, apiKey: String, tokenV1: String)

object BTCPayServerConfig {

  def forConfig(config: Config = ConfigFactory.load()): BTCPayServerConfig = {
    val baseUrl = config.getString("btcpayserver.baseUrl")
    val apiKey = config.getString("btcpayserver.apiKey")
    val token = config.getString("btcpayserver.tokenV1")
    BTCPayServerConfig(baseUrl = baseUrl, apiKey = apiKey, tokenV1 = token)
  }
}
