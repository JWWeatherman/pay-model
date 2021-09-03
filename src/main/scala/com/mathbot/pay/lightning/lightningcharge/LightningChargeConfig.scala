package com.mathbot.pay.lightning.lightningcharge

import com.mathbot.pay.Sensitive
import com.typesafe.config.Config

/**
 * @param username
 * @param password
 * @param baseUrl
 * @param websocketUrl to subscribe for payment events
 */
case class LightningChargeConfig(username: String, password: Sensitive, baseUrl: String, websocketUrl: String)

object LightningChargeConfig {
  def forConfig(config: Config): LightningChargeConfig =
    LightningChargeConfig(
      username = config.getString("lightningCharge.username"),
      password = Sensitive(config.getString("lightningCharge.password")),
      baseUrl = config.getString("lightningCharge.baseUrl"),
      websocketUrl = config.getString("lightningCharge.websocketUrl")
    )
}
