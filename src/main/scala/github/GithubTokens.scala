package github

import jwt.JwtToken
import play.api.libs.json._

case class GithubTokens(
    access_token: String,
    scope: Seq[String],
    token_type: String,
    id_token: JwtToken
)

object GithubTokens {
  implicit val githubTokenFormat: OFormat[GithubTokens] = Json.format[GithubTokens]
}
