package com.mathbot.pay.lightning.spark

import com.mathbot.pay.Sensitive
import com.typesafe.config.Config

case class SparkLightningWalletServiceConfig(baseUrl: String, accessKey: Sensitive)
object SparkLightningWalletServiceConfig {
  def forConfig(config: Config): SparkLightningWalletServiceConfig =
    SparkLightningWalletServiceConfig(
      config.getString("spark.baseUrl"),
      Sensitive(config.getString("spark.accessKey"))
    )
}
