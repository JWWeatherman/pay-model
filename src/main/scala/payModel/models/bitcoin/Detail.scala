package payModel.models.bitcoin

import play.api.libs.json.{Json, OFormat}

case class Detail(address: BtcAddress, amount: Double, category: String, vout: Int)

object Detail {
  implicit val formatDetail: OFormat[Detail] = Json.format[Detail]
}
