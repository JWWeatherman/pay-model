package email

import sttp.client3.Response

import scala.concurrent.Future

trait EmailService {

  def sendMessage(email: EmailMessage): Future[Response[Either[String, String]]]
}
