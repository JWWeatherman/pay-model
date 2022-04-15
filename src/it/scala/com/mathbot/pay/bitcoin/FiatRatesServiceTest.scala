package com.mathbot.pay.bitcoin

import com.mathbot.pay.BaseIntegrationTest
import com.softwaremill.macwire.wire

class FiatRatesServiceTest extends BaseIntegrationTest {

  val rates = wire[FiatRatesService]
  "FiatRates" should {
    "get rates" in {
      rates.requestRates.map(r => {
        println(r.code)
        println(r.body)
        assert(r.isSuccess)
      })
    }
  }
}
