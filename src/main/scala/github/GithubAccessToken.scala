package github

case class GithubAccessToken(
    access_token: String,
    scope: String,
    token_type: String
)

object GithubAccessToken {
  import play.api.libs.json._

  implicit val githubAccessTokenFormat: OFormat[GithubAccessToken] = Json.format[GithubAccessToken]
}
