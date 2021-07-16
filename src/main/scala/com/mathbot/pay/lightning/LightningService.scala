package com.mathbot.pay.lightning

import scala.concurrent.Future

trait LightningService {
  def listPays(l: ListPaysRequest = ListPaysRequest(None, None)): Future[Either[LightningRequestError, Pays]]
  def getInfo: Future[Either[LightningRequestError, LightningNodeInfo]]
  def pay(pay: Pay): Future[Either[LightningRequestError, Payment]]
  def listInvoices(
      l: ListInvoicesRequest = ListInvoicesRequest(None, None, None)
  ): Future[Either[LightningRequestError, Invoices]]

  def waitAnyInvoice(w: WaitAnyInvoice): Future[Either[LightningRequestError, ListInvoice]]

  def listOffers(r: LightningListOffersRequest): Future[Either[LightningRequestError, Seq[LightningOffer]]]

  def decodePay(r: Bolt11): Future[Either[LightningRequestError, DecodePay]]

  def createOffer(offerRequest: LightningOfferRequest): Future[Either[LightningRequestError, LightningOffer]]

  def invoice(inv: LightningInvoice): Future[Either[LightningRequestError, LightningCreateInvoice]]
}
