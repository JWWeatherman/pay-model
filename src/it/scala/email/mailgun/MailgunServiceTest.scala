package email.mailgun

import com.mathbot.pay.{BaseIntegrationTest, Sensitive}
import com.softwaremill.macwire.wire
import email.EmailMessage

class MailgunServiceTest extends BaseIntegrationTest {

  val config =
    MailgunConfig(
      baseUrl = "https://api.mailgun.net",
      apiKey = Sensitive("4fef063c61440a4533e201e2c6799a08-f2340574-23720cce"),
      domain = "mg.pollofeed.com"
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
          assert(r.body.isRight)
        })
    }
  }
}
