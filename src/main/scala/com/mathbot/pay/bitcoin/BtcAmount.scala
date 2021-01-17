package com.mathbot.pay.bitcoin

import java.text.DecimalFormat

import play.api.libs.json._

import scala.util.{Failure, Success, Try}

sealed trait BtcAmount

object Satoshi {

  lazy implicit val formatSatoshi: Format[Satoshi] = new Format[Satoshi] {
    override def writes(o: Satoshi): JsValue = JsNumber(o.toLong)
    override def reads(json: JsValue): JsResult[Satoshi] = json match {
      case JsNumber(sat) => JsSuccess(Satoshi(sat.toLongExact))
      case _ => JsError()
    }
  }

}

case class Satoshi(private val underlying: Long) extends BtcAmount with Ordered[Satoshi] {
  def +(other: Satoshi): Satoshi = Satoshi(underlying + other.underlying)
  def -(other: Satoshi): Satoshi = Satoshi(underlying - other.underlying)
  def unary_-(): Satoshi = Satoshi(-underlying)
  def *(m: Long): Satoshi = Satoshi(underlying * m)
  def *(m: Double): Satoshi = Satoshi((underlying * m).toLong)
  def /(d: Long): Satoshi = Satoshi(underlying / d)
  def compare(other: Satoshi): Int = underlying.compare(other.underlying)
  def max(other: BtcAmount): Satoshi = other match {
    case other: Satoshi => if (underlying > other.underlying) this else other
    case other: MilliSatoshi => if (underlying > other.toSatoshi.underlying) this else other.toSatoshi
    case other: MilliBtc => if (underlying > other.toSatoshi.underlying) this else other.toSatoshi
    case other: Btc => if (underlying > other.toSatoshi.underlying) this else other.toSatoshi
  }
  def min(other: BtcAmount): Satoshi = other match {
    case other: Satoshi => if (underlying < other.underlying) this else other
    case other: MilliSatoshi => if (underlying < other.toSatoshi.underlying) this else other.toSatoshi
    case other: MilliBtc => if (underlying < other.toSatoshi.underlying) this else other.toSatoshi
    case other: Btc => if (underlying < other.toSatoshi.underlying) this else other.toSatoshi
  }
  def toBtc: Btc = Btc(BigDecimal(underlying) / BtcAmount.Coin)
  def toMilliSatoshi: MilliSatoshi = MilliSatoshi(underlying * 1000L)
  def toMilliBtc: MilliBtc = toBtc.toMilliBtc
  def toLong: Long = underlying
}

case class MilliBtc(private val underlying: BigDecimal) extends BtcAmount with Ordered[MilliBtc] {
  def +(other: MilliBtc): MilliBtc = MilliBtc(underlying + other.underlying)
  def -(other: MilliBtc): MilliBtc = MilliBtc(underlying - other.underlying)
  def unary_-(): MilliBtc = MilliBtc(-underlying)
  def *(m: Long): MilliBtc = MilliBtc(underlying * m)
  def *(m: Double): MilliBtc = MilliBtc(underlying * m)
  def /(d: Long): MilliBtc = MilliBtc(underlying / d)
  def compare(other: MilliBtc): Int = underlying.compare(other.underlying)
  def max(other: BtcAmount): MilliBtc = other match {
    case other: Satoshi => if (underlying > other.toMilliBtc.underlying) this else other.toMilliBtc
    case other: MilliSatoshi => if (underlying > other.toMilliBtc.underlying) this else other.toMilliBtc
    case other: MilliBtc => if (underlying > other.underlying) this else other
    case other: Btc => if (underlying > other.toMilliBtc.underlying) this else other.toMilliBtc
  }
  def min(other: BtcAmount): MilliBtc = other match {
    case other: Satoshi => if (underlying < other.toMilliBtc.underlying) this else other.toMilliBtc
    case other: MilliSatoshi => if (underlying < other.toMilliBtc.underlying) this else other.toMilliBtc
    case other: MilliBtc => if (underlying < other.underlying) this else other
    case other: Btc => if (underlying < other.toMilliBtc.underlying) this else other.toMilliBtc
  }
  def toBtc: Btc = Btc(underlying / 1000)
  def toSatoshi: Satoshi = toBtc.toSatoshi
  def toMilliSatoshi: MilliSatoshi = toSatoshi.toMilliSatoshi
  def toBigDecimal: BigDecimal = underlying
  def toDouble: Double = underlying.toDouble
  def toLong: Long = underlying.toLong
}
object Btc {

  private val btcFormat = new DecimalFormat("0.########")

  def stringify(btc: Btc): String = btcFormat.format(btc.underlying)

  lazy implicit val formatBtc: Format[Btc] = new Format[Btc] {
    override def writes(o: Btc): JsValue = JsString(stringify(o))
    override def reads(json: JsValue): JsResult[Btc] = json match {
      case JsNumber(btc) => JsSuccess(Btc(btc))
      case JsString(btc) => JsSuccess(Btc(BigDecimal(btc)))
      case _ => JsError()
    }
  }
}
case class Btc(private val underlying: BigDecimal) extends BtcAmount with Ordered[Btc] {
  require(underlying.abs <= 21e6, "btc must not be greater than 21 millions")

  def +(other: Btc): Btc = Btc(underlying + other.underlying)
  def -(other: Btc): Btc = Btc(underlying - other.underlying)
  def unary_-(): Btc = Btc(-underlying)
  def *(m: Long): Btc = Btc(underlying * m)
  def *(m: Double): Btc = Btc(underlying * m)
  def /(d: Long): Btc = Btc(underlying / d)
  def compare(other: Btc): Int = underlying.compare(other.underlying)
  def max(other: BtcAmount): Btc = other match {
    case other: Satoshi => if (underlying > other.toBtc.underlying) this else other.toBtc
    case other: MilliSatoshi => if (underlying > other.toBtc.underlying) this else other.toBtc
    case other: MilliBtc => if (underlying > other.toBtc.underlying) this else other.toBtc
    case other: Btc => if (underlying > other.underlying) this else other
  }
  def min(other: BtcAmount): Btc = other match {
    case other: Satoshi => if (underlying < other.toBtc.underlying) this else other.toBtc
    case other: MilliSatoshi => if (underlying < other.toBtc.underlying) this else other.toBtc
    case other: MilliBtc => if (underlying < other.toBtc.underlying) this else other.toBtc
    case other: Btc => if (underlying < other.underlying) this else other
  }
  def toMilliBtc: MilliBtc = MilliBtc(underlying * 1000)
  def toSatoshi: Satoshi = Satoshi((underlying * BtcAmount.Coin).toLong)
  def toMilliSatoshi: MilliSatoshi = toSatoshi.toMilliSatoshi
  def toBigDecimal: BigDecimal = underlying
  def toDouble: Double = underlying.toDouble
  def toLong: Long = underlying.toLong

}

object MilliSatoshi {

  lazy implicit val formatMSatoshi: Format[MilliSatoshi] = new Format[MilliSatoshi] {
    override def writes(o: MilliSatoshi): JsValue = JsNumber(o.toLong)
    override def reads(json: JsValue): JsResult[MilliSatoshi] = json match {
      case JsString(satStr) =>
        Try(satStr.toLong) match {
          case Failure(exception) => JsError(s"Unable to convert to long $exception")
          case Success(value) => JsSuccess(MilliSatoshi(value))
        }
      case JsNumber(sat) => JsSuccess(MilliSatoshi(sat.toLong))
      case _ => JsError("Invalid format")
    }
  }

}

case class MilliSatoshi(private val underlying: Long) extends BtcAmount with Ordered[MilliSatoshi] {
  def toLong: Long = underlying
  def +(other: MilliSatoshi): MilliSatoshi = MilliSatoshi(underlying + other.underlying)
  def -(other: MilliSatoshi): MilliSatoshi = MilliSatoshi(underlying - other.underlying)
  def *(m: Long): MilliSatoshi = MilliSatoshi(underlying * m)
  def /(d: Long): MilliSatoshi = MilliSatoshi(underlying / d)
  def compare(other: MilliSatoshi): Int =
    if (underlying == other.underlying) 0 else if (underlying < other.underlying) -1 else 1
  def unary_-(): MilliSatoshi = MilliSatoshi(-underlying)

  def max(other: BtcAmount): MilliSatoshi = other match {
    case other: Satoshi => if (underlying > other.toMilliSatoshi.underlying) this else other.toMilliSatoshi
    case other: MilliSatoshi => if (underlying > other.underlying) this else other
    case other: MilliBtc => if (underlying > other.toMilliSatoshi.underlying) this else other.toMilliSatoshi
    case other: Btc => if (underlying > other.toMilliSatoshi.underlying) this else other.toMilliSatoshi
  }
  def min(other: BtcAmount): MilliSatoshi = other match {
    case other: Satoshi => if (underlying < other.toMilliSatoshi.underlying) this else other.toMilliSatoshi
    case other: MilliSatoshi => if (underlying < other.underlying) this else other
    case other: MilliBtc => if (underlying < other.toMilliSatoshi.underlying) this else other.toMilliSatoshi
    case other: Btc => if (underlying < other.toMilliSatoshi.underlying) this else other.toMilliSatoshi
  }
  def toBtc: Btc = toSatoshi.toBtc
  def toMilliBtc: MilliBtc = toSatoshi.toMilliBtc
  def toSatoshi: Satoshi = Satoshi(underlying / 1000)
}

object BtcAmount {
  val Coin = 100000000L
  val Cent = 1000000L
  val MaxMoney: Double = 21e6 * Coin
}
