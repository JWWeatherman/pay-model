package com.mathbot.pay.lightning

import akka.stream.scaladsl.Source
import com.mathbot.pay.lightning.url.{CreateInvoiceWithDescriptionHash, InvoiceWithDescriptionHash}
import com.typesafe.scalalogging.StrictLogging
import play.api.libs.json.JsValue

import scala.concurrent.{ExecutionContext, Future}
import sttp.client3.Response

import scala.concurrent.duration.FiniteDuration

trait LightningService extends StrictLogging {
  def invoice(inv: LightningInvoice): Future[Response[Either[LightningRequestError, LightningCreateInvoice]]]
  def listInvoices(
      l: ListInvoicesRequest = ListInvoicesRequest(label = None, invstring = None, payment_hash = None)
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
   * description=List result of payment {bolt11} or {payment_hash}, or all
   *      verbose=Covers old payments (failed and succeeded) and current ones.
   */
  def listPays(l: ListPaysRequest = ListPaysRequest(None, None)): Future[Response[Either[LightningRequestError, Pays]]]
  def getInfo: Future[Response[Either[LightningRequestError, LightningNodeInfo]]]
  def pay(pay: Pay): Future[Response[Either[LightningRequestError, Payment]]]
  def decodePay(r: Bolt11): Future[Response[Either[LightningRequestError, DecodePay]]]
  def createOffer(offerRequest: LightningOfferRequest): Future[Response[Either[LightningRequestError, LightningOffer]]]
  def listOffers(r: LightningListOffersRequest): Future[Response[Either[LightningRequestError, LightningOffers]]]

  /**
   * Note bolt11 will differ from the listInvoices since they create the invoice and
   * override the description hash
   * @param i
   * @return
   */
  def invoiceWithDescriptionHash(
      i: InvoiceWithDescriptionHash
  ): Future[Response[Either[LightningRequestError, CreateInvoiceWithDescriptionHash]]]

  def waitAnyInvoice(w: WaitAnyInvoice): Future[Response[Either[LightningRequestError, ListInvoice]]]

  /**
   * https://lightning.readthedocs.io/lightning-waitinvoice.7.html
   * @param label
   * @return
   */
  def waitInvoice(label: String): Future[Response[Either[LightningRequestError, ListInvoice]]]

//  def waitSendPay[T](payment_hash: String,
//                     timeout: Option[FiniteDuration]): Future[Response[Either[LightningRequestError, T]]]
}
