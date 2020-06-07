package payModel.models.lightning

import play.api.libs.json.Json

case class Payments(payments: Seq[Payment])

object Payments {
  implicit val fomratPayments = Json.format[Payments]
}
