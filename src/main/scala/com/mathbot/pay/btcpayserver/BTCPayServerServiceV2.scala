package com.mathbot.pay.btcpayserver

import com.github.dwickern.macros.NameOf.nameOf
import play.api.libs.json.{JsError, JsResult, Json}
import requests.Response

import scala.util.Try

class BTCPayServerServiceV2(
    config: BTCPayServerConfig
) {

  private val baseUrl = config.baseUrl
  private val headers = Map("Authorization" -> config.apiKey, "Content-Type" -> "application/json")

  def invoice(invoice: InvoiceRequest): (Response, JsResult[ChargeInfoResponse]) = {
    val response = requests.post(
      url = s"$baseUrl/invoices",
      data = Json.toJson(invoice).toString(),
      headers = headers,
      check = false // requests.RequestFailedException(val response: Response) if a non-2xx status code is received.
      // You can disable throwing the exception by passing in check = false
    )
    (response, asChargeInfoResponse(response.text()))
  }
  def getInvoice(id: String): (Response, JsResult[ChargeInfoResponse]) = {
    val response = requests.get(s"$baseUrl/invoices/$id", headers = headers, check = false)
    (response, asChargeInfoResponse(response.text()))
  }

  def exchangeRate(storeId: String): (Response, JsResult[Option[BitcoinExchangeRate]]) = {
    val response = requests.get(s"$baseUrl/api/rates?storeId=$storeId", check = false)
    val str = response.text()
    val r = Try(Json.parse(str))
      .map(_.validate[Seq[BitcoinExchangeRate]].map(_.find(_.currencyPair == "BTC_USD")))
      .getOrElse(JsError(s"Not a Seq[${nameOf(BitcoinExchangeRate)}] $str"))
    (response, r)
  }

  private def asChargeInfoResponse(str: String) =
    Try(Json.parse(str))
      .map(_.validate[ChargeInfoResponse])
      .getOrElse(JsError(s"Not a ${nameOf(ChargeInfoResponse)} $str"))
}
