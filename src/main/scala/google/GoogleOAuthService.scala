package google

import com.github.dwickern.macros.NameOf.nameOf
import com.typesafe.scalalogging.StrictLogging
import jwt.JwtToken
import pdi.jwt.JwtAlgorithm.RS256
import pdi.jwt.JwtJson
import play.api.libs.json.{JsObject, JsString}
import sttp.capabilities.akka.AkkaStreams
import sttp.client3.SttpBackend

import java.io.ByteArrayInputStream
import java.security.cert.{CertificateFactory, X509Certificate}
import java.time.Instant
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Success

class GoogleOAuthService(backend: SttpBackend[Future, AkkaStreams], config: GoogleApiConfig)(
    implicit executionContext: ExecutionContext
) extends StrictLogging {

  import sttp.client3._
  import playJson._

  private var lastLoaded = Option.empty[Instant]
  private var pemCertificates: Seq[X509Certificate] = Seq.empty[X509Certificate]

  def loadCertificates: collection.Seq[X509Certificate] = {
    if (lastLoaded.forall(_.isBefore(Instant.now().minusSeconds(Duration("24h").toSeconds))) || pemCertificates.isEmpty) {
      logger.info(s"Loading new ${nameOf(pemCertificates)}")
      val f = basicRequest
        .get(uri"${config.oauthPemUrl}")
        .response(asJson[JsObject])
        .send(backend)
        .map { res =>
          {
            res.body match {
              case Left(_) =>
                logger.error(s"Failed to load ${nameOf(pemCertificates)} uri=${config.oauthPemUrl} code=${res.code}")
                Seq()
              case Right(certs) => // parse the certificates
                val cf = CertificateFactory.getInstance("X.509")
                certs.fields
                  .map(_._2.asInstanceOf[JsString].value.getBytes)
                  .map(new ByteArrayInputStream(_))
                  .map { stream =>
                    val c = cf.generateCertificate(stream).asInstanceOf[X509Certificate]
                    lastLoaded = Some(Instant.now)
                    logger.info(s"Loading new ${nameOf(pemCertificates)}")
                    stream.close()
                    c
                  }
            }
          }
        }
      Await.result(
        f,
        Duration("30s")
      ) // Don't want to start without certs, so its okay to block the http call once a day
    } else pemCertificates
  }

  def retrieveAndVerifyTokens(code: String) = {
    pemCertificates = loadCertificates.toSeq
    basicRequest
      .post(uri"${config.token_uri}")
      .body(
        Map(
          nameOf(code) -> code,
          nameOf(config.client_id) -> config.client_id,
          nameOf(config.client_secret) -> config.client_secret,
          nameOf(config.redirect_uri) -> config.redirect_uri.toString(),
          "grant_type" -> "authorization_code"
        )
      )
      .response(asJson[GoogleApiTokens])
      .send(backend)
      .map(res => {
        res.body
          .map(tokens => {
            val publicKeys = pemCertificates.map(_.getPublicKey)
            val parsedTokens =
              publicKeys.map(k => JwtJson.decodeJson(token = tokens.id_token, key = k, algorithms = Seq(RS256)))
            val token = parsedTokens collectFirst {
                case Success(value) => value
              } flatMap (_.validate[JwtToken].asOpt)
            token

          })
      })
  }
}
