package google

import play.api.libs.json.Json

case class GoogleApiTokens(
    access_token: String,
    expires_in: Long,
    token_type: String,
    refresh_token: Option[String],
    id_token: String
)

object GoogleApiTokens {
  implicit val formatGoogleApiTokens = Json.format[GoogleApiTokens]
}
