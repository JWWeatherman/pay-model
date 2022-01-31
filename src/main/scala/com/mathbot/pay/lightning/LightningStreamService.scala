package com.mathbot.pay.lightning

import com.mathbot.pay.lightning.url.{CreateInvoiceWithDescriptionHash, InvoiceWithDescriptionHash}
import play.api.libs.json.Reads
import sttp.client3.Response

import scala.concurrent.{Future, Promise}

class LightningStreamService(lightningStream: LightningStream) extends LightningService {


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

   def createOffer(
                            offerRequest: LightningOfferRequest
                          ): Future[Response[Either[LightningRequestError, LightningOffer]]] = {
    wrap[LightningOffer](offerRequest)
  }

   def invoice(
                        inv: LightningInvoice
                      ): Future[Response[Either[LightningRequestError, LightningCreateInvoice]]] =
    wrap[LightningCreateInvoice](inv)

  // TODO:
   def invoiceWithDescriptionHash(
                                           i: InvoiceWithDescriptionHash
                                         ): Future[Response[Either[LightningRequestError, CreateInvoiceWithDescriptionHash]]] = ???

  private def wrap[T](r: LightningJson)(implicit readsT: Reads[T]): Future[Response[Either[LightningRequestError, T]]] = {
    val p = Promise[Response[Either[LightningRequestError, T]]]()
    lightningStream.enqueue(r) {
      case t if t("response").validate[T].isSuccess =>
        // cache listpays
        p.success(Response.ok(Right(t("response").as[T])))
      case t if t.validate[LightningRequestError].isSuccess =>
        p.success(Response.ok(Left(t.as[LightningRequestError])))
      case other =>
        p.success(Response.ok(Left(LightningRequestError(ErrorMsg(500, s"Unknown response $other")))))
    }
    p.future
  }
}
