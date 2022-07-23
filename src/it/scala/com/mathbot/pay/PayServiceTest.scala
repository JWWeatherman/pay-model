package com.mathbot.pay

import com.mathbot.pay.lightning.PayService.{PayInvoiceServiceConfig, PlayerInvoice__IN, RpcRequest}
import com.mathbot.pay.lightning.{
  Bolt11,
  Invoices,
  LightningGetInfoRequest,
  ListInvoicesRequest,
  ListInvoicesResponse,
  Pay,
  PayService
}
import com.softwaremill.macwire.wire
import fr.acinq.eclair.MilliSatoshi
import play.api.libs.json.Json
import sttp.ws.WebSocket

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration._

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

    "get token" in {
      for {
        r <- service.getToken
      } yield {
        assert(r.body.isRight)
      }
    }

    "get info" in {
      for {
        tr <- service.getInfo
      } yield {
        assert(tr.body.isRight)
      }
    }

    "get rates" in {
      for {
        r <- service.getRates
      } yield {
        assert(r.isSuccess)
        println(r.body)
        assert(r.body.isRight)

      }
    }
    "get rates usd" in {
      for {
        r <- service.getUSDRates
      } yield {
        assert(r.isSuccess)
        println(r.body)
        assert(r.body.isRight)

      }
    }

    "pay" in {
      for {
        r <- service.pay(Pay(Bolt11(sys.env("BOLT11"))))
      } yield {
        println(r.body)
        println(r.code)
        assert(r.body.isRight)
      }
    }

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
//    "open ws" in {
//      println("get token")
//      (for {
//        tr <- service.getToken
//        if tr.isSuccess
//        _ = println("got token")
//        _ <- service.testWs
//        _ = println("open ws")
//        r <- service.openWs { msg =>
//          println("msg= " + msg)
//        }
//        _ = println("opened ws")
//      } yield {
//        assert(r.isSuccess)
//      }) recover (err => {
//        println(err)
//        assert(false)
//      })
//    }

    "open ws" in {

      val p = Promise[Unit]()
      service.testWs { ws =>
        {

          p.future
        }
      }
      (for {
        _ <- service.testWs { ws =>
          {

            println("opened ws")
            for {
              result <- service.getInvoiceByLabelWs("blackjack,bekd8Mmr1v67pr2_-w0j3g==,e_zCciNMk-o=")(ws)
              _ = println("ws label inv= " + result)
              invoiceC <- service.invoiceByWs(
                PlayerInvoice__IN(msatoshi = MilliSatoshi(1000),
                                  playerId = "",
                                  source = "",
                                  description = None,
                                  webhook = None,
                                  expiry = None)
              )(ws)
              _ = println("invoice ws= " + invoiceC)

            } yield {
              result
            }
          }
        }

      } yield {
        assert(true)
      }) recover (err => {
        println(err)
        assert(false)
      })
    }
  }
}
