package com.mathbot.pay.btcpayserver

import com.mathbot.pay.BaseIntegrationTest

class BTCPayServerServiceTest extends BaseIntegrationTest {

  val config = BTCPayServerConfig(baseUrl = sys.env("BTCPAYSERVER_BASE_URL"), apiKey = sys.env("BTCPAYSERVER_API_KEY"))
  val invoiceId = sys.env.getOrElse("BTCPAYSERVER_TEST_INVOICE_ID", "")

//  val be = new FollowRedirectsBackend(
//    AkkaHttpBackend.usingActorSystem(system),
//    sensitiveHeaders = Set()
//  )
//  val apiService = new BTCPayServerService(backend, config, system.dispatcher)
  val apiService = new BTCPayServerServiceV2(config)
  val req = InvoiceRequest(
    new Buyer(
      email = "t@gmail.com",
      name = "tester",
      address1 = Some("1234 main street"),
      city = Some("Philadelphia"),
      state = Some("PA"),
      zip = Some("19000"),
      country = Some("country"),
    ),
    orderId = "orderId123445",
    itemDesc = "itemDesc",
    redirectUrl = None,
    notificationUrl = "http://example.com",
    price = 2
  )
  "Btcpayserver" should {

    "create invoice" in {
      val (res, invOpt) = apiService.invoice(req)
      println(res.statusCode)
      println(invOpt.get.data.id)
      assert(invOpt.isSuccess)
    }

    s"get invoice by id: $invoiceId" in {

      val (a, b) = apiService
        .getInvoice(invoiceId)
      assert(b.isSuccess)
    }
  }
}
