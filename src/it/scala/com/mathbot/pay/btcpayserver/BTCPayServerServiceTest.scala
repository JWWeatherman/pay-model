package com.mathbot.pay.btcpayserver

import com.mathbot.pay.BaseIntegrationTest
import com.softwaremill.macwire.wire

class BTCPayServerServiceTest extends BaseIntegrationTest {

  val config = BTCPayServerConfig(baseUrl = sys.env("BTCPAYSERVER_BASE_URL"), apiKey = sys.env("BTCPAYSERVER_API_KEY"))
  val invoiceId = sys.env.getOrElse("BTCPAYSERVER_TEST_INVOICE_ID", "")
  val apiService = wire[BTCPayServerService]
  println(config)
  "Btcpayserver" should {

    s"get invoice by id: $invoiceId" in {

      apiService
        .getInvoice(invoiceId)
        .map(i => {
          println(i.code)
          i.history.foreach(println)
          println(i.body.merge.toString.take(30))
          assert(i.body.isRight)
        })
    }
  }
}
