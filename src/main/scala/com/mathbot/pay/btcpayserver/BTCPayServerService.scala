package com.mathbot.pay.btcpayserver

import com.github.dwickern.macros.NameOf.nameOf
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.Json
import sttp.client3._
import sttp.model.MediaType

import java.util.Base64
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

@deprecated("use BTCPayServerServiceV2 b/c of issues w/ basic auth w/ sttp", "2021-02-21")
class BTCPayServerService(backend: SttpBackend[Future, Any], config: BTCPayServerConfig) {
  import sttp.client3._
  private val basic = s"Basic ${Base64.getEncoder.encodeToString(config.apiKey.getBytes())}"

  val baseRequest = sttp.client3.basicRequest
//    .basic(config.apiKey, "")
    .header("Authorization", config.apiKey, true)
    .contentType(MediaType.ApplicationJson)
    .readTimeout(Duration("30s"))
    .followRedirects(true)

  def getInvoice(id: String): Future[Response[Either[String, ChargeInfoResponse]]] = {
    val request = baseRequest
      .get(uri"${config.baseUrl}/invoices/$id")
      .response(parseChargeInfo)
    request.send(backend)
  }

  def invoice(inv: InvoiceRequest): Future[Response[Either[String, ChargeInfoResponse]]] = {
    val request = baseRequest
      .post(uri"${config.baseUrl}/invoices")
      .body(Json.toJson(inv).toString)
      .response(parseChargeInfo)
    request.send(backend)
  }

  private def parseChargeInfo: ResponseAs[Either[String, ChargeInfoResponse], Any] =
    asString.map {
      case Left(value) =>
        Left(value)
      case Right(bodyStr) =>
        scala.util.Try(Json.parse(bodyStr)) match {
          case Failure(exception) => Left(s"Failed to parse json $bodyStr $exception")
          case Success(value) =>
            value
              .validate[ChargeInfoResponse]
              .map(Right(_))
              .getOrElse(Left(s"Response not a ${nameOf(ChargeInfoResponse)} $value"))
        }
    }

}
