package email.mailgun

import com.mathbot.pay.Sensitive

case class MailgunConfig(baseUrl: String, domain: String, apiKey: Sensitive)

object MailgunConfig {

  val defaultBaseUrl = "https://api.mailgun.net"

  def apply(domain: String, apiKey: String): MailgunConfig = MailgunConfig(defaultBaseUrl, domain, Sensitive(apiKey))
}
