package com.mathbot.pay.bitcoin

import com.mathbot.pay.BaseIntegrationTest
import com.softwaremill.macwire.wire

class BitcoinJsonRpcClientIntegrationTest extends BaseIntegrationTest {

  val config = BitcoinJsonRpcConfig(baseUrl = "http://localhost:43782",
                                    username = "mathbotPayments",
                                    password = "fiddleryeeryding")
  val service = wire[BitcoinJsonRpcClient]
  "BitcoinJsonRpcClientTest" should {
    "getblockchaininfo" in {
      service.getblockchaininfo.map(r => {
        assert(r.isRight)
      })
    }
    "getbalances" in {
      service.getbalances.map(r => {
        assert(r.isRight)
      })
    }
    "getbalance" in {
      service.getbalance.map(r => {
        assert(r.isRight)
      })
    }
  }
}
