package btcpayserver

import com.mathbot.pay.bitcoin.BtcAddress
import com.mathbot.pay.lightning.Bolt11
import fr.acinq.eclair.payment.Bolt11Invoice
import play.api.libs.json.{Json, OFormat}

import scala.util.Try

case class BtcPayPayment(
    id: String,
    receivedDate: String, // 2023-01-31T09:53:05
    value: Double, // Btc todo: writes string will break db
    fee: Double, // Btc todo: writes string will break db
    paymentType: String,
    confirmed: Boolean,
    completed: Boolean,
    destination: String
) {
  lazy val bolt11: Either[String, Bolt11] =
    if (Bolt11Invoice.prefixes.values.exists(destination.startsWith))
      Try(Bolt11(destination)).toEither.left.map(_.getMessage)
    else Left("not a bolt11")
  lazy val bitcoinAddr: Either[Throwable, BtcAddress] = Try(BtcAddress(destination)).toEither
  lazy val isLightningPayment: Boolean = bolt11.isRight
  lazy val isBitcoinPayment: Boolean = bitcoinAddr.isRight
}

object BtcPayPayment {
  implicit val formatPayments: OFormat[BtcPayPayment] = Json.format[BtcPayPayment]
}
