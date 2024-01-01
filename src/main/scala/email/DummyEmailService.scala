package email

import akka.http.scaladsl.util.FastFuture
import com.typesafe.scalalogging.StrictLogging
import sttp.client3.Response

import scala.concurrent.Future

class DummyEmailService extends EmailService with StrictLogging {
  override def sendMessage(email: EmailMessage): Future[Response[Either[String, String]]] = FastFuture.successful {
    val msg = "skipping sending email on dummy"
    logger.info(msg)
    Response.ok(Right(msg))
  }
}
