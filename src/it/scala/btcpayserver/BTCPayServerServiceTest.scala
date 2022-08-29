package btcpayserver

import com.mathbot.pay.BaseIntegrationTest

class BTCPayServerServiceTest extends BaseIntegrationTest {

  val config = BTCPayServerConfig(baseUrl = sys.env("BTCPAYSERVER_BASE_URL"), apiKey = sys.env("BTCPAYSERVER_API_KEY"))
  val invoiceIdOpt = sys.env.get("BTCPAYSERVER_TEST_INVOICE_ID")

  val apiService = new BTCPayServerService(config)
  val req = InvoiceRequest(
    new Buyer(
      email = "t@gmail.com",
      name = "tester",
      address1 = Some("1234 main street"),
      city = Some("Philadelphia"),
      state = Some("PA"),
      zip = Some("19000"),
      country = Some("country")
    ),
    orderId = "orderId123445",
    itemDesc = "itemDesc",
    redirectUrl = None,
    notificationUrl = "http://example.com",
    price = 2
  )
  "Btcpayserver" should {

    var response = Option.empty[ChargeInfoResponse]
    "create invoice" in {

      for {
        res <- apiService.invoice(req)
      } yield {
        println(res.code)
        println(res)
        assert(res.isSuccess)
      }
    }

//    val invoiceId = invoiceIdOpt.getOrElse(response.get.data.id)
//    s"get invoice by id: $invoiceId" in {
//
//      val (a, b) = apiService
//        .getInvoice(invoiceId)
//      assert(b.isSuccess)
//    }
  }
}
