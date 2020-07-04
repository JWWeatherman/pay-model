package com.mathbot.pay.btcpayserver

import play.api.libs.json._

case class BitcoinExchangeRate(
    name: String,
    cryptoCode: String,
    currencyPair: String,
    code: String,
    rate: BigDecimal
)

object BitcoinExchangeRate {

  implicit val formatBitcoinExchangeRate: OFormat[BitcoinExchangeRate] = Json.format[BitcoinExchangeRate]
}
