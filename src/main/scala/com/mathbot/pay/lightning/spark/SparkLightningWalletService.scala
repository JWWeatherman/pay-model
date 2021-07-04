package com.mathbot.pay.lightning.spark

import akka.http.scaladsl.model.ws.Message
import akka.http.scaladsl.util.FastFuture
import akka.stream.scaladsl.{Flow, Source}
import akka.util.ByteString
import com.mathbot.pay.lightning._
import play.api.libs.json.{JsValue, Json}
import sttp.client.playJson.asJson
import sttp.client.{basicRequest, SttpBackend, UriContext}
import sttp.model.MediaType

import scala.concurrent.{ExecutionContext, Future}

class SparkLightningWalletService(config: SparkLightningWalletServiceConfig)(
    implicit ec: ExecutionContext,
    backend: SttpBackend[Future,
                         Source[ByteString, Any],
                         ({
                           type λ[γ$3$] = Flow[Message, Message, γ$3$]
                         })#λ]
) extends LightningService {

  private val base = basicRequest
    .headers(Map("X-Access" -> config.accessKey))
    .contentType(MediaType.ApplicationJson)

  private def makeBody(method: String, params: JsValue) = {
    Json.obj("method" -> method, "params" -> params).toString()
  }

  override def listPays(
      l: ListPaysRequest = ListPaysRequest(None, None)
  ): Future[Either[LightningRequestError, Pays]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("listpays", Json.toJson(l)))
      .response(asJson[Pays].mapLeft(err => LightningRequestError(ErrorMsg(500, s"Bad response $err"), None)))

    r.send().map(_.body)

  }

  override def getInfo: Future[Either[LightningRequestError, InfoResponse]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("getinfo", Json.obj()))
      .response(asJson[InfoResponse].mapLeft(err => LightningRequestError(ErrorMsg(500, s"Bad response $err"), None)))
    r.send()
      .map(_.body)
  }
  override def listInvoices(
      listInvoicesRequest: ListInvoicesRequest
  ): Future[Either[LightningRequestError, Invoices]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("listinvoices", Json.toJson(listInvoicesRequest)))
      .response(asJson[Invoices].mapLeft(err => LightningRequestError(ErrorMsg(500, s"Bad response $err"), None)))
    r.send().map(_.body)

  }

  override def pay(pay: Pay): Future[Either[LightningRequestError, Payment]] = ???

  override def waitAnyInvoice(w: WaitAnyInvoice): Future[Either[LightningRequestError, ListInvoice]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("waitanyinvoice", Json.toJson(w)))
      .response(
        asJson[ListInvoice].mapLeft(err => LightningRequestError(ErrorMsg(500, s"Bad response $err"), None))
      )
    r.send().map(_.body)
  }

  override def listOffers(
      req: LightningListOffersRequest
  ): Future[Either[LightningRequestError, Seq[LightningOffer]]] = {
    val r = base
      .post(uri"${config.baseUrl}")
      .body(makeBody("listoffers", Json.toJson(req)))
      .response(
        asJson[Seq[LightningOffer]].mapLeft(err => LightningRequestError(ErrorMsg(500, s"Bad response $err"), None))
      )
    r.send().map(_.body)
  }
}

//object SpasrkApp extends App {
//  val SPARK_URL_BASE = "https://btcpal.online/spark/btc/rpc"
//  val SPARK_ACCESS_KEY = "pWzDtFPUpmNZLqXDWkDVMMPsVpgxuasHBfgqfT7DA"
//  val logger = LoggerFactory.getLogger("PayApplication")
//  implicit val system = ActorSystem("PayApplication")
//  implicit val ec: ExecutionContext = system.dispatcher
//  implicit lazy val backend = Slf4jTimingBackend(AkkaHttpBackend.usingActorSystem(system))
//  val c = SparkLightningWalletServiceConfig(SPARK_URL_BASE, SPARK_ACCESS_KEY)
//  val s = new SparkLightningWalletService(c)
//
//  s.listInvoices().onComplete {
//    case res =>
//      logger.info(s"get info $res")
//      sys.exit()
//  }
//}
