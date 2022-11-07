package google

import akka.http.scaladsl.model.Uri
import com.typesafe.config.Config

import scala.jdk.CollectionConverters.CollectionHasAsScala

case class GoogleApiConfig(
    redirect_uri: Uri,
    auth_token_uri: Uri,
    token_uri: Uri,
    client_id: String,
    client_secret: String,
    scopes: Seq[String],
    oauthPemUrl: Uri
)

object GoogleApiConfig {

  def forConfig(config: Config): GoogleApiConfig =
    GoogleApiConfig(
      redirect_uri = config.getString("google.redirect_uri"),
      auth_token_uri = config.getString("google.auth_token_uri"),
      token_uri = config.getString("google.token_uri"),
      client_id = config.getString("google.client_id"),
      client_secret = config.getString("google.client_secret"),
      scopes = config
        .getStringList("google.scopes")
        .asScala
        .toSeq,
      oauthPemUrl = config.getString("google.oauthPemUrl")
    )
}
