package payModel.models.lightning

import payModel.models.MilliSatoshi
import play.api.libs.json._

import scala.util.Try

case class Bolt11(bolt11: String) extends LightningJson {

  require(Bolt11.isValid(bolt11))
  def amount: MilliSatoshi = LightningPaymentDecoder.parseAmount(bolt11)

  override def equals(obj: Any): Boolean = {
    obj match {
      case Bolt11(b) => this.bolt11 == b
      case s: String => this.bolt11 == s
      case _ => false
    }
  }
  override def toString: String = bolt11
}

object Bolt11 {

  lazy implicit val formatBolt11: Format[Bolt11] = new Format[Bolt11] {
    override def writes(o: Bolt11): JsValue = JsString(o.bolt11)

    override def reads(json: JsValue): JsResult[Bolt11] = json match {
      case JsString(bolt11) =>
        Try(isValid(bolt11))
          .map(_ => JsSuccess(Bolt11(bolt11)))
          .getOrElse(JsError("Invalid bolt1"))
      case _ => JsError()
    }
  }

  def isValid(bolt11: String): Boolean =
    Try(LightningPaymentDecoder.parseAmount(bolt11)).map(_.toLong > 0).getOrElse(false)

}
