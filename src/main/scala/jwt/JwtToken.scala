package jwt

import play.api.libs.json.{Json, OFormat}

/**
 *
 * @param iss
 * @param sub
 * @param email
 * @param name
 * @param picture
 */
case class JwtToken(iss: String, sub: String, email: String, name: String, picture: Option[String] = None) {}

object JwtToken {

  implicit val jtwTokenFormatter: OFormat[JwtToken] = Json.format[JwtToken]

}
