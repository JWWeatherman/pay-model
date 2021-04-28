package com.mathbot.pay.lightningcharge

import com.typesafe.config.Config

/**
 *
 * @param username
 * @param password
 * @param baseUrl
 * @param websocketUrl to subscribe for payment events
 */
case class LightningChargeConfig(username: String, password: String, baseUrl: String, websocketUrl: String)

object LightningChargeConfig {
  def forConfig(config: Config): LightningChargeConfig = LightningChargeConfig(
    username = config.getString("lightningCharge.username"),
    password = config.getString("lightningCharge.password"),
    baseUrl = config.getString("lightningCharge.baseUrl"),
    websocketUrl = config.getString("lightningCharge.websocketUrl"),
  )
}
