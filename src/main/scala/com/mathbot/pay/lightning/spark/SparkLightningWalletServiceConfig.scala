package com.mathbot.pay.lightning.spark

import com.typesafe.config.Config

case class SparkLightningWalletServiceConfig(baseUrl: String, accessKey: String)
object SparkLightningWalletServiceConfig {
  def forConfig(config: Config): SparkLightningWalletServiceConfig = SparkLightningWalletServiceConfig(
    config.getString("spark.baseUrl"),
    config.getString("spark.accessKey"),
  )
}
