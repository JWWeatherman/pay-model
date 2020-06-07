package payModel.models.lightning

import play.api.libs.json.{Json, OFormat}

case class Route(
    pubkey: String,
    short_channel_id: String,
    fee_base_msat: Long,
    fee_proportional_millionths: Long,
    cltv_expiry_delta: Long
)

object Route {
  lazy implicit val formatRoute: OFormat[Route] = Json.format[Route]

}
