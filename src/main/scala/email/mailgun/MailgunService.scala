package email.mailgun

import email.{EmailMessage, EmailService}
import sttp.client3.{SttpBackend, _}

import scala.concurrent.{ExecutionContext, Future}

class MailgunService(config: MailgunConfig, backend: SttpBackend[Future, Any])(implicit ec: ExecutionContext)
    extends EmailService {

  /**
   * https://documentation.mailgun.com/en/latest/api-sending.html#sending
   * @param email
   * @return 200 {"id":"<20221129145158.01930d43a0da70f2@mg.pollofeed.com>","message":"Queued. Thank you."}
   */
  def sendMessage(email: EmailMessage) =
    basicRequest
      .post(uri"${config.baseUrl}/v3/${config.domain}/messages")
      .auth
      .basic("api", config.apiKey.value)
      .body(
        Map(
          "from" -> email.from,
          "to" -> email.to,
          "subject" -> email.subject,
          "text" -> email.asHtml
        )
      )
      .send(backend)

}
