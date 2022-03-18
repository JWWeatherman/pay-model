package com.mathbot.pay

import com.mathbot.pay.lightning.PayService.{PayInvoiceServiceConfig, PlayerPayment__IN}
import com.mathbot.pay.lightning.{Bolt11, PayService}
import com.mathbot.pay.webhook.CallbackURL
import com.softwaremill.macwire.wire

class PayServiceTest extends BaseIntegrationTest {
  val config =
    PayInvoiceServiceConfig(clientId = sys.env("PAY_CLIENT_ID"),
                            clientSecret = Sensitive(sys.env("PAY_CLIENT_SECRET")),
                            baseUrl = sys.env("PAY_BASE_URL"))

  val service = wire[PayService]

//  override def beforeAll(): Unit = {
//    Await.result(service.getToken.map(_.body.getOrElse(throw new RuntimeException("UNABLE TO UPDATE TOKEN"))),
//                 30.seconds)
//  }
  val bolt11 = Bolt11(
    "lnbc100n1pslhcydpp5dyxx4qt434te5wrlgrxrwzecuw8zcl2dtrew5tpfp8tg4z2tzlssdqzxycqpjsp5ktdd582vf5fg54edftkremwwdetr705nq7qpare832nw9vgmsf9qrzjq23qy3l4zhvh3jlea88x5s58kkf36ujqdzmm3za6attrsrdnmk8f5z0zdsqqykqqqqqqqqy0qqqqqqgqyg9qgsqqqyssq2zpr326vk2jtu3vvr8kmuc46jqnwmc449w0l4ejfdv47zg8npgaqxd2zsq63r059l3cqs0q076dps8k9sctf6gcrvs8p2krp2hjhdscpuldhzy"
  )
  "PayService" should {
    "get info" in {

      for {
        tr <- service.getToken
        _ = println(tr)
        if tr.isSuccess
        r <- service.getInfo
//        inv1 <- service.getInvoiceByPaymentHash("27fb17cd8f344d1bccfae7624cd8e1d3ac46a5ba8c0d1468406339fae70bf7ef")
//        invoices <- service.listInvoices()
//        s <- service.playerStatement("IT SPEC")
////        i <- service.playerInvoice(PlayerInvoice(MilliSatoshi(10000), "IT SPEC", "IT SPEC"))
//        p <- service.playerPayment(
//          PlayerPayment__IN(source = "IT SPEC",
//                            playerId = "IT SPEC",
//                            bolt11 = bolt11,
//                            callbackURL = Some(CallbackURL("https://api.pollofeed.com/callback")))
//        )
      } yield {

        println(r)
        assert(r.isSuccess)
        assert(r.body.isRight)
//        assert(s.body.isRight)
//        assert(p.body.isRight)
//        println(i.body)
//        assert(i.body.isRight)
      }
    }
//    "open ws" in {
//      for {
//        tr <- service.getToken
//        if tr.isSuccess
//        r <- service.openWs
//      } yield {
//        assert(r.isSuccess)
//      }
//    }
  }
}
