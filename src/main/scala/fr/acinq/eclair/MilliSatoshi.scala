package fr.acinq.eclair

import fr.acinq.bitcoin.Satoshi
import play.api.libs.json._

case class MilliSatoshi(underlying: Long) extends AnyVal with Ordered[MilliSatoshi] {
  def +(other: MilliSatoshi): MilliSatoshi =
    MilliSatoshi(
      underlying + other.underlying
    )
  def -(other: MilliSatoshi): MilliSatoshi =
    MilliSatoshi(
      underlying - other.underlying
    )
  def +(other: Satoshi): MilliSatoshi =
    MilliSatoshi(
      underlying + other.toMilliSatoshi.underlying
    )
  def -(other: Satoshi): MilliSatoshi =
    MilliSatoshi(
      underlying - other.toMilliSatoshi.underlying
    )
  def *(m: Double): MilliSatoshi =
    MilliSatoshi(underlying = (underlying * m).toLong)
  def *(m: Long): MilliSatoshi = MilliSatoshi(underlying * m)
  def /(d: Long): MilliSatoshi = MilliSatoshi(underlying / d)
  def unary_- = MilliSatoshi(-underlying)

  override def toString: String = s"$underlying msat"
  override def compare(other: MilliSatoshi): Int =
    underlying.compareTo(other.underlying)
  // Since BtcAmount is a sealed trait that MilliSatoshi cannot extend, we need to redefine comparison operators.
  def compare(other: Satoshi): Int = compare(other.toMilliSatoshi)
  def <=(other: Satoshi): Boolean = compare(other) <= 0
  def >=(other: Satoshi): Boolean = compare(other) >= 0
  def <(other: Satoshi): Boolean = compare(other) < 0
  def >(other: Satoshi): Boolean = compare(other) > 0

  // Asymmetric min/max functions to provide more control on the return type
  def max(other: MilliSatoshi): MilliSatoshi = if (this > other) this else other
  def min(other: MilliSatoshi): MilliSatoshi = if (this < other) this else other
  def max(other: Satoshi): MilliSatoshi =
    if (this > other) this else other.toMilliSatoshi
  def min(other: Satoshi): MilliSatoshi =
    if (this < other) this else other.toMilliSatoshi
  def truncateToSatoshi: Satoshi = Satoshi(underlying / 1000L)
  def toLong: Long = underlying
}

object MilliSatoshi {

  lazy implicit val formatMSatoshi: Format[MilliSatoshi] = new Format[MilliSatoshi] {
    override def writes(o: MilliSatoshi): JsValue = JsNumber(o.toLong)
    override def reads(json: JsValue): JsResult[MilliSatoshi] =
      json match {
        case JsString(satStr) =>
          satStr.replace("msat", "").toLongOption match {
            case None =>
              JsError(s"Not a MilliSatoshi Unable to convert string to long $satStr")
            case Some(value) => JsSuccess(MilliSatoshi(value))
          }
        case JsNumber(sat) => JsSuccess(MilliSatoshi(sat.toLong))
        case _ => JsError(s"Not a MilliSatoshi Invalid format")
      }
  }

}
