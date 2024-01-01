package email.sendgrid

import com.mathbot.pay.Sensitive
import com.typesafe.config.Config

object SendGridConfig {
  def forConfig(config: Config): SendGridConfig =
    // todo: baseurl
    SendGridConfig(Sensitive(config.getString("sendGrid.apiKey")))
}
case class SendGridConfig(secretKey: Sensitive, baseUrl: String = "https://api.sendgrid.com")
