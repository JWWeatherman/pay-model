package com.mathbot.pay.lightning

import org.slf4j.LoggerFactory

import scala.concurrent.{Future, Promise}

class LightningStreamService(lightningStream: LightningStream) extends LightningService {

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

  val logger = LoggerFactory.getLogger("LightnintStreamService")
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

  def getInfo: Future[Either[LightningRequestError, InfoResponse]] = {
    val p = Promise[Either[LightningRequestError, InfoResponse]]()
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
  def decodePay(bolt11: Bolt11) = {
    val p = Promise[LightningJson]()
    lightningStream.enqueue(DecodePayRequest(bolt11)) { l: LightningJson =>
      p.success(l)
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
        p.success(Right(lp.result))
    }
    p.future

  }
}
