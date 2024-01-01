package email.sendgrid

import email.{EmailMessage, EmailService}
import sttp.client3._

import scala.concurrent.{ExecutionContext, Future}

class SendGridService(config: SendGridConfig, backend: SttpBackend[Future, Any])(implicit ec: ExecutionContext)
    extends EmailService {
  import playJson._
  def sendMessage(email: EmailMessage): Future[Response[Either[String, String]]] = {
    val req = basicRequest
      .post(uri"${config.baseUrl}/v3/mail/send")
      .auth
      .bearer(config.secretKey.value)
      .body(SendGridEmail(email))
    req.send(backend)
  }
}
