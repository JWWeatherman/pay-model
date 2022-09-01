package btcpayserver

import play.api.libs.json.{Json, OFormat}

case class Payments(
    id: String,
    receivedDate: String,
    value: Double,
    fee: Double,
    paymentType: String,
    confirmed: Boolean,
    completed: Boolean,
    destination: String
)

object Payments {

  implicit val formatPayments: OFormat[Payments] = Json.format[Payments]
}
