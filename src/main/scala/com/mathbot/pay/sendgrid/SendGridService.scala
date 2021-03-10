package com.mathbot.pay.sendgrid

import play.api.libs.json.Json
import sttp.client._
import sttp.model.MediaType

import scala.concurrent.Future

class SendGridService(config: SendGridConfig, backend: SttpBackend[Future, Nothing, NothingT]) {
  implicit val be = backend
  def sendMessage(email: EmailMessage): Future[Response[Either[String, String]]] = {
    val req = basicRequest
      .post(uri"${config.baseUrl}/v3/mail/send")
      .auth
      .bearer(config.secretKey)
      .contentType(MediaType.ApplicationJson)
      .body(Json.toJson(SendGridEmail(email)).toString)
    req.send()
  }
}
