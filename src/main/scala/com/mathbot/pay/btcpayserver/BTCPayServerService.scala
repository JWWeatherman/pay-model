package com.mathbot.pay.btcpayserver

import java.util.Base64

import com.github.dwickern.macros.NameOf.nameOf
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.Json
import sttp.client._
import sttp.model.MediaType

import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class BTCPayServerService(
    backend: SttpBackend[Future, Nothing, NothingT],
    config: BTCPayServerConfig,
    ec: ExecutionContext,
    logger: Logger = LoggerFactory.getLogger("BTCPayServerInvoiceService")
) {

  private implicit val be: SttpBackend[Future, Nothing, NothingT] = backend
  private implicit val e: ExecutionContext = ec

  private val basic = s"Basic ${Base64.getEncoder.encodeToString(config.apiKey.getBytes())}"

  private final val baseRequest = sttp.client.basicRequest
    .header("Authorization", config.apiKey, true)
    .contentType(MediaType.ApplicationJson)
    .readTimeout(Duration("30s"))
    .followRedirects(true)

  def getInvoice(id: String): Future[Response[Either[String, ChargeInfoResponse]]] = {
    val request = baseRequest
      .get(uri"${config.baseUrl}/invoices/$id")
      .response(parseChargeInfo)
    logger.info(request.toCurl)
    request.send()
  }

  def invoice(inv: InvoiceRequest): Future[Response[Either[String, ChargeInfoResponse]]] = {
    val request = baseRequest
      .post(uri"${config.baseUrl}/invoices")
      .body(Json.toJson(inv).toString)
      .response(parseChargeInfo)
    val curl = request.toCurl
    logger.debug(curl)
    request.send()
  }

  private def parseChargeInfo: ResponseAs[Either[String, ChargeInfoResponse], Nothing] =
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
