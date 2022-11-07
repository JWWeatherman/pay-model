package github

import play.api.libs.json.{Json, OFormat}

case class GithubUser(
    id: Long,
    avatar_url: String,
    email: Option[String],
    name: Option[String]
)

object GithubUser {
  implicit val githubUserFormat: OFormat[GithubUser] = Json.format[GithubUser]
}
