package payModel.models.lightning
import play.api.libs.json.{Json, OFormat}

case class LightningRequestError(error: ErrorMsg, bolt11: Option[Bolt11] = None) extends LightningJson

object LightningRequestError {
  lazy implicit val formatRequestError: OFormat[LightningRequestError] = Json.format[LightningRequestError]
}
