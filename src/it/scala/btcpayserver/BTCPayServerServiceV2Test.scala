package btcpayserver

import com.mathbot.pay.{BaseIntegrationTest, SecureIdentifier}

class BTCPayServerServiceV2Test extends BaseIntegrationTest {

  val config =
    BTCPayServerConfig(baseUrl = sys.env("BTCPAYSERVER_BASE_URL"), apiKey = sys.env("BTCPAYSERVER_API_KEY"), "")
  val invoiceIdOpt = sys.env.get("BTCPAYSERVER_TEST_INVOICE_ID")

  val STORE_ID = "82UqSQBJmzXgb5kBXz1WStDUerBR3fgste3CAGZW54qk"
  val apiService = new BTCPayServerServiceV2(config)
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
    orderId = SecureIdentifier(16).toString,
    itemDesc = "itemDesc",
    redirectUrl = None,
    notificationUrl = "http://pay:9000/callback/btcpayserver",
    price = 20
  )
  "Btcpayserver" should {

    "get invoice" in {
      val r = apiService.V1.getInvoice(STORE_ID, "8euF5EoEK1zeDVkBnoWbuP")

      println(r.statusCode)
      println(r.text())
      assert(true)
    }
//    "v1 invoice" in {
//      val response = apiService.V1.invoice(STORE_ID, "1.00", "usd", Some("jchimien@gmail.com"))
//
//      val s = response.statusCode
//      println(s)
//      println(response.text())
//      assert(true)
//    }
//    var response = Option.empty[ChargeInfoResponse]
//    "create invoice" in {
//      val (res, invOpt) = apiService.invoice(req)
//      println(res.statusCode)
//      println(res.text())
//      assert(invOpt.isSuccess)
////      println(invOpt.get.data)
//      response = Some(invOpt.get)
//      assert(invOpt.isSuccess)
//      println("has addrs= " + invOpt.get.data.addresses)
//      assert(invOpt.get.data.addresses.BTC.isDefined)
//    }
//
//    val invoiceId = "Kq89L4ZasEGSgS8UMMemCD" // invoiceIdOpt.getOrElse(response.get.data.id)
//    s"get invoice by id: $invoiceId" in {
//
//      val (a, b) = apiService
//        .getInvoice(invoiceId)
//      assert(b.isSuccess)
//    }
  }
}
