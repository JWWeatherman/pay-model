package com.mathbot.pay.lightning.spark

import akka.http.scaladsl.model.ws.Message
import akka.http.scaladsl.util.FastFuture
import akka.stream.scaladsl.{Flow, Source}
import akka.util.ByteString
import com.mathbot.pay.lightning._
import play.api.libs.json.{JsValue, Json, Reads}
import sttp.client
import sttp.client.playJson.asJson
import sttp.client.{asStreamAlways, asStringAlways, basicRequest, ResponseAs, SttpBackend, UriContext}
import sttp.model.MediaType

import scala.concurrent.{ExecutionContext, Future}

class SparkLightningWalletService(config: SparkLightningWalletServiceConfig)(
    implicit ec: ExecutionContext,
    backend: SttpBackend[Future,
                         Source[ByteString, Any],
                         ({
                           type λ[γ$3$] = Flow[Message, Message, γ$3$]
                         })#λ]
) extends LightningService {

  private val base = basicRequest
    .headers(Map("X-Access" -> config.accessKey))
    .contentType(MediaType.ApplicationJson)

  private def toBody[T](implicit reads: Reads[T]): ResponseAs[Either[LightningRequestError, T], Nothing] =
    asJson[T].mapLeft(err => LightningRequestError(ErrorMsg(500, s"Bad response $err"), None))

  private def makeBody(method: String, params: JsValue) = {
    Json.obj("method" -> method, "params" -> params).toString()
  }

  override def listPays(
      l: ListPaysRequest = ListPaysRequest(None, None)
  ): Future[Either[LightningRequestError, Pays]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("listpays", Json.toJson(l)))
      .response(toBody[Pays])

    r.send().map(_.body)

  }

  override def getInfo: Future[Either[LightningRequestError, InfoResponse]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("getinfo", Json.obj()))
      .response(toBody[InfoResponse])
    r.send()
      .map(_.body)
  }
  override def listInvoices(
      listInvoicesRequest: ListInvoicesRequest
  ): Future[Either[LightningRequestError, Invoices]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("listinvoices", Json.toJson(listInvoicesRequest)))
      .response(toBody[Invoices])
    r.send().map(_.body)

  }

  override def pay(pay: Pay): Future[Either[LightningRequestError, Payment]] = ???

  override def waitAnyInvoice(w: WaitAnyInvoice): Future[Either[LightningRequestError, ListInvoice]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("waitanyinvoice", Json.toJson(w)))
      .response(toBody[ListInvoice])

    r.send().map(_.body)
  }

  override def listOffers(
      req: LightningListOffersRequest
  ): Future[Either[LightningRequestError, Seq[LightningOffer]]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("listoffers", Json.toJson(req)))
      .response(toBody[LightningOffers].mapRight(_.offers))
    r.send().map(_.body)
  }

  def stream: Future[client.Response[Source[ByteString, Any]]] = {
    base
      .get(uri"${config.baseUrl.replace("/rpc", "/stream")}")
      .response(asStreamAlways[Source[ByteString, Any]])
      .send()
  }

  override def decodePay(b: Bolt11): Future[Either[LightningRequestError, DecodePay]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("decodepay", Json.obj("bolt11" -> b.toString)))
      .response(toBody[DecodePay])
    r.send().map(_.body)
  }

  override def createOffer(
      offerRequest: LightningOfferRequest
  ): Future[Either[LightningRequestError, LightningOffer]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("offer", Json.toJson(offerRequest)))
      .response(toBody[LightningOffer])
    r.send().map(_.body)
  }
}
