package com.mathbot.pay.lightning

import scala.concurrent.Future

trait LightningService {
  def listPays(l: ListPaysRequest = ListPaysRequest(None, None)): Future[Either[LightningRequestError, Pays]]
  def getInfo: Future[Either[LightningRequestError, InfoResponse]]
  def pay(pay: Pay): Future[Either[LightningRequestError, Payment]]
  def listInvoices(
      l: ListInvoicesRequest = ListInvoicesRequest(None, None, None)
  ): Future[Either[LightningRequestError, Invoices]]

  def waitAnyInvoice(w: WaitAnyInvoice): Future[Either[LightningRequestError, ListInvoice]]
}
