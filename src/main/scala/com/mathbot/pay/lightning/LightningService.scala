package com.mathbot.pay.lightning

import com.mathbot.pay.lightning.url.{CreateInvoiceWithDescriptionHash, InvoiceWithDescriptionHash}

import scala.concurrent.Future
import sttp.client3.Response

trait LightningService {
  def listPays(l: ListPaysRequest = ListPaysRequest(None, None)): Future[Response[Either[LightningRequestError, Pays]]]
  def getInfo: Future[Response[Either[LightningRequestError, LightningNodeInfo]]]
  def pay(pay: Pay): Future[Response[Either[LightningRequestError, Payment]]]
  def listInvoices(
      l: ListInvoicesRequest = ListInvoicesRequest(None, None, None)
  ): Future[Response[Either[LightningRequestError, Invoices]]]

  def waitAnyInvoice(w: WaitAnyInvoice): Future[Response[Either[LightningRequestError, ListInvoice]]]

  def listOffers(r: LightningListOffersRequest): Future[Response[Either[LightningRequestError, Seq[LightningOffer]]]]

  def decodePay(r: Bolt11): Future[Response[Either[LightningRequestError, DecodePay]]]

  def createOffer(offerRequest: LightningOfferRequest): Future[Response[Either[LightningRequestError, LightningOffer]]]

  def invoice(inv: LightningInvoice): Future[Response[Either[LightningRequestError, LightningCreateInvoice]]]

  /**
   * Note bolt11 will differ from the listInvoices since they create the invoice and
   * override the description hash
   * @param i
   * @return
   */
  def invoiceWithDescriptionHash(
      i: InvoiceWithDescriptionHash
  ): Future[Response[Either[LightningRequestError, CreateInvoiceWithDescriptionHash]]]
}
