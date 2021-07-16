package com.mathbot.pay.lightning

import com.mathbot.pay.bitcoin.MilliSatoshi
import com.mathbot.pay.lightning.Bolt11.parseAmount
import play.api.libs.json._

case class Bolt11(private val _bolt11: String) extends LightningJson {

  val bolt11: String = Bolt11.trimPrefix(_bolt11)
  val milliSatoshi: MilliSatoshi = parseAmount(bolt11)

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

    override def reads(json: JsValue): JsResult[Bolt11] =
      json match {
        case JsString(bolt11) =>
          JsSuccess(Bolt11(bolt11))
      }
  }

  def trimPrefix(bolt11: String) = if (bolt11.startsWith("lightning:")) bolt11.replace("lightning:", "") else bolt11

  @throws(classOf[RuntimeException])
  def parseAmount(bolt11: String): MilliSatoshi = {
    val idx = bolt11.lastIndexOf("1")
    val regex = s"lnbc|lni|lntb".r
    val raw = regex.replaceAllIn(bolt11.take(idx), "")
    raw.toLowerCase match {
      case empty if raw.isEmpty => MilliSatoshi(0)
      case a if a.last == 'p' => MilliSatoshi(a.dropRight(1).toLong / 10L) // 1 pico-bitcoin == 10 milli-satoshi
      case a if a.last == 'n' => MilliSatoshi(a.dropRight(1).toLong * 100L)
      case a if a.last == 'u' => MilliSatoshi(a.dropRight(1).toLong * 100000L)
      case a if a.last == 'm' => MilliSatoshi(a.dropRight(1).toLong * 100000000L)
      case a => MilliSatoshi(a.toLong * 100000000000L)
    }
  }
}
