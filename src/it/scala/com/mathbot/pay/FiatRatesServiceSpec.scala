package com.mathbot.pay

import com.softwaremill.macwire.wire

class FiatRatesServiceSpec extends BaseIntegrationTest {

  val service = wire[FiatRatesService]
  "FiatRatesService" should {

    "get coingecko" in {
      service.getRatesCoinGecko.send(backend).map(r => assert(r.body.isRight))
    }
    "get bitpay" in {
      service.getRatesBitPay.send(backend).map(r => assert(r.body.isRight))
    }
    "get blockchaininfo" in {
      service.getRatesBlockChainInfo.send(backend).map(r => assert(r.body.isRight))
    }
    "get rates" in {
      service.reloadData.map(r => {
        println(r)
        assert(r.isRight)
      })
    }
  }
}
