package com.mathbot.pay.lightning.spark

import akka.stream.scaladsl.Source
import com.mathbot.pay.lightning._
import play.api.libs.json._
import sttp.capabilities.akka.AkkaStreams
import sttp.client3.SttpBackend
import sttp.client3.akkahttp.AkkaHttpServerSentEvents
import sttp.model.sse.ServerSentEvent

import scala.concurrent.Future

class SparkLightningWalletService(config: SparkLightningWalletServiceConfig, backend: SttpBackend[Future, AkkaStreams])
    extends LightningService {
  import SparkLightningWalletService._
  import sttp.client3._
  import sttp.client3.playJson._

  private val base = basicRequest
    .headers(Map("X-Access" -> config.accessKey.value))

  private def toBody[T](implicit reads: Reads[T]): ResponseAs[Either[LightningRequestError, T], Any] =
    asJson[T].mapLeft(err => LightningRequestError(ErrorMsg(500, s"Bad response $err"), None))

  private def makeBody(method: String, params: JsValue): JsObject =
    Json.obj("method" -> method, "params" -> params)

  override def listPays(
      l: ListPaysRequest = ListPaysRequest(bolt11 = None, payment_hash = None)
  ) = {
    base
      .post(uri"${config.baseUrl}")
      .body(makeBody("listpays", Json.toJson(l)))
      .response(toBody[Pays])
      .send(backend)

  }

  override def getInfo: Future[Response[Either[LightningRequestError, LightningNodeInfo]]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("getinfo", Json.obj()))
      .response(toBody[LightningNodeInfo])
    r.send(backend)
  }
  override def listInvoices(
      listInvoicesRequest: ListInvoicesRequest
  ): Future[Response[Either[LightningRequestError, Invoices]]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("listinvoices", Json.toJson(listInvoicesRequest)))
      .response(toBody[Invoices])
    r.send(backend)

  }

  override def pay(pay: Pay): Future[Response[Either[LightningRequestError, Payment]]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("pay", Json.toJson(pay)))
      .response(toBody[Payment])

    r.send(backend)
  }

  override def waitAnyInvoice(
      w: WaitAnyInvoice
  ): Future[Response[Either[LightningRequestError, ListInvoice]]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("waitanyinvoice", Json.toJson(w)))
      .response(toBody[ListInvoice])

    r.send(backend)
  }

  override def listOffers(
      req: LightningListOffersRequest
  ): Future[Response[Either[LightningRequestError, Seq[LightningOffer]]]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("listoffers", Json.toJson(req)))
      .response(toBody[LightningOffers].mapRight(_.offers))
    r.send(backend)
  }

  def onEvent(event: ServerSentEvent): Either[String, SparkWalletSSE] = {
    (event.eventType, event.data) match {
      case (Some(event), Some(data)) =>
        event match {
          case "btcusd" => Right(BtcUsd(BigDecimal(data)))
          case "inv-paid" =>
            Json.parse(data).validate[ListInvoice] match {
              case JsSuccess(value, _) => Right(InvoicePaid(value))
              case JsError(errors) => Left(errors.mkString(","))
            }
          case other => Left(other)
        }
      case noEventTypeOrData => Left(event.toString())
    }
  }

  def stream: Future[Response[Either[String, Source[ServerSentEvent, Any]]]] =
    base
      .get(uri"${config.baseUrl.replace("/rpc", "/stream")}")
      .response(
        asStreamUnsafe(AkkaStreams)
          .mapRight(_.via(AkkaHttpServerSentEvents.parse))
      )
      .send(backend)

  override def decodePay(b: Bolt11): Future[Response[Either[LightningRequestError, DecodePay]]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("decodepay", Json.obj("bolt11" -> b.toString)))
      .response(toBody[DecodePay])
    r.send(backend)
  }

  override def createOffer(
      offerRequest: LightningOfferRequest
  ): Future[Response[Either[LightningRequestError, LightningOffer]]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("offer", Json.toJson(offerRequest)))
      .response(toBody[LightningOffer])
    r.send(backend)
  }

  override def invoice(
      inv: LightningInvoice
  ): Future[Response[Either[LightningRequestError, LightningCreateInvoice]]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("offer", Json.toJson(inv)))
      .response(toBody[LightningCreateInvoice])
    r.send(backend)
  }
}

object SparkLightningWalletService {
  trait SparkWalletSSE
  case class BtcUsd(btcusd: BigDecimal) extends SparkWalletSSE
  case class InvoicePaid(listPay: ListInvoice) extends SparkWalletSSE

}
