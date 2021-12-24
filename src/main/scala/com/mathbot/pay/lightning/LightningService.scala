package com.mathbot.pay.lightning

import com.mathbot.pay.lightning.url.{CreateInvoiceWithDescriptionHash, InvoiceWithDescriptionHash}

import scala.concurrent.{ExecutionContext, Future}
import sttp.client3.Response

trait LightningService {
  def invoice(inv: LightningInvoice): Future[Response[Either[LightningRequestError, LightningCreateInvoice]]]
  def listInvoices(
      l: ListInvoicesRequest = ListInvoicesRequest(label = None, invstring = None, payment_hash = None)
  ): Future[Response[Either[LightningRequestError, Invoices]]]
  def getInvoice(
      payment_hash: String
  )(implicit ec: ExecutionContext): Future[Either[LightningRequestError, ListInvoice]] = {
    for {
      inv <- listInvoices(ListInvoicesRequest(payment_hash = Some(payment_hash))).map(
        _.body.flatMap(
          _.invoices.find(_.payment_hash == payment_hash).toRight(LightningRequestError(ErrorMsg(404, "not found")))
        )
      )
    } yield inv

  }

  def getInvoice(
      bolt11: Bolt11
  )(implicit ec: ExecutionContext): Future[Either[LightningRequestError, Option[ListInvoice]]] =
    listInvoices(ListInvoicesRequest(invstring = Some(bolt11.bolt11)))
      .map(_.body.map(_.invoices.find(_.bolt11.contains(bolt11))))
  def listPays(l: ListPaysRequest = ListPaysRequest(None, None)): Future[Response[Either[LightningRequestError, Pays]]]
  def getInfo: Future[Response[Either[LightningRequestError, LightningNodeInfo]]]
  def pay(pay: Pay): Future[Response[Either[LightningRequestError, Payment]]]
  def decodePay(r: Bolt11): Future[Response[Either[LightningRequestError, DecodePay]]]
  def createOffer(offerRequest: LightningOfferRequest): Future[Response[Either[LightningRequestError, LightningOffer]]]
  def listOffers(r: LightningListOffersRequest): Future[Response[Either[LightningRequestError, Seq[LightningOffer]]]]

  /**
   * Note bolt11 will differ from the listInvoices since they create the invoice and
   * override the description hash
   * @param i
   * @return
   */
  def invoiceWithDescriptionHash(
      i: InvoiceWithDescriptionHash
  ): Future[Response[Either[LightningRequestError, CreateInvoiceWithDescriptionHash]]]
  //  def waitAnyInvoice(w: WaitAnyInvoice): Future[Response[Either[LightningRequestError, ListInvoice]]]
}
