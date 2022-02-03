package com.mathbot.pay.lightning.pay

import com.github.dwickern.macros.NameOf.nameOf
import com.mathbot.pay.{SecureIdentifier, Sensitive}
import com.mathbot.pay.bitcoin.MilliSatoshi
import com.mathbot.pay.lightning._
import com.mathbot.pay.lightning.pay.PayService.PayInvoiceServiceConfig
import com.mathbot.pay.webhook.CallbackURL
import play.api.libs.json.{JsValue, Json, Reads}
import sttp.capabilities.akka.AkkaStreams
import sttp.client3.SttpBackend
import sttp.model.MediaType

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object PayService {
  object PlayerStatement {
    implicit val formatPlayerStatement = Json.format[PlayerStatement]
  }
  case class PlayerStatement(invoices: Set[ListInvoice], payments: Seq[ListPay], playerId: String) {
    lazy val paidInvoices = invoices.filter(_.status == LightningInvoiceStatus.paid)
    val completeOrPendingPayments = {
      payments.filter(
        p => p.status == PayStatus.complete || p.status == PayStatus.pending || p.status == PayStatus.paid
      )
    }
    val paidInvoicesMsat = paidInvoices.flatMap(_.msatoshi).map(_.toLong).sum
    val completeOrPendingPaymentsMsat = completeOrPendingPayments.map(_.amount_sent_msat.toLong).sum
    val balance = paidInvoicesMsat - completeOrPendingPaymentsMsat
  }
  val DEFAULT_DESCRIPTION = ""
  final val SEPARATOR = ","

  def makeLabel(source: String, playerId: String, s: Int = 8) =
    s"$source$SEPARATOR$playerId$SEPARATOR${SecureIdentifier(s)}"

  def invoice(inv: PlayerInvoice__IN): LightningInvoice = {
    import inv._
    val label = makeLabel(source, playerId)
    LightningInvoice(inv.msatoshi,
                     label = label,
                     description = description.getOrElse(DEFAULT_DESCRIPTION),
                     expiry = Some(15.minutes), // todo hardcode
                     preimage = None)
  }

  def payment(p: LightningDebitRequest, playerId: String, source: String) = {
    val label = makeLabel(source, playerId)
    p.copy(pay = p.pay.copy(label = Some(label)))
  }

  def parseLabel(label: String, playerId: String) =
    label.split(SEPARATOR).toSeq match {
      case Seq(_, `playerId`, _) => true
      case o => false
    }

  def parseAppLabel(label: String, source: String) =
    label.split(SEPARATOR).toSeq match {
      case Seq(`source`, _, _) => true
      case o => false
    }

  def statement(i: Set[ListInvoice], p: Seq[ListPay], playerId: String): PlayerStatement = {

    val ii = i.filter(j => parseLabel(j.label, playerId))
    val pp = p.filter(_.label.exists(parseLabel(_, playerId)))
    PlayerStatement(ii, pp, playerId)
  }

  def appStatement(i: Set[ListInvoice], p: Seq[ListPay], source: String): PlayerStatement = {
    val ii = i.filter(j => parseAppLabel(j.label, source))
    val pp = p.filter(_.label.exists(parseAppLabel(_, source)))
    PlayerStatement(ii, pp, source)
  }

  object RpcRequest {
    implicit val formatRpcReq = Json.reads[RpcRequest]
  }
  case class RpcRequest(method: String, params: JsValue)

  case class PayInvoiceServiceConfig(clientId: String, clientSecret: Sensitive, baseUrl: String)

  object MyTokenResponse {
    implicit val formatMyTokenResponse = Json.format[MyTokenResponse]
  }

  /**
   * {
    "access_token" : "Si7EHoP_rRDoRAfTRORa-wESMvT2r5WnNarvDW-OjmQ.wCJWYPY-AoamoLF0q1ER5WJt04gOdDPgivY9EgAEBCs",
    "expires_in" : 3599,
    "scope" : "offline",
    "token_type" : "bearer"
  }
   * @param access_token
   * @param scope
   * @param token_type
   * @param refresh_token
   */
  case class MyTokenResponse(access_token: String, expires_in: Int, scope: String, token_type: String)

  object PlayerInvoice {
    implicit val formatPlayerInvoice = Json.format[PlayerInvoice]
  }
  case class PlayerInvoice(msatoshi: MilliSatoshi, playerId: String, source: String)

  object PlayerPayment__IN {
    implicit val formatPlayerPayment__IN = Json.format[PlayerPayment__IN]
  }
  case class PlayerPayment__IN(source: String, playerId: String, bolt11: Bolt11, callbackURL: Option[CallbackURL])

  object PlayerStatement__IN {
    implicit val formatPlayerStatement__IN = Json.format[PlayerStatement__IN]
  }
  case class PlayerStatement__IN(playerId: String)

  object PlayerStatement__OUT {
    implicit val formatPlayerStatement__OUT = Json.format[PlayerStatement__OUT]
  }
  case class PlayerStatement__OUT(statement: PlayerStatement)

  object AppInvoices__IN {
    implicit val formatAppInvoices__IN = Json.format[AppInvoices__IN]
  }
  case class AppInvoices__IN(source: String)

  object AppInvoices__OUT {
    implicit val formatAppInvoices__OUT = Json.format[AppInvoices__OUT]
  }
  case class AppInvoices__OUT(invoices: Set[ListInvoice], source: String)

  object AppStatment__IN {
    implicit val formatAppStatment__IN = Json.format[AppStatment__IN]
  }
  case class AppStatment__IN(source: String)

  object PlayerInvoice__IN {
    implicit val formatPlayerInvoiceRequest = Json.reads[PlayerInvoice__IN]
  }

  case class PlayerInvoice__IN(msatoshi: MilliSatoshi,
                               playerId: String,
                               source: String,
                               description: Option[String],
                               webhook: Option[CallbackURL]) {
    val invoice = PayService.invoice(this)
  }

}

class PayService(config: PayInvoiceServiceConfig, val backend: SttpBackend[Future, AkkaStreams])(
    implicit ec: ExecutionContext
) extends RpcLightningService {
  import PayService._
  import config._
  import sttp.client3._
  import sttp.client3.playJson._
  val baseUrl = config.baseUrl + "/lightning/rpc"

  var ACCESS_TOKEN = ""
  def base: RequestT[Empty, Either[String, String], Any] =
    basicRequest.auth.bearer(ACCESS_TOKEN)

  def getToken = {
    val r = basicRequest
      .post(uri"${config.baseUrl}/oauth2/token")
      .auth
      .basic(clientId, clientSecret.value)
      .body(("grant_type", "client_credentials"))
      .response(asJson[MyTokenResponse])

    r.send(backend)
      .andThen {
        case Failure(err) =>
          err
        case Success(value) =>
          value.body.foreach(r => {
            ACCESS_TOKEN = r.access_token
            logger.debug(s"Updated access_token")
          })
      }
  }

  def playerInvoice(inv: PlayerInvoice) =
    base
      .post(uri"${config.baseUrl}/lightning/player/invoice")
      .contentType(MediaType.ApplicationJson)
      .body(
        inv
      )
      .response(toBody[LightningCreateInvoice])
      .send(backend)

  def playerPayment(payment: PlayerPayment__IN) = {
    val r = base
      .contentType(MediaType.ApplicationJson)
      .post(uri"${config.baseUrl}/lightning/player/pay")
      .body(payment)
      .response(toBody[ListPay])
    r.send(backend)

  }

  def playerStatement(playerId: String) = {
    val r = base
      .contentType(MediaType.ApplicationJson)
      .post(uri"${config.baseUrl}/lightning/player/statement")
      .body(PlayerStatement__IN(playerId))
      .response(toBody[PlayerStatement__OUT])
    r.send(backend)
  }

}
