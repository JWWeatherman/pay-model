package com.mathbot.pay.lightning.lightningcharge

import play.api.libs.json.{JsError, Json}
import sttp.capabilities.akka.AkkaStreams
import sttp.client3.playJson.asJson
import sttp.client3.{SttpBackend, basicRequest, _}
import sttp.model.{MediaType, StatusCode}

import scala.concurrent.Future
import scala.concurrent.duration._

/**
 * https://github.com/ElementsProject/lightning-charge
 * @param config
 * @param backend
 * @param ec
 */
class LightningChargeService(
    config: LightningChargeConfig,
    backend: SttpBackend[Future, AkkaStreams]
) {
  import playJson._
  private val base = basicRequest.auth
    .basic(config.username, config.password.value)
    .acceptEncoding(MediaType.ApplicationJson.toString())

  private final val baseInvoiceRequest = base
    .post(uri"${config.baseUrl}/invoice")
    .response(asJson[LightningChargeInvoice])

  def invoice(
      invoice: LightningChargeInvoiceRequest
  ): Future[Response[Either[ResponseException[String, JsError], LightningChargeInvoice]]] =
    baseInvoiceRequest.body(invoice).send(backend)

  def invoice(
      invoice: LightningChargeInvoiceRequestByCurrency
  ) =
    baseInvoiceRequest.body(invoice).send(backend)

  /*
    https://github.com/ElementsProject/lightning-charge#get-invoiceidwaittimeoutsec
    GET /invoice/:id/wait?timeout=[sec]
   */
  def longPoll(
      id: String,
      timeout: FiniteDuration = 20.seconds
  ) = {
    val params = Map("timeout" -> timeout.toSeconds)
    val req = base
      .get(uri"${config.baseUrl}/invoice/$id/wait?$params")
      .readTimeout(timeout.plus(3.seconds))
      .response(asJson[LightningChargeInvoice])
    req.send(backend)

  }

  def get(id: String) = {
    val req = base
      .get(uri"${config.baseUrl}/invoice/$id")
      .response(asJson[LightningChargeInvoice])
    req.send(backend)
  }

  def getAll() = {
    val req = base
      .get(uri"${config.baseUrl}/invoices")
      .response(asJson[Seq[LightningChargeInvoice]])
    req.send(backend)
  }

}

object LightningChargeService {
  case class InvoiceResponseError(status: StatusCode)
}
