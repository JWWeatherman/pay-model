package payModel.models.lightning

import play.api.libs.json.{Json, OFormat}

case class Fallback(`type`: String, addr: String, hex: String)

object Fallback {
  lazy implicit val formatFallback: OFormat[Fallback] = Json.format[Fallback]

}
