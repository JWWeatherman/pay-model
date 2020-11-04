package com.mathbot.pay.bitcoin

import play.api.libs.json.{Json, OFormat}

sealed trait BtcAmount

case class Satoshi(private val underlying: Long) extends BtcAmount with Ordered[Satoshi] {
  def +(other: Satoshi) = Satoshi(underlying + other.underlying)
  def -(other: Satoshi) = Satoshi(underlying - other.underlying)
  def unary_-() = Satoshi(-underlying)
  def *(m: Long) = Satoshi(underlying * m)
  def *(m: Double) = Satoshi((underlying * m).toLong)
  def /(d: Long) = Satoshi(underlying / d)
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
  def toLong = underlying
}

case class MilliBtc(private val underlying: BigDecimal) extends BtcAmount with Ordered[MilliBtc] {
  def +(other: MilliBtc) = MilliBtc(underlying + other.underlying)
  def -(other: MilliBtc) = MilliBtc(underlying - other.underlying)
  def unary_-() = MilliBtc(-underlying)
  def *(m: Long) = MilliBtc(underlying * m)
  def *(m: Double) = MilliBtc(underlying * m)
  def /(d: Long) = MilliBtc(underlying / d)
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
  def toBigDecimal = underlying
  def toDouble: Double = underlying.toDouble
  def toLong: Long = underlying.toLong
}
object Btc {
  implicit val formatBtc: OFormat[Btc] = Json.format[Btc]
}
case class Btc(private val underlying: BigDecimal) extends BtcAmount with Ordered[Btc] {
  require(underlying.abs <= 21e6, "btc must not be greater than 21 millions")

  def +(other: Btc) = Btc(underlying + other.underlying)
  def -(other: Btc) = Btc(underlying - other.underlying)
  def unary_-() = Btc(-underlying)
  def *(m: Long) = Btc(underlying * m)
  def *(m: Double) = Btc(underlying * m)
  def /(d: Long) = Btc(underlying / d)
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
  def toBigDecimal = underlying
  def toDouble: Double = underlying.toDouble
  def toLong: Long = underlying.toLong

}

case class MilliSatoshi(private val underlying: Long) extends BtcAmount with Ordered[MilliSatoshi] {
  def toLong = underlying
  def +(other: MilliSatoshi) = MilliSatoshi(underlying + other.underlying)
  def -(other: MilliSatoshi) = MilliSatoshi(underlying - other.underlying)
  def *(m: Long) = MilliSatoshi(underlying * m)
  def /(d: Long) = MilliSatoshi(underlying / d)
  def compare(other: MilliSatoshi): Int =
    if (underlying == other.underlying) 0 else if (underlying < other.underlying) -1 else 1
  def unary_-() = MilliSatoshi(-underlying)

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
  val MaxMoney = 21e6 * Coin
}
