package com.mathbot.pay.lightning.url

import com.mathbot.pay.bitcoin.MilliSatoshi
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.duration.DurationInt

class InvoiceWithDescriptionHashTest extends AnyWordSpec {
  "InvoiceWithDescriptionHashTest" should {
    "create" in {
      val i = InvoiceWithDescriptionHash.apply(description = "test",
                                               milliSatoshi = MilliSatoshi(1000),
                                               label = "test-123",
                                               expiry = 15.minutes,
                                               img = None,
                                               preimage = None)
      assert(i.description_hash === "0ff28169eeec4762e0c3e51cb125a604557bd1e34775deeb39ba91fd01e22a0e")
    }
  }
}
