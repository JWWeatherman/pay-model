package com.mathbot.pay.sendgrid

import com.typesafe.config.{Config, ConfigFactory}

object SendGridConfig {
  def forConfig(config: Config = ConfigFactory.load()): SendGridConfig =
    SendGridConfig(config.getString("sendGrid.apiKey"))
}
case class SendGridConfig(secretKey: String, baseUrl: String = "https://api.sendgrid.com")
