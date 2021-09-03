package com.mathbot.pay.lightning.url

import akka.http.scaladsl.model.Uri
import org.scalatest.funsuite.AnyFunSuite

class LightningUrlTest extends AnyFunSuite {

  val expected =
    "LNURL1DP68GURN8GHJ7UM9WFMXJCM99E3K7MF0V9CXJ0M385EKVCENXC6R2C35XVUKXEFCV5MKVV34X5EKZD3EV56NYD3HXQURZEPEXEJXXEPNXSCRVWFNV9NXZCN9XQ6XYEFHVGCXXCMYXYMNSERXFQ5FNS"

  val uri = "https://service.com/api?q=3fc3645b439ce8e7f2553a69e5267081d96dcd340693afabe04be7b0ccd178df"
  test("encode") {
    val rul = Uri(uri)
    val lnurl = LightningUrl(rul)
    assert(
      lnurl.url.toUpperCase === expected
    )
  }
  test("decode") {
    assert(LightningUrl.decode(expected) === uri)
  }

}
