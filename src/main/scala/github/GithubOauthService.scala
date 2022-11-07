package github

import akka.http.scaladsl.util.FastFuture
import cats.data.EitherT
import com.mathbot.pay.SecureIdentifier
import com.typesafe.scalalogging.StrictLogging
import jwt.JwtToken
import play.api.libs.json.{JsError, Json}
import sttp.capabilities.akka.AkkaStreams
import sttp.client3.SttpBackend
import sttp.model.MediaType

import scala.concurrent.{ExecutionContext, Future}

/**
 * Call github to get a user information
 * @param config
 * @param backend
 * @param executionContext
 */
class GithubOauthService(val config: GithubApiConfig, backend: SttpBackend[Future, AkkaStreams])(
    implicit executionContext: ExecutionContext
) extends StrictLogging {

  import sttp.client3._
  import playJson._
  private val base = basicRequest.headers(Map("Accept" -> MediaType.ApplicationJson.toString()))
  private def userBase(token: GithubAccessToken) = base.headers(Map("Authorization" -> s"token ${token.access_token}"))
  def retrieveTokens(
      sessionId: SecureIdentifier,
      code: String
  ): Future[Response[Either[ResponseException[String, JsError], GithubAccessToken]]] = {
    val m = Json.obj(
      "code" -> code,
      "client_id" -> config.clientId,
      "client_secret" -> config.clientSecret,
      "redirect_uri" -> config.authRedirectUrl.toString(),
      "state" -> sessionId.toString
    )
    base
      .post(uri"${config.authTokenUrl}")
      .body(m)
      .response(asJson[GithubAccessToken])
      .send(backend)

  }

  def retrievePublicEmails(
      tokens: GithubAccessToken
  ): Future[Response[Either[ResponseException[String, JsError], Seq[GithubEmail]]]] = {
    userBase(tokens)
      .get(uri"${config.publicEmailsUrl}")
      .response(asJson[Seq[GithubEmail]])
      .send(backend)
  }

  def retrieveUser(
      tokens: GithubAccessToken
  ): Future[Response[Either[ResponseException[String, JsError], GithubUser]]] = {
    userBase(tokens)
      .get(uri"${config.userUrl}")
      .response(asJson[GithubUser])
      .send(backend)
  }

  def retrieveTokensForUser(
      sessionId: SecureIdentifier,
      code: String
  ): EitherT[Future, ResponseException[String, JsError], GithubTokensFromCodeSuccess] =
    for {
      tokens <- EitherT(retrieveTokens(sessionId, code).map(_.body))
      user <- EitherT { retrieveUser(tokens).map(_.body) }
      emails <- EitherT {
        // if email found, no need to lookup
        user.email
          .map(email =>
            FastFuture.successful(Right(GithubEmail(email = email, verified = true, primary = true) :: Nil))
          )
          .getOrElse(retrievePublicEmails(tokens).map(_.body))
      } // probably can remove this call as user has primary email
    } yield {
      val jwt = JwtToken(
        iss = config.issuer.toString(),
        sub = user.id.toString,
        email = emails.find(_.primary).map(_.email).orElse(user.email).getOrElse(""),
        name = user.name.getOrElse(""),
        picture = Some(user.avatar_url)
      )
      GithubTokensFromCodeSuccess(
        sessionId = sessionId,
        tokens = GithubTokens(
          access_token = tokens.access_token,
          scope = tokens.scope.split(':'),
          token_type = tokens.token_type,
          id_token = jwt
        )
      )
    }
}
