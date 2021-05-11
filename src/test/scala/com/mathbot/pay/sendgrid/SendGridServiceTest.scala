package com.mathbot.pay.sendgrid

import akka.http.scaladsl.util.FastFuture
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.wordspec.AsyncWordSpec
import org.scalatestplus.mockito.MockitoSugar

class SendGridServiceTesst extends AsyncWordSpec with MockitoSugar {
  val c = SendGridConfig("")
  val service = mock[SendGridService]
  "SendGridServiceTest" should {
    "sendMessage" in {

      when(service.sendMessage(any[EmailMessage])) thenReturn FastFuture.successful(
        sttp.client.Response.ok(Right("accepted"))
      )

      service
        .sendMessage(new EmailMessage {
          override def asHtml: String = "..."

          override def from: String = "me"

          override def to: String = "you"

          override def subject: String = "will you go out with me?"
        })
        .map(r => assert(r.body.right.get === "accepted"))
    }
  }
}
