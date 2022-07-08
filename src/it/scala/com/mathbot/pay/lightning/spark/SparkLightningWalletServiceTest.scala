package com.mathbot.pay.lightning.spark

import com.mathbot.pay.{BaseIntegrationTest, Sensitive}
import com.softwaremill.macwire.wire

class SparkLightningWalletServiceTest extends BaseIntegrationTest {
  val config = SparkLightningWalletServiceConfig(
    accessKey = Sensitive(sys.env("SPARK_ACCESS_KEY")),
    baseUrl = sys.env("SPARK_BASE_URL"),
  )
  val service = wire[SparkLightningWalletService]

  "test" should {
    "test in" in {
      service.getInfo.map(r => {
        println(r)
        assert(r.isSuccess)
      })
    }
  }

}
