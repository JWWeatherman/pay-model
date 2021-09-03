package com.mathbot.pay.sendgrid

import com.mathbot.pay.Sensitive
import com.typesafe.config.{Config, ConfigFactory}

object SendGridConfig {
  def forConfig(config: Config = ConfigFactory.load()): SendGridConfig =
    SendGridConfig(Sensitive(config.getString("sendGrid.apiKey")))
}
case class SendGridConfig(secretKey: Sensitive, baseUrl: String = "https://api.sendgrid.com")
