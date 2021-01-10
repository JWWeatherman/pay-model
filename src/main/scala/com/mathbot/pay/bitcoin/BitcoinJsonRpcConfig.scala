package com.mathbot.pay.bitcoin

import com.typesafe.config.Config

case class BitcoinJsonRpcConfig(baseUrl: String, username: String, password: String, jsonRpc: String = "1.0")

object BitcoinJsonRpcConfig {
  def forConfig(config: Config): BitcoinJsonRpcConfig = {
    val baseUrl: String = config.getString("bitcoin.rpc.baseUrl")
    val user: String = config.getString("bitcoin.rpc.user")
    val pw: String = config.getString("bitcoin.rpc.password")
    BitcoinJsonRpcConfig(baseUrl, user, pw)
  }
}
