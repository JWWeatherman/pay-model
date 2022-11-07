package github

import akka.http.scaladsl.model.Uri
import com.typesafe.config.Config

import scala.jdk.CollectionConverters.CollectionHasAsScala

case class GithubApiConfig(
    oauthUrl: Uri,
    authRedirectUrl: Uri,
    authTokenUrl: Uri,
    publicEmailsUrl: Uri,
    userUrl: Uri,
    clientId: String,
    clientSecret: String,
    scopes: Seq[String],
    issuer: Uri
)

object GithubApiConfig {
  def forConfig(config: Config): GithubApiConfig =
    GithubApiConfig(
      oauthUrl = config.getString("github.oauth-url"),
      authRedirectUrl = config.getString("github.auth-redirect-url"),
      authTokenUrl = config.getString("github.auth-token-url"),
      publicEmailsUrl = config.getString("github.public-emails-url"),
      userUrl = config.getString("github.user-url"),
      clientId = config.getString("github.client-id"),
      clientSecret = config.getString("github.client-secret"),
      scopes = config.getStringList("github.scopes").asScala.toSeq,
      issuer = config.getString("github.issuer")
    )
}
