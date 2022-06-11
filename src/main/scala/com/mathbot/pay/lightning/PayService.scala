package com.mathbot.pay.lightning

import akka.stream.scaladsl.Flow
import com.mathbot.pay.FiatRatesService.FiatRatesInfo
import com.mathbot.pay.json.{FiniteDurationToSecondsReader, FiniteDurationToSecondsWriter}
import com.mathbot.pay.lightning.url.{CreateInvoiceWithDescriptionHash, InvoiceWithDescriptionHash}
import com.mathbot.pay.webhook.CallbackURL
import com.mathbot.pay.{SecureIdentifier, Sensitive}
import fr.acinq.eclair.MilliSatoshi
import play.api.libs.json._
import sttp.capabilities
import sttp.capabilities.akka.AkkaStreams
import sttp.client3.SttpBackend
import sttp.model.MediaType
import sttp.ws.{WebSocket, WebSocketFrame}

import java.time.Instant
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object PayService {
  object PlayerStatement {

    implicit val formatPlayerStatement: Format[PlayerStatement] = new Format[PlayerStatement] {
      private implicit val fo: OFormat[PlayerStatement] = Json.format[PlayerStatement]
      override def reads(json: JsValue): JsResult[PlayerStatement] = json.validate[PlayerStatement]

      override def writes(o: PlayerStatement): JsValue =
        Json
          .toJson[PlayerStatement](o)
          .as[JsObject] ++ Json.obj("balance" -> o.balance)
    }
  }
  case class PlayerStatement(
      invoices: Set[ListInvoice],
      payments: Seq[ListPay],
      playerId: String,
      subtractFromBalance: MilliSatoshi
  ) {
    lazy val paidInvoices: Set[ListInvoice] = invoices.filter(_.isPaid)
    val completeOrPendingPayments: Seq[ListPay] = payments.filter(p => p.isPaid || p.isPending)
    val paidInvoicesMsat: MilliSatoshi = paidInvoices.flatMap(_.amount_msat).sum
    // pending payments we'll have to deduct amount_sent_msat since destinatino has not recieved amount
    val completeOrPendingPaymentsMsat: MilliSatoshi =
      completeOrPendingPayments.map(r => r.amount_msat getOrElse r.amount_sent_msat).sum
    val balance: MilliSatoshi = paidInvoicesMsat - completeOrPendingPaymentsMsat - subtractFromBalance
  }
  val DEFAULT_DESCRIPTION = ""
  final val SEPARATOR = ","

  def makeLabel(source: String, playerId: String, s: Int = 8) =
    s"$source$SEPARATOR$playerId$SEPARATOR${SecureIdentifier(s)}"

  /**
   *
   * @param label ListInvoice.label
   * @param source application name
   * @return (playerId, secureIdentifier)
   */
  def parseLabel(label: String, source: String): Option[(String, String)] = label.split(SEPARATOR).toSeq match {
    case Seq(`source`, playerId, id) => Some(playerId, id)
    case o => None
  }

  def invoice(inv: PlayerInvoice__IN): LightningInvoice = {
    import inv._
    val label = makeLabel(source, playerId)
    LightningInvoice(
      inv.msatoshi,
      label = label,
      description = description.getOrElse(DEFAULT_DESCRIPTION),
      expiry = expiry orElse Some(15.minutes), // todo hardcode
      preimage = None
    )
  }

  def payment(p: LightningDebitRequest, playerId: String, source: String): LightningDebitRequest = {
    val label = makeLabel(source, playerId)
    p.copy(pay = p.pay.copy(label = Some(label)))
  }

  def parseAppLabel(label: String, source: String): Boolean =
    label.split(SEPARATOR).toSeq match {
      case Seq(`source`, _, _) => true
      case o => false
    }

  def statement(
      i: Set[ListInvoice],
      p: Seq[ListPay],
      playerId: String,
      subtractFromBalance: MilliSatoshi
  ): PlayerStatement = {

    val ii = i.filter(j => parseLabel(j.label, playerId).isDefined)
    val pp = p.filter(_.label.exists(parseLabel(_, playerId).isDefined))
    PlayerStatement(ii, pp, playerId, subtractFromBalance)
  }

  def appStatement(i: Set[ListInvoice], p: Seq[ListPay], source: String): PlayerStatement = {
    val ii = i.filter(j => parseAppLabel(j.label, source))
    val pp = p.filter(_.label.exists(parseAppLabel(_, source)))
    PlayerStatement(invoices = ii, payments = pp, playerId = source, subtractFromBalance = MilliSatoshi(0))
  }

  object RpcRequest {
    implicit val formatRpcReq: Reads[RpcRequest] = Json.reads[RpcRequest]
  }
  case class RpcRequest(method: String, params: JsValue)

  case class PayInvoiceServiceConfig(
      clientId: String,
      clientSecret: Sensitive,
      baseUrl: String,
      accessToken: Option[String] = None
  ) {
    val wsBaseUrl: String =
      if (baseUrl.startsWith("https")) baseUrl.replace("https", "wss")
      else baseUrl.replace("http", "ws")
  }

  object MyTokenResponse {
    implicit val formatMyTokenResponse: OFormat[MyTokenResponse] = Json.format[MyTokenResponse]
  }

  /**
   * {
   *    "access_token" : "Si7EHoP_rRDoRAfTRORa-wESMvT2r5WnNarvDW-OjmQ.wCJWYPY-AoamoLF0q1ER5WJt04gOdDPgivY9EgAEBCs",
   *    "expires_in" : 3599,
   *    "scope" : "offline",
   *    "token_type" : "bearer"
   *  }
   * @param access_token
   * @param scope
   * @param token_type
   * @param refresh_token
   */
  case class MyTokenResponse(access_token: String, expires_in: Int, scope: String, token_type: String) {
    val expiresAt: Instant = Instant.now().plusSeconds(expires_in)
  }

  object PlayerPayment__IN {
    implicit val formatPlayerPayment__IN: OFormat[PlayerPayment__IN] = Json.format[PlayerPayment__IN]
  }
  case class PlayerPayment__IN(source: String, playerId: String, bolt11: Bolt11, callbackURL: Option[CallbackURL]) {
    val label: String = makeLabel(source = source, playerId = playerId)
  }

  object PlayerStatement__IN {
    implicit val formatPlayerStatement__IN: OFormat[PlayerStatement__IN] = Json.format[PlayerStatement__IN]
  }
  case class PlayerStatement__IN(playerId: String, subtractFromBalance: Option[MilliSatoshi]) {
    val toSubtract: MilliSatoshi = subtractFromBalance.getOrElse(MilliSatoshi(0))
  }

  object PlayerStatement__OUT {
    implicit val formatPlayerStatement__OUT: OFormat[PlayerStatement__OUT] = Json.format[PlayerStatement__OUT]
  }
  case class PlayerStatement__OUT(statement: PlayerStatement)

  object AppInvoices__IN {
    implicit val formatAppInvoices__IN: OFormat[AppInvoices__IN] = Json.format[AppInvoices__IN]
  }
  case class AppInvoices__IN(source: String)

  object AppInvoices__OUT {
    implicit val formatAppInvoices__OUT: OFormat[AppInvoices__OUT] = Json.format[AppInvoices__OUT]
  }
  case class AppInvoices__OUT(invoices: Set[ListInvoice], source: String)

  object AppStatement__IN {
    implicit val formatAppStatment__IN: OFormat[AppStatement__IN] = Json.format[AppStatement__IN]
  }
  case class AppStatement__IN(source: String)

  object PlayerInvoice__IN extends FiniteDurationToSecondsReader with FiniteDurationToSecondsWriter {
    implicit val formatPlayerInvoiceRequest: OFormat[PlayerInvoice__IN] = Json.format[PlayerInvoice__IN]
  }

  case class PlayerInvoice__IN(
      msatoshi: MilliSatoshi,
      playerId: String,
      source: String,
      description: Option[String],
      webhook: Option[CallbackURL],
      expiry: Option[FiniteDuration],
      version: Option[Int] = None
  ) {
    val invoice: LightningInvoice = PayService.invoice(this)
  }

  object ValidatePay {
    implicit val formatValidatePay: OFormat[ValidatePay] = Json.format[ValidatePay]
  }
  case class ValidatePay(pay: PlayerPayment__IN, subtractFromBalance: MilliSatoshi) {
    def maxWithdraw(statement: PlayerStatement): MilliSatoshi = statement.balance - subtractFromBalance
    def validateStatement(statement: PlayerStatement): Boolean =
      pay.bolt11.invoice.amountOpt.exists(e => e.toLong <= maxWithdraw(statement).toLong)
  }

  object PlayerInvoiceWithDescriptionHash__IN {
    implicit val formatPlayerInvoiceWithDescriptionHash__In: OFormat[PlayerInvoiceWithDescriptionHash__IN] =
      Json.format[PlayerInvoiceWithDescriptionHash__IN]
  }
  case class PlayerInvoiceWithDescriptionHash__IN(
      invoice: InvoiceWithDescriptionHash,
      source: String,
      playerId: String
  ) {
    val inv: InvoiceWithDescriptionHash = invoice.copy(label = PayService.makeLabel(source, playerId))
  }
  object FiatRatesInfoUSD {
    implicit val fFiatRatesInfoUSD: OFormat[FiatRatesInfoUSD] = Json.format[FiatRatesInfoUSD]
  }
  case class FiatRatesInfoUSD(usd: Double)
}

class PayService(config: PayService.PayInvoiceServiceConfig,
                 val backend: SttpBackend[Future, AkkaStreams with capabilities.WebSockets])(implicit val
                                                                                             ec: ExecutionContext)
    extends RpcLightningService {
  import PayService._
  import config._
  import sttp.client3._
  import playJson._
  val baseUrl: String = config.baseUrl + "/lightning/rpc"

  private var ACCESS_TOKEN = config.accessToken.getOrElse(default = "INVALID")
  def base: RequestT[Empty, Either[String, String], Any] =
    basicRequest.auth.bearer(ACCESS_TOKEN)

  def getToken: Future[Response[Either[ResponseException[String, JsError], MyTokenResponse]]] =
    basicRequest
      .post(uri"${config.baseUrl}/oauth2/token")
      .auth
      .basic(clientId, clientSecret.value)
      .body(("grant_type", "client_credentials"))
      .response(asJson[MyTokenResponse])
      .send(backend)
      .andThen {
        case Failure(err) =>
          err
        case Success(value) =>
          value.body.foreach(r => {
            ACCESS_TOKEN = r.access_token
            logger.debug(s"Updated access_token")
          })
      }

  @deprecated("prefer playerInvoiceV2 to return full invoice details")
  def playerInvoice(inv: PlayerInvoice__IN): Future[Response[Either[LightningRequestError, LightningCreateInvoice]]] =
    base
      .post(uri"${config.baseUrl}/lightning/player/invoice")
      .contentType(MediaType.ApplicationJson)
      .body(inv)
      .response(toBody[LightningCreateInvoice])
      .send(backend)

  def playerInvoiceV2(inv: PlayerInvoice__IN): Future[Response[Either[LightningRequestError, ListInvoice]]] =
    base
      .post(uri"${config.baseUrl}/lightning/player/invoice/v2")
      .contentType(MediaType.ApplicationJson)
      .body(inv)
      .response(toBody[ListInvoice])
      .send(backend)

  def playerInvoiceWithDescriptionHash(
      inv: PlayerInvoiceWithDescriptionHash__IN
  ): Future[Response[Either[LightningRequestError, CreateInvoiceWithDescriptionHash]]] =
    base
      .post(uri"${config.baseUrl}/lightning/player/invoiceWithDescriptionHash")
      .contentType(MediaType.ApplicationJson)
      .body(inv)
      .response(toBody[CreateInvoiceWithDescriptionHash])
      .send(backend)

  def playerPayment(payment: PlayerPayment__IN): Future[Response[Either[LightningRequestError, ListPay]]] =
    base
      .contentType(MediaType.ApplicationJson)
      .post(uri"${config.baseUrl}/lightning/player/pay")
      .body(payment)
      .response(toBody[ListPay])
      .send(backend)

  def playerStatement(
      playerId: String,
      subtractFromBalance: Option[MilliSatoshi] = None
  ): Future[Response[Either[LightningRequestError, PlayerStatement__OUT]]] =
    base
      .contentType(MediaType.ApplicationJson)
      .post(uri"${config.baseUrl}/lightning/player/statement")
      .body(PlayerStatement__IN(playerId, subtractFromBalance))
      .response(toBody[PlayerStatement__OUT])
      .send(backend)

  /**
   * Validate the balance on the server and pay the invoice if so
   * @param validatePay
   * @return
   */
  def validatePay(validatePay: ValidatePay): Future[Response[Either[LightningRequestError, ListPay]]] =
    base
      .post(uri"${config.baseUrl}/lightning/player/validatePay")
      .body(validatePay)
      .response(toBody[ListPay])
      .send(backend)

  def getRates: Future[Response[Either[LightningRequestError, FiatRatesInfo]]] =
    base
      .get(uri"${config.baseUrl}/rates")
      .response(toBody[FiatRatesInfo])
      .send(backend)

  def getUSDRates: Future[Response[Either[LightningRequestError, FiatRatesInfoUSD]]] =
    base
      .get(uri"${config.baseUrl}/rates/usd")
      .response(toBody[FiatRatesInfoUSD])
      .send(backend)
//

  def useWebSocket(ws: WebSocket[Future]): Future[Unit] = {
    def receive() = ws.receiveText().map(t => println(s"RECEIVED: $t"))
    for {
      _ <- ws.sendText(Json.toJson(LightningGetInfoRequest()).toString())
      _ <- receive()
//      _ <- receive()
    } yield ()
  }

  lazy val wsBase = base.get(uri"${config.wsBaseUrl}/ws")
  def testWs: Future[Response[Either[String, Unit]]] = {
    wsBase.response(asWebSocket(useWebSocket)).send(backend)
  }
  def testWs(useWebSocket: WebSocket[Future] => Future[Unit]): Future[Response[Either[String, Unit]]] =
    wsBase.response(asWebSocket(useWebSocket)).send(backend)

  def openWs(onMessage: String => Unit): Future[Response[Either[String, Unit]]] = {
    wsBase
      .response(
        asWebSocketStream(AkkaStreams)(
          Flow[WebSocketFrame.Data[_]].map {
            case d @ WebSocketFrame.Text(payload, finalFragment, rsv) =>
              onMessage(d.payload)
              WebSocketFrame.ping
            case WebSocketFrame.Binary(payload, finalFragment, rsv) =>
              onMessage(new String(payload))
              WebSocketFrame.ping
          }
        )
      )
      .send(backend)
  }
}
