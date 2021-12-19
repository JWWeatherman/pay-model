package com.mathbot.pay.lightning.spark

import com.mathbot.pay.bitcoin.MilliSatoshi
import com.mathbot.pay.lightning.LightningInvoice
import com.mathbot.pay.{BaseIntegrationTest, Sensitive}
import com.softwaremill.macwire.wire

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

class SparkLightningWalletServiceTest extends BaseIntegrationTest {
  val config = SparkLightningWalletServiceConfig(
    accessKey = Sensitive(sys.env("SPARK_ACCESS_KEY")),
    baseUrl = sys.env("SPARK_BASE_URL"),
  )
  val service = wire[SparkLightningWalletService]

  "LightningChargeService" should {
    "create invoice by currency" in {

      service
        .invoice(
          LightningInvoice(
            msatoshi = MilliSatoshi(1),
            expiry = Some(FiniteDuration(10, TimeUnit.MINUTES)),
            description = "test currency",
            label = "test hello 3",
            preimage = None
          )
        )
        .map(res => {

          println(res)
          assert(res.isSuccess)
        })
    }
  }
}
