package com.mathbot.pay.lightning

import org.slf4j.LoggerFactory

import scala.concurrent.{Future, Promise}

class LightningStreamService(lightningStream: LightningStream) extends LightningService {

  val logger = LoggerFactory.getLogger("LightningStreamService")
  override def listPays(
      l: ListPaysRequest = ListPaysRequest(None, None)
  ): Future[Either[LightningRequestError, Pays]] = {
    val p = Promise[Either[LightningRequestError, Pays]]()
    lightningStream.enqueue(l) {
      case l: ListPaysResponse =>
        // cache listpays
        p.success(Right(l.result))
      case l: LightningRequestError =>
        p.success(Left(l))
    }
    p.future
  }

  def waitAnyInvoice(w: WaitAnyInvoice): Future[Either[LightningRequestError, ListInvoice]] = {
    val p = Promise[Either[LightningRequestError, ListInvoice]]()
    lightningStream.enqueue(w) {
      case l: LightningRequestError =>
        p.success(Left(l))
      case l: ListInvoiceResponse => p.success(Right(l.result))

    }
    p.future
  }

  override def listInvoices(
      l: ListInvoicesRequest = ListInvoicesRequest(None, None, None)
  ): Future[Either[LightningRequestError, Invoices]] = {
    val p = Promise[Either[LightningRequestError, Invoices]]()
    lightningStream.enqueue(l) {
      case l: ListInvoicesResponse =>
        p.success(Right(l.result))
      case l: LightningRequestError =>
        p.success(Left(l))
    }
    p.future
  }

  def getInfo: Future[Either[LightningRequestError, LightningNodeInfo]] = {
    val p = Promise[Either[LightningRequestError, LightningNodeInfo]]()
    lightningStream
      .enqueue(LightningGetInfoRequest()) {
        case g: GetInfoResponse => p.success(Right(g.result))
        case other =>
          p.failure(
            new RuntimeException(s"Failed getinfo from lighting client response = $other")
          )
      }
    p.future
  }
  override def decodePay(bolt11: Bolt11): Future[Either[LightningRequestError, DecodePay]] = {
    val p = Promise[Either[LightningRequestError, DecodePay]]()
    lightningStream.enqueue(DecodePayRequest(bolt11)) {
      case err: LightningRequestError =>
        p.success(Left(err))
      case d: DecodePayResponse => p.success(Right(d.result))
    }
    p.future
  }

  def pay(pay: Pay): Future[Either[LightningRequestError, Payment]] = {
    val p = Promise[Either[LightningRequestError, Payment]]()
    lightningStream.enqueue(pay) {
      case payResponse: PayResponse =>
        p.success(Right(payResponse.result))
      case err: LightningRequestError =>
        p.success(Left(err))
    }
    p.future
  }

  def listPay(req: ListPaysRequest): Future[Either[LightningRequestError, Pays]] = {
    val p = Promise[Either[LightningRequestError, Pays]]()
    lightningStream.enqueue(req) {
      case err: LightningRequestError =>
        p.success(Left(err))
      case lp: ListPaysResponse =>
        p.success(Right(lp.result))
    }
    p.future
  }

  override def listOffers(r: LightningListOffersRequest): Future[Either[LightningRequestError, Seq[LightningOffer]]] = {
    val p = Promise[Either[LightningRequestError, Seq[LightningOffer]]]()
    lightningStream.enqueue(r) {
      case err: LightningRequestError =>
        p.success(Left(err))
      case lp: LightningListOffersResponse =>
        p.success(Right(lp.result.offers))
    }
    p.future

  }

  override def createOffer(
      offerRequest: LightningOfferRequest
  ): Future[Either[LightningRequestError, LightningOffer]] = {
    val p = Promise[Either[LightningRequestError, LightningOffer]]()
    lightningStream.enqueue(offerRequest) {
      case err: LightningRequestError =>
        p.success(Left(err))
      case lp: LightningOffer =>
        p.success(Right(lp))
    }
    p.future
  }

  override def invoice(inv: LightningInvoice): Future[Either[LightningRequestError, LightningCreateInvoice]] = {
    val p = Promise[Either[LightningRequestError, LightningCreateInvoice]]()
    lightningStream.enqueue(inv) {
      case err: LightningRequestError =>
        p.success(Left(err))
      case lp: LightningCreateInvoiceResponse =>
        p.success(Right(lp.result))
    }
    p.future
  }
}
