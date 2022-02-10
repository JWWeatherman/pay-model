package com.mathbot.pay.lightning.url

import com.mathbot.pay.bitcoin.MilliSatoshi
import org.scalatest.wordspec.AnyWordSpec

import java.util.Base64
import scala.concurrent.duration.DurationInt

class InvoiceWithDescriptionHashTest extends AnyWordSpec {
  "InvoiceWithDescriptionHashTest" should {
    "create" in {
      val i = InvoiceWithDescriptionHash.apply(
        description = "test",
        milliSatoshi = MilliSatoshi(1000),
        label = "test-123",
        expiry = 15.minutes,
        img = None,
        preimage = None
      )
      assert(i.description_hash === "0ff28169eeec4762e0c3e51cb125a604557bd1e34775deeb39ba91fd01e22a0e")
    }
    val img = "aGVsbG8=" // hello
    val s = new String(Base64.getDecoder.decode(img))
    assert(s === "hello")
    "create img" in {
      val i = InvoiceWithDescriptionHash.apply(
        description = "test",
        milliSatoshi = MilliSatoshi(1000),
        label = "test-123",
        expiry = 15.minutes,
        img = Some(img),
        preimage = None
      )
      assert(i.msatoshi === MilliSatoshi(1000))
      assert(i.description_hash === "5b5c0ceb19d5d249ff047a392c737c26796a382fa2bbb55e089fe900667d53bf")
    }
    "fail to create img" in {

      assertThrows[IllegalArgumentException](
        InvoiceWithDescriptionHash.apply(
          description = "test",
          milliSatoshi = MilliSatoshi(1000),
          label = "test-123",
          expiry = 15.minutes,
          img = Some("not base 64"),
          preimage = None
        )
      )

    }
  }
}
