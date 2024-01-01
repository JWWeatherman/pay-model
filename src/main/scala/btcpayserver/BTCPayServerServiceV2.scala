package btcpayserver

import com.github.dwickern.macros.NameOf.nameOf
import play.api.libs.json.{JsError, JsResult, Json}
import requests.Response

import java.util.Base64
import scala.util.Try

class BTCPayServerServiceV2(
    config: BTCPayServerConfig
) {

  private val basic = s"Basic ${Base64.getEncoder.encodeToString(config.apiKey.getBytes())}"
  private val baseUrl = config.baseUrl
  private val headers = Map("Authorization" -> basic, "Content-Type" -> "application/json")
  private val headersV1 = Map("Authorization" -> s"token ${config.tokenV1}", "Content-Type" -> "application/json")

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

  object V1 {
    // https://btcpal.space/docs#operation/Invoices_CreateInvoice
    // returns 401
    // returns 200 {"id":"8euF5EoEK1zeDVkBnoWbuP","storeId":"82UqSQBJmzXgb5kBXz1WStDUerBR3fgste3CAGZW54qk","amount":"1.00","checkoutLink":"https://btcpal.space/i/8euF5EoEK1zeDVkBnoWbuP","status":"New","additionalStatus":"None","monitoringExpiration":1675514208,"expirationTime":1675427808,"createdTime":1675341408,"availableStatusesForManualMarking":["Settled","Invalid"],"archived":false,"type":"Standard","currency":"USD","metadata":{"buyerEmail":"jchimien@gmail.com"},"checkout":{"speedPolicy":"MediumSpeed","paymentMethods":["BTC","BTC-LightningNetwork","BTC-LNURLPAY"],"defaultPaymentMethod":null,"expirationMinutes":1440,"monitoringMinutes":1440,"paymentTolerance":10.0,"redirectURL":null,"redirectAutomatically":false,"requiresRefundEmail":null,"defaultLanguage":null,"checkoutType":null},"receipt":{"enabled":null,"showQR":null,"showPayments":null}}
    def invoice(storeId: String, amount: String, currency: String, email: Option[String]) = {

      requests.post(
        url = s"$baseUrl/api/v1/stores/$storeId/invoices",
        data = Json
          .obj(
            "metadata" -> Json.obj(
              "buyerEmail" -> email
            ),
            "amount" -> amount,
            "currency" -> currency
          )
          .toString(),
        headers = headersV1,
        check = false
      )
    }
    // return 200 {"id":"8euF5EoEK1zeDVkBnoWbuP","storeId":"82UqSQBJmzXgb5kBXz1WStDUerBR3fgste3CAGZW54qk","amount":"1.00","checkoutLink":"https://btcpal.space/i/8euF5EoEK1zeDVkBnoWbuP","status":"New","additionalStatus":"None","monitoringExpiration":1675514208,"expirationTime":1675427808,"createdTime":1675341408,"availableStatusesForManualMarking":["Settled","Invalid"],"archived":false,"type":"Standard","currency":"USD","metadata":{"buyerEmail":"jchimien@gmail.com"},"checkout":{"speedPolicy":"MediumSpeed","paymentMethods":["BTC","BTC-LightningNetwork","BTC-LNURLPAY"],"defaultPaymentMethod":null,"expirationMinutes":1440,"monitoringMinutes":1440,"paymentTolerance":10.0,"redirectURL":null,"redirectAutomatically":false,"requiresRefundEmail":null,"defaultLanguage":null,"checkoutType":null},"receipt":{"enabled":null,"showQR":null,"showPayments":null}}
    def getInvoice(storeId: String, invoiceId: String) =
      requests.get(url = s"$baseUrl/api/v1/stores/$storeId/invoices/$invoiceId", headers = headersV1, check = false)
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
