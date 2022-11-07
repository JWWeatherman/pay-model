package com.mathbot.pay

import com.mathbot.pay.lightning.PayService.PayInvoiceServiceConfig
import com.mathbot.pay.lightning.{Bolt11, LightningInvoice, ListInvoice, Pay, PayService}
import com.softwaremill.macwire.wire
import fr.acinq.eclair.MilliSatoshi
import play.api.libs.json.Json

import scala.concurrent.duration._
import scala.util.Try

class PayServiceTest extends BaseIntegrationTest {
  val config =
    PayInvoiceServiceConfig(
      clientId = sys.env("PAY_CLIENT_ID"),
      clientSecret = Sensitive(sys.env("PAY_CLIENT_SECRET")),
      baseUrl = sys.env("PAY_BASE_URL")
    )
  val bolt11 = Bolt11(sys.env("BOLT11"))

  val service = wire[PayService]

  override def beforeAll(): Unit = {
    // todo: [info]   java.util.concurrent.TimeoutException: Future timed out after [30 seconds]
//    Await.result(
//      service.getToken.map(_.body.getOrElse(throw new RuntimeException("UNABLE TO UPDATE TOKEN"))),
//      30.seconds
//    )
  }
  "PayService" should {

//    "get token" in {
//      for {
//        r <- service.getToken
//      } yield {
//        assert(r.body.isRight)
//      }
//    }
//
//    "get info" in {
//      for {
//        tr <- service.getInfo
//      } yield {
//        assert(tr.body.isRight)
//      }
//    }
//    "create invoice" in {
//      val i = LightningInvoice(
//        msatoshi = MilliSatoshi(1000),
//        label = "TEST-PAY-LABEL-1234",
//        description = "testing a label",
//        expiry = Some(2.minutes),
//        preimage = None
//      )
//      for {
//        r <- service.invoice(i)
//      } yield {
//
//        println(r.body)
//        assert(r.body.isRight)
//      }
//    }
//
//    "get rates" in {
//      for {
//        r <- service.getRates
//      } yield {
//        assert(r.isSuccess)
//        assert(r.body.isRight)
//
//      }
//    }
//    "get rates usd" in {
//      for {
//        r <- service.getUSDRates
//      } yield {
//        assert(r.isSuccess)
//        assert(r.body.isRight)
//
//      }
//    }
//
//    "pay" in {
//      for {
//        r <- service.pay(Pay(bolt11))
//      } yield {
//        assert(r.body.isRight)
//      }
//    }

//    "get info" in {
//
//      for {
//        tr <- service.getToken
//        _ = println(tr)
//        if tr.isSuccess
//        r <- service.getInfo
//        i <- service.playerInvoiceV2(
//          PlayerInvoice__IN(msatoshi = MilliSatoshi(1000),
//                            playerId = "test",
//                            source = "test",
//                            description = None,
//                            webhook = None,
//                            expiry = Some(3.minutes),
//                            version = Some(2))
//        )
////        inv1 <- service.getInvoiceByPaymentHash("27fb17cd8f344d1bccfae7624cd8e1d3ac46a5ba8c0d1468406339fae70bf7ef")
////        invoices <- service.listInvoices()
////        s <- service.playerStatement("IT SPEC")
////        p <- service.playerPayment(
////          PlayerPayment__IN(source = "IT SPEC",
////                            playerId = "IT SPEC",
////                            bolt11 = bolt11,
////                            callbackURL = Some(CallbackURL("https://api.pollofeed.com/callback")))
////        )
//      } yield {
//
//        println(r)
//        println("invoice response: " + i)
//        assert(r.isSuccess)
//        assert(r.body.isRight)
//        assert(i.isSuccess)
////        assert(s.body.isRight)
////        assert(p.body.isRight)
////        println(i.body)
////        assert(i.body.isRight)
//      }
//    }

    "test" in {
      def openPayWsReq = {
        val req = service
          .openWsReq { msg =>
            println(msg)
            val j = Try(Json.parse(msg))
              .map(j => {
                j.validate[ListInvoice]
                  .map(li => {
                    println("Received invoice from ws label={} status={}", li.label, li.status)
                    // todo: handle
                  })
              })
              .getOrElse(println("Received pay ws message={}. Ignoring", msg))
          }
          .readTimeout(10.seconds)

        println(req.toCurl)

        req
      }

      for {
        gt <- service.getToken
        t = gt.body.right.get
        r <- openPayWsReq.send(backend)
      } yield {
        println("done")
        assert(true)
      }
    }
//    "open ws" in {
//      println("get token")
//      (for {
//        tr <- service.getToken
//        if tr.isSuccess
//        _ = println("open ws")
//        req = service.openWsReq { msg => println("msg= " + msg) }
//        _ = println(req.toCurl)
//        r <- req.send(backend)
//        _ = println("opened ws " + r.isSuccess)
//      } yield {
//        assert(r.isSuccess)
//      }) recover (err => {
//        println(err)
//        assert(false)
//      })
//    }

  }
}
