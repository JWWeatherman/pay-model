package com.mathbot.pay.lightning.spark

import com.mathbot.pay.bitcoin.MilliSatoshi
import com.mathbot.pay.lightning.LightningInvoice
import com.mathbot.pay.{BaseIntegrationTest, Sensitive}
import com.softwaremill.macwire.wire

import java.time.Instant
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

class SparkLightningWalletServiceTest extends BaseIntegrationTest {
  val config = SparkLightningWalletServiceConfig(
    accessKey = Sensitive(sys.env("SPARK_ACCESS_KEY")),
    baseUrl = sys.env("SPARK_BASE_URL"),
  )
  val service = wire[SparkLightningWalletService]

  "LightningChargeService" should {
//    "get info" in {
//      service.getInfo.map(r => {
//        println(r)
//        assert(r.isSuccess)
//      })
//    }
//    "create invoice by currency" in {
//
//      service
//        .invoice(
//          LightningInvoice(
//            msatoshi = MilliSatoshi(1),
//            expiry = Some(FiniteDuration(10, TimeUnit.MINUTES)),
//            description = "just a test invoice",
//            label = s"test-invoice-${Instant.now}",
//            preimage = None
//          )
//        )
//        .map(res => {
//
//          println(res)
//          assert(res.isSuccess)
//        })
//    }
    "get invoice" in {
      service.getInvoice(payment_hash = "").map(res => {
        assert(res.isSuccess)
      })
    }
  }
}
