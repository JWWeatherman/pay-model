package com.mathbot.pay.bitcoin

import org.scalatest.FunSuite

class BtcAmountSpec extends FunSuite {

  test("btc/millibtc/satoshi/milliSatoshi conversions") {
    val x = 12.34567 btc
    val y: MilliBtc = x
    val z: Satoshi = x
    val z1: Satoshi = y
    val ms: MilliSatoshi = x
    val ms1: MilliSatoshi = y

    assert(ms === ms1)
    assert(ms.toLong === 1234567000000L)
    val x2: Btc = ms
    val y2: MilliBtc = ms
    val z2: Satoshi = ms
    assert(y2 === y)
    assert(z2 === z)
    assert(x2 === x)
    assert(x.toBigDecimal === BigDecimal(12.34567))
    assert(x.toDouble === 12.34567)
    assert(x.toLong === 12L)
    assert(y.toBigDecimal === BigDecimal(12345.67))
    assert(y.toDouble === 12345.67)
    assert(y.toLong === 12345L)
    assert(z === z1)
    assert(z.toLong === 1234567000L)
    val x1: Btc = z1
    assert(x1 === x)
    val y1: MilliBtc = z1
    assert(y1 === y)
  }

  test("conversions overflow") {
    intercept[IllegalArgumentException] {
      22e6 btc
    }
  }

  test("arithmetic operations") {
    val x = 1.1 btc
    val y: Btc = x - Satoshi(50000)
    val z: Satoshi = y
    assert(z === Satoshi(109950000))
    assert(z + MilliBtc(1.5) === Satoshi(109950000 + 150000))
    assert(z + z === Satoshi(109950000 + 109950000))
    assert(z + z - z === z)
    assert((z + z) / 2 === z)
    assert((z * 3) / 3 === z)
    assert(z * 1.5 === Satoshi(164925000))
    assert(Seq(500 satoshi, 100 satoshi, 50 satoshi).sum === Satoshi(650))
    assert(Btc(1) + Btc(2) === Btc(3))
    assert(MilliBtc(1) + MilliBtc(2) === MilliBtc(3))
    assert(Satoshi(1) + Satoshi(2) === Satoshi(3))
    assert(Btc(1.3) + MilliBtc(100) - Satoshi(100000000) === Btc(0.4))
    assert(Satoshi(130000000) + MilliBtc(200) - Btc(1.1) === Satoshi(40000000))
  }

  test("comparisons") {
    val x: Satoshi = 1.001 btc
    val y: Satoshi = 1 btc
    val z: Satoshi = 1 millibtc

    assert(x >= x)
    assert(x <= x)
    assert(x > y)
    assert(y < x)
    assert(x < y + z + z)
    assert(x === y + z)
    assert(Btc(32) > Btc(31))
    assert(MilliBtc(32) > MilliBtc(31))
    assert(Btc(1.3) < MilliBtc(1301))
    assert(Btc(1.3) > MilliBtc(1299))
    assert(Satoshi(100000) < MilliBtc(1.001))
    assert(Satoshi(100000) > MilliBtc(0.999))
  }

  test("negate amount") {
    assert(Satoshi(-20) === -Satoshi(20))
    assert(MilliBtc(-1.5) === -MilliBtc(1.5))
    assert(Btc(-2.5) === -Btc(2.5))
    assert(MilliSatoshi(-100) === -MilliSatoshi(100))
  }

  test("max/min") {
    assert((100 millisatoshi).max(101 millisatoshi) === MilliSatoshi(101))
    assert((100 millisatoshi).min(101 millisatoshi) === MilliSatoshi(100))
    assert((100000 millisatoshi).max(101 satoshi) === MilliSatoshi(101000))
    assert((100000 millisatoshi).min(101 satoshi) === MilliSatoshi(100000))
    assert((100000000 millisatoshi).max(0.999 millibtc) === MilliSatoshi(100000000))
    assert((100000000 millisatoshi).min(0.999 millibtc) === MilliSatoshi(99900000))
    assert((100000000000L millisatoshi).max(0.999 btc) === MilliSatoshi(100000000000L))
    assert((100000000000L millisatoshi).min(0.999 btc) === MilliSatoshi(99900000000L))
    assert((100 satoshi).max(101 satoshi) === Satoshi(101))
    assert((100 satoshi).min(101 satoshi) === Satoshi(100))
    assert((100 satoshi).max(101000 millisatoshi) === Satoshi(101))
    assert((100 satoshi).min(101000 millisatoshi) === Satoshi(100))
    assert((100000 satoshi).max(0.999 millibtc) === Satoshi(100000))
    assert((100000 satoshi).min(0.999 millibtc) === Satoshi(99900))
    assert((100000000 satoshi).max(0.999 btc) === Satoshi(100000000))
    assert((100000000 satoshi).min(0.999 btc) === Satoshi(99900000))
    assert((100 millibtc).max(101 millibtc) === MilliBtc(101))
    assert((100 millibtc).min(101 millibtc) === MilliBtc(100))
    assert((1 millibtc).max(90000000 millisatoshi) === MilliBtc(1))
    assert((1 millibtc).min(90000000 millisatoshi) === MilliBtc(0.9))
    assert((1 millibtc).max(90000 satoshi) === MilliBtc(1))
    assert((1 millibtc).min(90000 satoshi) === MilliBtc(0.9))
    assert((100 millibtc).max(0.2 btc) === MilliBtc(200))
    assert((100 millibtc).min(0.2 btc) === MilliBtc(100))
    assert((1.1 btc).max(0.9 btc) === Btc(1.1))
    assert((1.1 btc).min(0.9 btc) === Btc(0.9))
    assert((1.1 btc).max(900 millibtc) === Btc(1.1))
    assert((1.1 btc).min(900 millibtc) === Btc(0.9))
    assert((1.1 btc).max(90000000 satoshi) === Btc(1.1))
    assert((1.1 btc).min(90000000 satoshi) === Btc(0.9))
    assert((1.1 btc).max(90000000000L millisatoshi) === Btc(1.1))
    assert((1.1 btc).min(90000000000L millisatoshi) === Btc(0.9))
  }

}
