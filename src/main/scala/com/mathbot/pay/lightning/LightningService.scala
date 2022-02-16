package com.mathbot.pay.lightning

import akka.http.scaladsl.util.FastFuture
import com.mathbot.pay.lightning.url.{CreateInvoiceWithDescriptionHash, InvoiceWithDescriptionHash}
import com.typesafe.scalalogging.StrictLogging
import sttp.client3.Response

import scala.concurrent.{ExecutionContext, Future}

trait LightningService extends StrictLogging {

  //////////////////// INVOICES ////////////////////
  def invoice(inv: LightningInvoice): Future[Response[Either[LightningRequestError, LightningCreateInvoice]]]

  def invoiceReturningListInvoice(
      inv: LightningInvoice
  )(implicit ec: ExecutionContext): Future[Response[Either[LightningRequestError, ListInvoice]]] =
    for {
      i <- invoice(inv)
      r <- i.body match {
        case Right(l) => getInvoiceByPaymentHash(l.payment_hash)
        case Left(e) => FastFuture.successful(Response.ok[Either[LightningRequestError, ListInvoice]](Left(e)))
      }
    } yield r
  def listInvoices(
      l: ListInvoicesRequest = ListInvoicesRequest()
  ): Future[Response[Either[LightningRequestError, Invoices]]]
  def getInvoiceByPaymentHash(
      payment_hash: String
  )(implicit ec: ExecutionContext): Future[Response[Either[LightningRequestError, ListInvoice]]] = {
    for {
      inv <- listInvoices(ListInvoicesRequest(payment_hash = Some(payment_hash))).map(
        r =>
          r.copy(
            body = r.body.flatMap(
              _.invoices.find(_.payment_hash == payment_hash).toRight(LightningRequestError(ErrorMsg(404, "not found")))
            )
        )
      )
    } yield inv

  }

  def getInvoice(
      bolt11: Bolt11
  )(implicit ec: ExecutionContext): Future[Either[LightningRequestError, Option[ListInvoice]]] =
    listInvoices(ListInvoicesRequest(invstring = Some(bolt11.bolt11)))
      .map(_.body.map(_.invoices.find(_.bolt11.contains(bolt11))))

  def getInvoiceByLabel(
      label: String
  )(implicit ec: ExecutionContext): Future[Either[LightningRequestError, Option[ListInvoice]]] =
    listInvoices(ListInvoicesRequest(label = Some(label)))
      .map(_.body.map(_.invoices.find(_.label == label)))

  /**
   * Note bolt11 will differ from the listInvoices since they create the invoice and
   * override the description hash
   * @param i
   * @return
   */
  def invoiceWithDescriptionHash(
      i: InvoiceWithDescriptionHash
  ): Future[Response[Either[LightningRequestError, CreateInvoiceWithDescriptionHash]]]

  /**
   * https://lightning.readthedocs.io/lightning-waitinvoice.7.html
   * @param label
   * @return
   */
  def waitInvoice(label: String): Future[Response[Either[LightningRequestError, ListInvoice]]]

  def waitAnyInvoice(w: WaitAnyInvoice): Future[Response[Either[LightningRequestError, ListInvoice]]]

  //////////////////// PAYMENTS ////////////////////

  /**
   * description=List result of payment {bolt11} or {payment_hash}, or all
   *      verbose=Covers old payments (failed and succeeded) and current ones.
   */
  def listPays(
      l: ListPaysRequest = ListPaysRequest(bolt11 = None, payment_hash = None, status = None)
  ): Future[Response[Either[LightningRequestError, Pays]]]
  def pay(pay: Pay): Future[Response[Either[LightningRequestError, Payment]]]
  def decodePay(bolt11: Bolt11): Future[Response[Either[LightningRequestError, DecodePay]]]

  def getPayByBolt11(
      bolt11: Bolt11
  )(implicit ec: ExecutionContext): Future[Response[Either[LightningRequestError, ListPay]]] =
    listPays(ListPaysRequest(bolt11)).map(r => {
      r.copy(
        body = r.body.flatMap(
          _.pays.find(_.bolt11.contains(bolt11)).toRight(LightningRequestError(ErrorMsg(404, "not found")))
        )
      )
    })

  //////////////////// OFFERS ////////////////////
  def offer(offerRequest: LightningOfferRequest): Future[Response[Either[LightningRequestError, LightningOffer]]]
  def listOffers(r: LightningListOffersRequest): Future[Response[Either[LightningRequestError, LightningOffers]]]

  def getOfferByOfferId(
      id: String
  )(implicit ec: ExecutionContext): Future[Response[Either[LightningRequestError, LightningOffer]]] =
    listOffers(LightningListOffersRequest(offer_id = Some(id), only_active = None)).map(r => {
      val a = r.body.flatMap(_.offers.find(_.offer_id == id).toRight(LightningRequestError(ErrorMsg(404, "not found"))))
      r.copy(
        body = a
      )
    })

  //////////////////// CONFIG ////////////////////
  def getInfo: Future[Response[Either[LightningRequestError, LightningNodeInfo]]]
}
