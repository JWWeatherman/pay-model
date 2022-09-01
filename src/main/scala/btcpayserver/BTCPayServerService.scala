package btcpayserver

import com.github.dwickern.macros.NameOf.nameOf
import play.api.libs.json.Json
import sttp.client3.{asString, basicRequest, SttpBackend, UriContext}
import sttp.model.MediaType

import java.util.Base64
import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@deprecated("use BTCPayServerServiceV2 b/c of issues w/ basic auth w/ sttp", "2021-02-21")
class BTCPayServerService(
    config: BTCPayServerConfig
)(implicit val e: ExecutionContext, val backend: SttpBackend[Future, _]) {

  val token = Base64.getEncoder.encodeToString(config.apiKey.getBytes())
  private val basic = s"Basic ${token}"

  val baseRequest = basicRequest.auth
    .basicToken(config.apiKey)
    //    .header("Authorization", config.apiKey, true)
    .contentType(MediaType.ApplicationJson)
    .readTimeout(Duration("30s"))
    .followRedirects(true)

  def getInvoice(id: String) = {
    val request = baseRequest
      .get(uri"${config.baseUrl}/invoices/$id")
      .response(parseChargeInfo)
    request.send(backend)
  }

  def invoice(inv: InvoiceRequest) = {
    val request = baseRequest
      .post(uri"${config.baseUrl}/invoices")
      .body(Json.toJson(inv).toString)
      .response(parseChargeInfo)
    request.send(backend)
  }

  private def parseChargeInfo =
    asString.map {
      case Left(value) =>
        Left(value)
      case Right(bodyStr) =>
        scala.util.Try(Json.parse(bodyStr)) match {
          case Failure(exception) => Left(s"Failed to parse json $bodyStr $exception")
          case Success(value) =>
            value
              .validate[ChargeInfoResponse]
              .map(Right(_))
              .getOrElse(Left(s"Response not a ${nameOf(ChargeInfoResponse)} $value"))
        }
    }

}
