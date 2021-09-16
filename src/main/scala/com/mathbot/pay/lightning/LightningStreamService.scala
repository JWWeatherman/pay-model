package com.mathbot.pay.lightning

import com.mathbot.pay.lightning.url.{CreateInvoiceWithDescriptionHash, InvoiceWithDescriptionHash}
import org.slf4j.{Logger, LoggerFactory}
import sttp.client3.Response

import scala.concurrent.{Future, Promise}

class LightningStreamService(lightningStream: LightningStream) extends LightningService {

  val logger: Logger = LoggerFactory.getLogger("LightningStreamService")
  override def listPays(
      l: ListPaysRequest = ListPaysRequest()
  ): Future[Response[Either[LightningRequestError, Pays]]] = {
    val p = Promise[Response[Either[LightningRequestError, Pays]]]()
    lightningStream.enqueue(l) {
      case l: ListPaysResponse =>
        // cache listpays
        p.success(Response.ok(Right(l.result)))
      case l: LightningRequestError =>
        p.success(Response.ok(Left(l)))
    }
    p.future
  }

  def waitAnyInvoice(w: WaitAnyInvoice): Future[Response[Either[LightningRequestError, ListInvoice]]] = {
    val p = Promise[Response[Either[LightningRequestError, ListInvoice]]]()
    lightningStream.enqueue(w) {
      case l: LightningRequestError =>
        p.success(Response.ok(Left(l)))
      case l: ListInvoiceResponse => p.success(Response.ok(Right(l.result)))

    }
    p.future
  }

  override def listInvoices(
      l: ListInvoicesRequest = ListInvoicesRequest(label = None, invstring = None, payment_hash = None)
  ): Future[Response[Either[LightningRequestError, Invoices]]] = {
    val p = Promise[Response[Either[LightningRequestError, Invoices]]]()
    lightningStream.enqueue(l) {
      case l: ListInvoicesResponse =>
        p.success(Response.ok(Right(l.result)))
      case l: LightningRequestError =>
        p.success(Response.ok(Left(l)))
    }
    p.future
  }

  def getInfo: Future[Response[Either[LightningRequestError, LightningNodeInfo]]] = {
    val p = Promise[Response[Either[LightningRequestError, LightningNodeInfo]]]()
    lightningStream
      .enqueue(LightningGetInfoRequest()) {
        case g: GetInfoResponse => p.success(Response.ok(Right(g.result)))
        case other =>
          p.failure(
            new RuntimeException(s"Failed getinfo from lighting client response = $other")
          )
      }
    p.future
  }
  override def decodePay(bolt11: Bolt11): Future[Response[Either[LightningRequestError, DecodePay]]] = {
    val p = Promise[Response[Either[LightningRequestError, DecodePay]]]()
    lightningStream.enqueue(DecodePayRequest(bolt11)) {
      case err: LightningRequestError =>
        p.success(Response.ok(Left(err)))
      case d: DecodePayResponse => p.success(Response.ok(Right(d.result)))
    }
    p.future
  }

  def pay(pay: Pay): Future[Response[Either[LightningRequestError, Payment]]] = {
    val p = Promise[Response[Either[LightningRequestError, Payment]]]()
    lightningStream.enqueue(pay) {
      case payResponse: PayResponse =>
        p.success(Response.ok(Right(payResponse.result)))
      case err: LightningRequestError =>
        p.success(Response.ok(Left(err)))
    }
    p.future
  }

  def listPay(req: ListPaysRequest): Future[Response[Either[LightningRequestError, Pays]]] = {
    val p = Promise[Response[Either[LightningRequestError, Pays]]]()
    lightningStream.enqueue(req) {
      case err: LightningRequestError =>
        p.success(Response.ok(Left(err)))
      case lp: ListPaysResponse =>
        p.success(Response.ok(Right(lp.result)))
    }
    p.future
  }

  override def listOffers(
      r: LightningListOffersRequest
  ): Future[Response[Either[LightningRequestError, Seq[LightningOffer]]]] = {
    val p = Promise[Response[Either[LightningRequestError, Seq[LightningOffer]]]]()
    lightningStream.enqueue(r) {
      case err: LightningRequestError =>
        p.success(Response.ok(Left(err)))
      case lp: LightningListOffersResponse =>
        p.success(Response.ok(Right(lp.result.offers)))
    }
    p.future

  }

  override def createOffer(
      offerRequest: LightningOfferRequest
  ): Future[Response[Either[LightningRequestError, LightningOffer]]] = {
    val p = Promise[Response[Either[LightningRequestError, LightningOffer]]]()
    lightningStream.enqueue(offerRequest) {
      case err: LightningRequestError =>
        p.success(Response.ok(Left(err)))
      case lp: LightningOffer =>
        p.success(Response.ok(Right(lp)))
    }
    p.future
  }

  override def invoice(
      inv: LightningInvoice
  ): Future[Response[Either[LightningRequestError, LightningCreateInvoice]]] = {
    val p = Promise[Response[Either[LightningRequestError, LightningCreateInvoice]]]()
    lightningStream.enqueue(inv) {
      case err: LightningRequestError =>
        p.success(Response.ok(Left(err)))
      case lp: LightningCreateInvoiceResponse =>
        p.success(Response.ok(Right(lp.result)))
    }
    p.future
  }

  override def invoiceWithDescriptionHash(
      i: InvoiceWithDescriptionHash
  ): Future[Response[Either[LightningRequestError, CreateInvoiceWithDescriptionHash]]] = ???
}
