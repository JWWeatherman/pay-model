package payModel.models.lightning

import play.api.libs.json.Json

object PayStatus extends Enumeration {
  type PayStatus = Value
  val complete, failed, pending = Value
  implicit val formatPayStatus = Json.formatEnum(this)
}
