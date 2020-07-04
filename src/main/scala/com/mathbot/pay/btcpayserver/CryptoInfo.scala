package com.mathbot.pay.btcpayserver

import play.api.libs.json.{Json, OFormat}

case class CryptoInfo(
    cryptoCode: String,
    paymentType: String,
    rate: Double,
    exRates: ExRates,
    paid: String,
    price: String,
    due: String,
    paymentUrls: PaymentUrls,
    address: String,
    url: String,
    totalDue: String,
    networkFee: String,
    txCount: Double,
    cryptoPaid: String,
    payments: Seq[Payments]
)

object CryptoInfo {

  // implicit val formatPaymentSeq: OFormat[Seq[Payments]] = Json.format[Seq[Payments]]
  implicit val formatCryptoInfo: OFormat[CryptoInfo] = Json.format[CryptoInfo]
}
