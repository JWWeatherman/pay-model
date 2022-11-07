package github

import play.api.libs.json.{Json, OFormat}

case class GithubEmail(email: String, verified: Boolean, primary: Boolean)

object GithubEmail {
  implicit val githubEmailFormatter: OFormat[GithubEmail] = Json.format[GithubEmail]
}
