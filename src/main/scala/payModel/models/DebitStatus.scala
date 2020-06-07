package payModel.models

import play.api.libs.json._

object DebitStatus extends Enumeration {
  type DebitStatus = Value
  val pending, failed, complete, confirmed, expired = Value
  implicit val formatDebitStatus= Json.formatEnum(this)
}
