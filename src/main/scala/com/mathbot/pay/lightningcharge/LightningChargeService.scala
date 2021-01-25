package com.mathbot.pay.lightningcharge

import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.Json
import sttp.client.{SttpBackend, _}
import sttp.model.{MediaType, StatusCode}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class LightningChargeService(
    config: LightningChargeConfig,
    backend: SttpBackend[Future, Nothing, NothingT],
    ec: ExecutionContext,
    logger: Logger = LoggerFactory.getLogger("LightningChargeService")
) {

  private implicit val e = ec
  private implicit val be = backend

  private val base = basicRequest.auth
    .basic(config.username, config.password)
    .acceptEncoding(MediaType.ApplicationJson.toString())

  def invoice(invoice: InvoiceRequest): Future[Response[Either[String, LightningChargeInvoice]]] = {
    val req = base
      .post(uri"${config.baseUrl}/invoice")
      .contentType(MediaType.ApplicationJson)
      .body(Json.toJson(invoice).toString)
      .response(asString.mapRight(Json.parse(_).as[LightningChargeInvoice]))
    req.send()
  }

  /*
    fixme akka timeout after 20 second
    https://github.com/ElementsProject/lightning-charge#get-invoiceidwaittimeoutsec
    GET /invoice/:id/wait?timeout=[sec]
   */
  def longPoll(id: String, timeout: FiniteDuration = 20.seconds) = {
    val params = Map("timeout" -> timeout.toSeconds)
    val req = base
      .get(uri"${config.baseUrl}/invoice/$id/wait?$params")
      .readTimeout(timeout.plus(1.second))
      .response(asString.mapRight(Json.parse(_).as[LightningChargeInvoice]))
    req.send()

  }

  def get(id: String) = {
    val req = base
      .get(uri"${config.baseUrl}/invoice/$id")
      .acceptEncoding(MediaType.ApplicationJson.toString())
      .response(asString.mapRight(Json.parse(_).as[LightningChargeInvoice]))
    req.send()
  }

  def getAll() = {
    val req = base
      .get(uri"${config.baseUrl}/invoices")
      .acceptEncoding(MediaType.ApplicationJson.toString())
      .response(asString.mapRight(Json.parse(_).as[Seq[LightningChargeInvoice]]))
    req.send()
  }

}

object LightningChargeService {
  case class InvoiceResponseError(status: StatusCode)
}
