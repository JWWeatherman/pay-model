package com.mathbot.pay.btcpayserver

import java.util.Base64

import com.mathbot.pay.BaseIntegrationTest
import com.softwaremill.macwire.wire
import sttp.client.FollowRedirectsBackend
import sttp.client.akkahttp.AkkaHttpBackend

class BTCPayServerServiceTest extends BaseIntegrationTest {

  val config = BTCPayServerConfig(baseUrl = sys.env("BTCPAYSERVER_BASE_URL"), apiKey = sys.env("BTCPAYSERVER_API_KEY"))
  val invoiceId = sys.env.getOrElse("BTCPAYSERVER_TEST_INVOICE_ID", "")

  val be = new FollowRedirectsBackend(
    AkkaHttpBackend.usingActorSystem(system),
    sensitiveHeaders = Set()
  )
  val apiService = new BTCPayServerService(be, config, system.dispatcher)
  "Btcpayserver" should {

//    "create invoice" in {
//
//      apiService.invoice(
//        InvoiceRequest(
//          new Buyer(
//            email = "email",
//            name = "name",
//            address1 = Some("address1"),
//            address2 = Some("address2"),
//            locality = Some("locality"),
//            city = Some("city"),
//            state = Some("state"),
//            region = Some("region"),
//            zip = Some("zip"),
//            country = Some("country"),
//            phone = Some("phone"),
//            sessionId = Some("sessionId"),
//          ),
//            orderId = "orderId",
//            itemDesc = "itemDesc",
//            redirectUrl = None,
//            notificationUrl = "notificationUrl",
//            price = 2,
//            currency = "currency",
//        )
//      ).map(result => {
//
//        result
//        assert(true)
//      })
//    }

    s"get invoice by id: $invoiceId" in {

      apiService
        .getInvoice(invoiceId)
        .map(i => {
          logger.info(i.code.toString())
          i.history.foreach(println)
          println(i.body.merge.toString.take(30))
          assert(i.body.isRight)
        })
    }
  }
}
