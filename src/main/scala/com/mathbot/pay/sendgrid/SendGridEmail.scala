package com.mathbot.pay.sendgrid

import play.api.libs.json.Json

object To {
  implicit val formatTo = Json.format[To]
}
case class To(
    email: String
)
object Personalizations {
  implicit val formatPersonalizations = Json.format[Personalizations]
}
case class Personalizations(
    to: List[To]
)
object Content {
  implicit val formatContent = Json.format[Content]
}
case class Content(
    `type`: String,
    value: String
)
object SendGridEmail {
  implicit val formatSendGridEmail = Json.format[SendGridEmail]

  def apply(email: EmailMessage): SendGridEmail =
    SendGridEmail(
      personalizations = List(Personalizations(List(To(email.to)))),
      from = To(email.from),
      subject = email.subject,
      content = List(Content("text/html", email.asHtml))
    )
}
case class SendGridEmail(
    personalizations: List[Personalizations],
    from: To,
    subject: String,
    content: List[Content]
)
