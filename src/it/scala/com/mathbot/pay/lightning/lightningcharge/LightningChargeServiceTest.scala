package com.mathbot.pay.lightning.lightningcharge

import com.mathbot.pay.BaseIntegrationTest
import com.softwaremill.macwire.wire

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

class LightningChargeServiceTest extends BaseIntegrationTest {

  val config = LightningChargeConfig(
    username = sys.env("LIGHTNING_CHARGE_USERNAME"),
    password = sys.env("LIGHTNING_CHARGE_PASSWORD"),
    baseUrl = sys.env("LIGHTNING_CHARGE_BASEURL"),
    websocketUrl = sys.env("LIGHTNING_CHARGE_WEBSOCKETURL")
  )
  val service = wire[LightningChargeService]

  "LightningChargeService" should {
    "create invoice by currency" in {

      service
        .invoice(
          LightningChargeInvoiceRequestByCurrency(webhook = None,
                                                  amount = 1,
                                                  currency = "usd",
                                                  expiry = FiniteDuration(10, TimeUnit.MINUTES),
                                                  description = "test currency",
                                                  metadata = None)
        )
        .map(res => {

          assert(res.isSuccess)
        })
    }
  }
}
