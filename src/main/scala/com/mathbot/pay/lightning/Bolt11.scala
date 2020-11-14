package com.mathbot.pay.lightning

import com.mathbot.pay.bitcoin.MilliSatoshi
import play.api.libs.json._

import scala.util.{Failure, Success, Try}

case class Bolt11(private val _bolt11: String) extends LightningJson {

  val bolt11: String = Bolt11.trimPrefix(_bolt11)
  val milliSatoshi: MilliSatoshi = LightningPaymentDecoder.parseAmount(bolt11)

  override def equals(obj: Any): Boolean = {
    obj match {
      case b: Bolt11 => bolt11 == b.bolt11
      case s: String => bolt11 == s
      case _ => false
    }
  }
  override def toString: String = bolt11
}

object Bolt11 {

  val prefix = "lightning:"

  lazy implicit val formatBolt11: Format[Bolt11] = new Format[Bolt11] {
    override def writes(o: Bolt11): JsValue = JsString(o.bolt11)

    override def reads(json: JsValue): JsResult[Bolt11] = json match {
      case JsString(bolt11) =>
        JsSuccess(Bolt11(bolt11))
    }
  }

  def trimPrefix(bolt11: String) = if (bolt11.startsWith("lightning:")) bolt11.replace("lightning:", "") else bolt11

}
