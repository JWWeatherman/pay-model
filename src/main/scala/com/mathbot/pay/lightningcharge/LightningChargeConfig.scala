package com.mathbot.pay.lightningcharge

import com.typesafe.config.Config

import scala.concurrent.duration._

case class LightningChargeConfig(username: String,
                                 password: String,
                                 invoiceExpiry: FiniteDuration,
                                 baseUrl: String,
                                 websocketUrl: String)

object LightningChargeConfig {
  def forConfig(config: Config): LightningChargeConfig = LightningChargeConfig(
    username = config.getString("lightningCharge.username"),
    password = config.getString("lightningCharge.password"),
    invoiceExpiry = Duration(config.getString("lightningCharge.invoiceExpiry")).asInstanceOf[FiniteDuration],
    baseUrl = config.getString("lightningCharge.baseUrl"),
    websocketUrl = config.getString("lightningCharge.websocketUrl"),
  )
}
