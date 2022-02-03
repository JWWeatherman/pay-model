package com.mathbot.pay.lightning

import com.github.dwickern.macros.NameOf.nameOf
import com.mathbot.pay.lightning.url.{CreateInvoiceWithDescriptionHash, InvoiceWithDescriptionHash}
import play.api.libs.json.{JsObject, JsValue, Json, Reads}
import sttp.capabilities.akka.AkkaStreams

import scala.concurrent.Future

trait RpcLightningService extends LightningService {

  import sttp.client3._
  import sttp.client3.playJson._
  def base: RequestT[Empty, Either[String, String], Any]
  def backend: SttpBackend[Future, AkkaStreams]
  def baseUrl: String

  override def listPays(
      l: ListPaysRequest = ListPaysRequest(bolt11 = None, payment_hash = None)
  ) = {
    base
      .post(uri"$baseUrl")
      .body(makeBody("listpays", Json.toJson(l)))
      .response(toBody[Pays])
      .send(backend)

  }

  override def getInfo: Future[Response[Either[LightningRequestError, LightningNodeInfo]]] = {
    val r = base
      .post(uri"$baseUrl")
      .body(makeBody("getinfo", Json.obj()))
      .response(toBody[LightningNodeInfo])

    r.send(backend)
  }
  override def listInvoices(
      listInvoicesRequest: ListInvoicesRequest
  ): Future[Response[Either[LightningRequestError, Invoices]]] = {
    val r = base
      .post(uri"$baseUrl")
      .body(makeBody("listinvoices", Json.toJson(listInvoicesRequest)))
      .response(toBody[Invoices])
    r.send(backend)

  }

  override def pay(pay: Pay): Future[Response[Either[LightningRequestError, Payment]]] = {
    val r = base
      .post(uri"$baseUrl")
      .body(makeBody(nameOf(pay), Json.toJson(pay)))
      .response(toBody[Payment])

    r.send(backend)
  }

  override def listOffers(
      req: LightningListOffersRequest
  ): Future[Response[Either[LightningRequestError, LightningOffers]]] = {
    val r = base
      .post(uri"$baseUrl")
      .body(makeBody("listoffers", Json.toJson(req)))
      .response(toBody[LightningOffers])
    r.send(backend)
  }

  override def decodePay(b: Bolt11): Future[Response[Either[LightningRequestError, DecodePay]]] = {
    val r = base
      .post(uri"$baseUrl")
      .body(makeBody("decodepay", Json.obj("bolt11" -> b.toString)))
      .response(toBody[DecodePay])
    r.send(backend)
  }

  override def createOffer(
      offerRequest: LightningOfferRequest
  ): Future[Response[Either[LightningRequestError, LightningOffer]]] = {
    val r = base
      .post(uri"$baseUrl")
      .body(makeBody("offer", Json.toJson(offerRequest)))
      .response(toBody[LightningOffer])
    r.send(backend)
  }

  override def invoice(
      inv: LightningInvoice
  ): Future[Response[Either[LightningRequestError, LightningCreateInvoice]]] = {
    val r = base
      .post(uri"$baseUrl")
      .body(makeBody(nameOf(invoice _), Json.toJson(inv)))
      .response(toBody[LightningCreateInvoice])
    r.send(backend)
  }

  def invoiceWithDescriptionHash(
      i: InvoiceWithDescriptionHash
  ): Future[Response[Either[LightningRequestError, CreateInvoiceWithDescriptionHash]]] = {
    val r = base
      .post(uri"$baseUrl")
      .body(makeBody("invoicewithdescriptionhash", Json.toJson(i)))
      .response(toBody[CreateInvoiceWithDescriptionHash])
    r.send(backend)
  }

  def waitAnyInvoice(w: WaitAnyInvoice): Future[Response[Either[LightningRequestError, ListInvoice]]] = {
    val r = base
      .post(uri"$baseUrl")
      .body(makeBody("waitanyinvoice", Json.toJson(w)))
      .response(toBody[ListInvoice])
    r.send(backend)
  }

  /**
   * https://lightning.readthedocs.io/lightning-waitinvoice.7.html
   *
   * @param label
   * @return
   */
  override def waitInvoice(label: String): Future[Response[Either[LightningRequestError, ListInvoice]]] =
    base
      .post(uri"$baseUrl")
      .body(makeBody("waitinvoice", Json.toJson(label)))
      .response(toBody[ListInvoice])
      .send(backend)
  def toBody[T](implicit reads: Reads[T]): ResponseAs[Either[LightningRequestError, T], Any] =
    asJson[T].mapLeft(err => LightningRequestError(ErrorMsg(500, s"Bad response $err")))

  def makeBody(method: String, params: JsValue): JsObject =
    Json.obj("method" -> method, "params" -> params)
}
