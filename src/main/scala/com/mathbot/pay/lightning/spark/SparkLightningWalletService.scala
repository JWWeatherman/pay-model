package com.mathbot.pay.lightning.spark

import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.mathbot.pay.lightning._
import play.api.libs.json.{JsValue, Json, Reads}
import sttp.capabilities.akka.AkkaStreams
import sttp.client3.SttpBackend

import scala.concurrent.Future

class SparkLightningWalletService(config: SparkLightningWalletServiceConfig, backend: SttpBackend[Future, AkkaStreams])
    extends LightningService {

  import sttp.client3._
  import sttp.client3.playJson._

  private val base = basicRequest
    .headers(Map("X-Access" -> config.accessKey.value))

  private def toBody[T](implicit reads: Reads[T]): ResponseAs[Either[LightningRequestError, T], Any] =
    asJson[T].mapLeft(err => LightningRequestError(ErrorMsg(500, s"Bad response $err"), None))

  private def makeBody(method: String, params: JsValue) =
    Json.obj("method" -> method, "params" -> params)

  override def listPays(
      l: ListPaysRequest = ListPaysRequest(None, None)
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

  override def pay(pay: Pay): Future[Response[Either[LightningRequestError, Payment]]] = ???

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

  def stream: Future[Response[Either[String, Source[ByteString, Any]]]] =
    base
      .get(uri"${config.baseUrl.replace("/rpc", "/stream")}")
      .response(asStreamUnsafe(AkkaStreams))
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
