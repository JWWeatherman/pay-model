package com.mathbot.pay.lightning

import akka.stream.scaladsl.Flow
import com.github.dwickern.macros.NameOf.nameOf
import com.mathbot.pay.FiatRatesService.FiatRatesInfo
import com.mathbot.pay.Sensitive
import play.api.libs.json._
import sttp.capabilities
import sttp.capabilities.akka.AkkaStreams
import sttp.client3.SttpBackend
import sttp.ws.{WebSocket, WebSocketFrame}

import java.time.Instant
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object PayService {

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
    import sttp.client3._
    val baseUri = uri"$baseUrl"
    val wsBaseUri = {
      // todo: handle wss
      baseUri.scheme("ws")
    }
    val wsBaseUrl = wsBaseUri.toString
  }

  object MyTokenResponse {
    implicit val formatMyTokenResponse: OFormat[MyTokenResponse] = Json.format[MyTokenResponse]
  }

  /**
   * @param access_token eg Si7EHoP_rRDoRAfTRORa-wESMvT2r5WnNarvDW-OjmQ.wCJWYPY-AoamoLF0q1ER5WJt04gOdDPgivY9EgAEBCs
   * @param expires_in eg 3599
   * @param scope eg offline
   * @param token_type bearer
   */
  case class MyTokenResponse(access_token: String, expires_in: Int, scope: String, token_type: String) {
    val expiresAt: Instant = Instant.now().plusSeconds(expires_in)
  }

  object FiatRatesInfoUSD {
    implicit val fFiatRatesInfoUSD: OFormat[FiatRatesInfoUSD] = Json.format[FiatRatesInfoUSD]
  }
  case class FiatRatesInfoUSD(usd: BigDecimal)
}

class PayService(
    config: PayService.PayInvoiceServiceConfig,
    val backend: SttpBackend[Future, AkkaStreams with capabilities.WebSockets]
)(implicit val ec: ExecutionContext)
    extends RpcLightningService {
  import PayService._
  import config._
  import sttp.client3._
  import playJson._
  // todo: hardcode
  val baseUrl: String = config.baseUrl + "/lightning/rpc"
  val baseUrlV2: String = config.baseUrl + "/v2/lightning/rpc"

  private var ACCESS_TOKEN = config.accessToken.getOrElse(default = "INVALID")
  def base: RequestT[Empty, Either[String, String], Any] =
    basicRequest.auth.bearer(ACCESS_TOKEN)

  def setAccessToken(r: MyTokenResponse): Unit =
    this.synchronized {
      ACCESS_TOKEN = r.access_token
    }

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
            setAccessToken(r)
          })
      }

  object V2 {
    def invoice(
        inv: LightningInvoice
    ): Future[Response[Either[LightningRequestError, ListInvoice]]] = {
      val r = base
        .post(uri"$baseUrl")
        .body(makeBody(nameOf(invoice _), Json.toJson(inv)))
        .response(toBody[LightningCreateInvoice].mapRight(ci => {
          ListInvoice(
            label = inv.label,
            bolt11 = Some(ci.bolt11),
            payment_hash = ci.payment_hash,
            amount_msat = Some(inv.msatoshi),
            amount_received_msat = None,
            status = LightningInvoiceStatus.unpaid,
            pay_index = None,
            paid_at = None,
            description = inv.description,
            expires_at = ci.expires_at,
            bolt12 = None,
            local_offer_id = None,
            payer_note = None,
            payment_preimage = None
          )
        }))
      r.send(backend)
    }
  }

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

  def wsBase = base.get(uri"${config.wsBaseUrl}/ws")

  def testWs(useWebSocket: WebSocket[Future] => Future[Unit]): Future[Response[Either[String, Unit]]] =
    wsBase.response(asWebSocket(useWebSocket)).send(backend)

  def openWsReq(
      onMessage: String => Unit
  ): RequestT[Identity, Either[String, Unit], Any with AkkaStreams with capabilities.WebSockets] = {
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
  }

  def openWs(onMessage: String => Unit): Future[Response[Either[String, Unit]]] = {
    openWsReq(onMessage).send(backend)
  }

  def getInvoiceByLabelWs(label: String)(ws: WebSocket[Future]): Future[Option[ListInvoice]] =
    for {
      _ <- ws.sendText(
        Json
          .obj(
            "method" -> "listinvoices",
            "params" -> ListInvoicesRequest(label = Some(label))
          )
          .toString()
      )
      r <- ws
        .receiveText()
        .map(Json.parse(_).asOpt[Invoices].flatMap(_.invoices.find(_.label == label)))
    } yield r

}
