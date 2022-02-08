package com.mathbot.pay.lightning

import com.mathbot.pay.bitcoin.MilliSatoshi
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

import scala.concurrent.duration.DurationInt

class LightningInvoiceTest extends AnyWordSpec {

  "inv" should {
    "write" in {
      val inv = LightningInvoice(msatoshi = MilliSatoshi(1000),
                                 label = "label",
                                 description = "description",
                                 expiry = Some(10.minutes),
                                 preimage = None)

      val json = Json.toJson(inv)
      assert(json.toString() === """{"msatoshi":1000,"label":"label","description":"description","expiry":600}""")
    }
  }
}
