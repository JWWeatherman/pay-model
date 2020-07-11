package com.mathbot.pay.btcpayserver

import java.time.Instant

import com.mathbot.pay.bitcoin.{Btc, BtcAddress}
import com.mathbot.pay.btcpayserver.CreditStatus.CreditStatus
import com.mathbot.pay.btcpayserver.InvoiceException.InvoiceException
import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Json, _}

case class ChargeInfoData(
    url: String,
    status: CreditStatus,
    cryptoInfo: Seq[CryptoInfo],
    price: Double, // usd
    id: String,
    invoiceTime: Instant,
    expirationTime: Instant,
    btcPaid: Btc,
    rate: Double,
    refundAddressRequestPending: Boolean,
    bitcoinAddress: BtcAddress,
    paymentTotals: PaymentSubtotals,
    amountPaid: Btc,
    addresses: PaymentAddresses,
    buyer: Buyer,
    currency: String = "USD",
    itemDesc: String = "",
    exceptionStatus: Option[InvoiceException] = None
)

object ChargeInfoData extends PlayJsonSupport {

  implicit val readsChargeInfoData: Reads[ChargeInfoData] = (
    (__ \ 'url).read[String] and
    (__ \ 'status).read[String].map(i => CreditStatus.withName(i)) and
    (__ \ 'cryptoInfo).read[Seq[CryptoInfo]] and
    (__ \ 'price).read[Double] and
    (__ \ 'id).read[String] and
    (__ \ 'invoiceTime).read[Instant] and
    (__ \ 'expirationTime).read[Instant] and
    (__ \ 'btcPaid).read[Btc] and
    (__ \ 'rate).read[Double] and
    (__ \ 'refundAddressRequestPending).read[Boolean] and
    (__ \ 'bitcoinAddress).read[BtcAddress] and
    (__ \ 'paymentTotals).read[PaymentSubtotals] and
    (__ \ 'amountPaid).read[Btc] and
    (__ \ 'addresses).read[PaymentAddresses] and
    (__ \ 'buyer).read[Buyer] and
    (__ \ 'currency).read[String] and
    (__ \ 'itemDesc).readWithDefault[String]("") and
    (__ \ 'exceptionStatus)
      .read[String]
      .orElse((__ \ 'exceptionStatus).read[Boolean].map(_.toString))
      .map {
        case "false" => None
        case exception => Some(InvoiceException.withName(exception))
      }
  )(ChargeInfoData.apply _)
  implicit val writesChargeInfoData: OWrites[ChargeInfoData] = Json.writes[ChargeInfoData]

}
