package payModel.models

import play.api.libs.json._
import sttp.model.Uri

import scala.util.Try

trait PlayJsonSupport {

  lazy implicit val formatSatoshi: Format[Satoshi] = new Format[Satoshi] {
    override def writes(o: Satoshi): JsValue = JsNumber(o.toLong)
    override def reads(json: JsValue): JsResult[Satoshi] = json match {
      case JsNumber(sat) => JsSuccess(Satoshi(sat.toLongExact))
      case _ => JsError()
    }
  }

  lazy implicit val formatMSatoshi: Format[MilliSatoshi] = new Format[MilliSatoshi] {
    override def writes(o: MilliSatoshi): JsValue = JsNumber(o.toLong)
    override def reads(json: JsValue): JsResult[MilliSatoshi] = json match {
      case JsNumber(sat) => JsSuccess(MilliSatoshi(sat.toLong))
      case _ => JsError()
    }
  }

  lazy implicit val formatBtc = new Format[Btc] {
    override def writes(o: Btc): JsValue = JsNumber(o.toDouble)
    override def reads(json: JsValue): JsResult[Btc] = json match {
      case JsNumber(btc) => JsSuccess(Btc(btc))
      case JsString(btc) => JsSuccess(Btc(BigDecimal(btc)))
      case _ => JsError()
    }
  }

  lazy implicit val formatUri: Format[Uri] = new Format[Uri] {
    override def writes(o: Uri): JsValue = JsString(o.toString)
    override def reads(json: JsValue): JsResult[Uri] = json match {
      case JsString(str) => JsSuccess(Uri(str))
      case _ => JsError("Invalid uri")
    }
  }
  def validHash(input: String): Boolean =
    input.length == 64 && Try(BigInt(input, 16)).isSuccess

}
