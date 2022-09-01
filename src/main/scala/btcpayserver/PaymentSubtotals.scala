package btcpayserver

import com.mathbot.pay.json.PlayJsonSupport
import fr.acinq.bitcoin.Satoshi
import play.api.libs.json._

case class PaymentSubtotals(
    BTC: Option[Satoshi],
    BTC_LightningLike: Option[Satoshi]
)

object PaymentSubtotals extends PlayJsonSupport {
  implicit val formatPaymentSubtotals: OFormat[PaymentSubtotals] = Json.format[PaymentSubtotals]
}
