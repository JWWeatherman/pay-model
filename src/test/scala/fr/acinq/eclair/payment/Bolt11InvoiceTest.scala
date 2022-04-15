package fr.acinq.eclair.payment

import fr.acinq.eclair.MilliSatoshi
import org.scalatest.OptionValues
import org.scalatest.wordspec.AnyWordSpec

class Bolt11InvoiceTest extends AnyWordSpec with OptionValues {
  "Bolt11InvoiceTest" should {
    "invoice" in {
      val b = Bolt11Invoice.fromString(
        "lnbc9u1p02hg8xpp5vszfk9k7n7cur8s72d8acnnfgah3sstfzsg88u30asuw2me8xevqdq9venkycqzpgs9hmfca78h8hz4f270l5ulevd3r95je2gxjqv9zplxqk6fm6hs4hd9fhg5j7u9r8qc3mx08hs4thme097xjfp50yv8ddt0p5v09vp4sqsu6f47"
      )
      assert(b.amountOpt.value === MilliSatoshi(900000))
    }
    "throw IllegalArgumentException on invalid string" in {
      assertThrows[IllegalArgumentException](
        Bolt11Invoice.fromString(
          "111"
        )
      )

    }
  }
}
