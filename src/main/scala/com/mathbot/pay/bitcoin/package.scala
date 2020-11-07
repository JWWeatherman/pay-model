package com.mathbot.pay

import com.mathbot.pay.btcpayserver.BitcoinNetwork

import scala.language.implicitConversions

package object bitcoin {
  val Coin = 100000000L

  val blockHashes = Map(
    BitcoinNetwork.mainnet -> "000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f",
    BitcoinNetwork.testnet -> "000000000933ea01ad0ee984209779baaec3ced90fa3f408719526f8d77f4943"
  )

  implicit object NumericSatoshi extends Numeric[Satoshi] {
    override def plus(x: Satoshi, y: Satoshi): Satoshi = (x.toLong + y.toLong) satoshi
    override def toDouble(x: Satoshi): Double = x.toLong
    override def toFloat(x: Satoshi): Float = x.toLong
    override def toInt(x: Satoshi): Int = x.toLong.toInt
    override def negate(x: Satoshi): Satoshi = Satoshi(-x.toLong)
    override def fromInt(x: Int): Satoshi = Satoshi(x.toLong)
    override def toLong(x: Satoshi): Long = x.toLong
    override def times(x: Satoshi, y: Satoshi): Satoshi = (x.toLong * y.toLong) satoshi
    override def minus(x: Satoshi, y: Satoshi): Satoshi = (x.toLong - y.toLong) satoshi
    override def compare(x: Satoshi, y: Satoshi): Int = x.compare(y)
  }

  implicit final class SatoshiLong(private val n: Long) extends AnyVal {
    def satoshi: Satoshi = Satoshi(n)
  }

  implicit final class MilliSatoshiLong(private val n: Long) extends AnyVal {
    def millisatoshi: MilliSatoshi = MilliSatoshi(n)
  }

  implicit final class BtcDouble(private val n: Double) extends AnyVal {
    def btc: Btc = Btc(n)
  }

  implicit final class MilliBtcDouble(private val n: Double) extends AnyVal {
    def millibtc: MilliBtc = MilliBtc(n)
  }

  implicit def satoshi2btc(input: Satoshi): Btc = input.toBtc
  implicit def btc2satoshi(input: Btc): Satoshi = input.toSatoshi
  implicit def satoshi2millibtc(input: Satoshi): MilliBtc = input.toMilliBtc
  implicit def millibtc2satoshi(input: MilliBtc): Satoshi = input.toSatoshi
  implicit def btc2millibtc(input: Btc): MilliBtc = input.toMilliBtc
  implicit def millibtc2btc(input: MilliBtc): Btc = input.toBtc

  implicit def satoshi2millisatoshi(input: Satoshi): MilliSatoshi = input.toMilliSatoshi
  implicit def millisatoshi2satoshi(input: MilliSatoshi): Satoshi = input.toSatoshi

  implicit def btc2millisatoshi(input: Btc): MilliSatoshi = input.toSatoshi

  implicit def millisatoshi2btc(input: MilliSatoshi): Btc = input.toBtc

  implicit def millibtc2millisatoshi(input: MilliBtc): MilliSatoshi = input.toMilliSatoshi

  implicit def millisatoshi2millibtc(input: MilliSatoshi): MilliBtc = input.toMilliBtc

}
