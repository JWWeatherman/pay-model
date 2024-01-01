package com.mathbot.pay.lightning

import com.mathbot.pay.lightning.url.{CreateInvoiceWithDescriptionHash, InvoiceWithDescriptionHash}
import play.api.libs.json.{Json, Reads}
import sttp.client3.Response

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.{ExecutionContext, Future, Promise}

class LightningStreamService(lightningStream: LightningStream)(implicit val ec: ExecutionContext)
    extends LightningService {

  def listPays(
      l: ListPaysRequest = ListPaysRequest()
  ): Future[Response[Either[LightningRequestError, Pays]]] = wrap[Pays](l)

  def waitAnyInvoice(w: WaitAnyInvoice): Future[Response[Either[LightningRequestError, ListInvoice]]] =
    wrap[ListInvoice](w)

  def listInvoices(
      l: ListInvoicesRequest = ListInvoicesRequest(label = None, invstring = None, payment_hash = None)
  ): Future[Response[Either[LightningRequestError, Invoices]]] =
    wrap[Invoices](l)

  def getInfo: Future[Response[Either[LightningRequestError, LightningNodeInfo]]] =
    wrap[LightningNodeInfo](LightningGetInfoRequest())

  def decodePay(bolt11: Bolt11): Future[Response[Either[LightningRequestError, DecodePay]]] =
    wrap[DecodePay](DecodePayRequest(bolt11))

  def pay(pay: Pay): Future[Response[Either[LightningRequestError, Payment]]] =
    wrap[Payment](pay)

  def listPay(req: ListPaysRequest): Future[Response[Either[LightningRequestError, Pays]]] =
    wrap[Pays](req)

  def listOffers(
      r: LightningListOffersRequest
  ): Future[Response[Either[LightningRequestError, LightningOffers]]] =
    wrap[LightningOffers](r)

  def offer(
      offerRequest: LightningOfferRequest
  ): Future[Response[Either[LightningRequestError, LightningOffer]]] =
    wrap[LightningOffer](offerRequest)

  def invoice(
      inv: LightningInvoice
  ): Future[Response[Either[LightningRequestError, LightningCreateInvoice]]] =
    wrap[LightningCreateInvoice](inv)

  def invoiceWithDescriptionHash(
      i: InvoiceWithDescriptionHash
  ): Future[Response[Either[LightningRequestError, CreateInvoiceWithDescriptionHash]]] =
    wrap[CreateInvoiceWithDescriptionHash](i)

  /**
   * https://lightning.readthedocs.io/lightning-waitinvoice.7.html
   *
   * @param label
   * @return
   */
  override def waitInvoice(label: String): Future[Response[Either[LightningRequestError, ListInvoice]]] =
    wrap[ListInvoice](WaitInvoice(label))

  private def wrap[T](
      r: LightningJson
  )(implicit readsT: Reads[T]): Future[Response[Either[LightningRequestError, T]]] = {
    val p = Promise[Response[Either[LightningRequestError, T]]]()
    lightningStream.enqueue(r) {
      case t if t.\("result").isDefined && t("result").validate[T].isSuccess =>
        p.success(Response.ok(Right(t("result").as[T])))
      case t if t.\("error").isDefined && t("error").validate[LightningRequestError].isSuccess =>
        p.success(Response.ok(Left(t("error").as[LightningRequestError])))
      case other =>
        p.success(Response.ok(Left(LightningRequestError(code = 500, message = Json.stringify(other)))))
    }
    p.future
  }
}
