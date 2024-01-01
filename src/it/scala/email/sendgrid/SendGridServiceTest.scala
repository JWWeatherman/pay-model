package email.sendgrid

import com.mathbot.pay.{BaseIntegrationTest, Sensitive}
import com.softwaremill.macwire.wire
import email.EmailMessage

class SendGridServiceTest extends BaseIntegrationTest {
  val config = SendGridConfig(Sensitive(sys.env("SENDGRID_API_KEY")))

  val service = wire[SendGridService]
  "SendGridService" should {
    "send email" in {
      service
        .sendMessage(new EmailMessage {
          override def asHtml: String = "test"

          override def from: String = sys.env.getOrElse("TEST_EMAIL_FROM", "")

          override def to: String = sys.env.getOrElse("TEST_EMAIL_TO", "")

          override def subject: String = "test"
        })
        .map(r => {
          println(r)
          assert(r.body.isRight)
        })
    }
  }
}
