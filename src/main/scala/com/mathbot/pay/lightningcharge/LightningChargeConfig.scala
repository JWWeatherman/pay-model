package com.mathbot.pay.lightningcharge

import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration._
import scala.util.Try

case class LightningChargeConfig(username: String,
                                 password: String,
                                 invoiceExpiry: FiniteDuration,
                                 baseUrl: String,
                                 webhookUrlBase: String,
                                 requestTimeout: FiniteDuration) {

  lazy val websocketUrl = baseUrl.replace("https", "wss") + "ws"
}

object LightningChargeConfig {

  def forConfig(config: Config = ConfigFactory.load()) = {
    // todo: full string
    val un: String = config.getString("lightningCharge.un")
    val pw: String = config.getString("lightningCharge.pw")
    val invoiceExpiry: FiniteDuration = Try(
      config
        .getString("lightningCharge.expiry")
    ).map(Duration(_).asInstanceOf[FiniteDuration]).getOrElse(10.minutes)
    val chargeUrl: String = config.getString("lightningCharge.url")
    val webhookUrlBase: String = config.getString("lightningCharge.webhookUrlBase")

    LightningChargeConfig(username = un,
                          password = pw,
                          invoiceExpiry = invoiceExpiry,
                          baseUrl = chargeUrl,
                          webhookUrlBase = webhookUrlBase,
                          requestTimeout = 30.seconds)

  }
}
