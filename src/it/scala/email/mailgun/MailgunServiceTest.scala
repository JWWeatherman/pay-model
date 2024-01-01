package email.mailgun

import com.mathbot.pay.{BaseIntegrationTest, Sensitive}
import com.softwaremill.macwire.wire
import email.EmailMessage

class MailgunServiceTest extends BaseIntegrationTest {

  val config =
    MailgunConfig(
      baseUrl = "https://api.mailgun.net",
      apiKey = Sensitive(sys.env("MAILGUN_API_KEY")),
      domain = sys.env("MAILGUN_DOMAIN")
    )
  val service = wire[MailgunService]
  "MailgunService" should {
    "send email" in {
      service
        .sendMessage(new EmailMessage {
          override def asHtml: String = "test"

          override def from: String = sys.env.getOrElse("TEST_EMAIL_FROM", "")

          override def to: String = sys.env.getOrElse("TEST_EMAIL_TO", "")

          override def subject: String = "test"
        })
        .map(r => {
          assert(r.body.isRight, r)
        })
    }
  }
}
