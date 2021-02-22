package com.mathbot.pay.ws

import akka.actor.{Actor, ActorRef}
import com.mathbot.pay.BaseIntegrationTest
import com.mathbot.pay.ws.SocketMessageFactoryTypes.{InboundMessageFactory, OutboundMessageFactory}
import com.softwaremill.tagging.Tagger
import play.api.libs.json.{JsObject, JsValue, Json}
import sttp.client.akkahttp.AkkaHttpBackend

import scala.concurrent.duration._

class WebsocketServiceTest extends BaseIntegrationTest {

  class TestMessageFactory(inbound: ActorRef) extends MessageFactory {
    override def inboundFactories: InboundMessageFactory = {
      case r => (r.validate[JsObject], inbound)
    }
    override def outboundFactories: OutboundMessageFactory = {
      case a => Json.obj("result" -> a.toString)
    }
  }

  class TesterActor extends Actor {
    override def receive: Receive = {
      case w: WsLightningRequestError =>
        println(s"ON BEHALF ${system.actorSelection(w.onBehalfOf)}")
        system.actorSelection(w.onBehalfOf) ! w
      case a =>
        println(s"received $a")
    }
  }
  val echoServer = "wss://echo.websocket.org"

  sealed trait OUTBOUND

  val outbound = testActor.taggedWith[OUTBOUND]

  "PaySocket" should {
    "connect to echo server" in {

      val factory = new TestMessageFactory(testActor)
      val socket = new WebsocketService[OUTBOUND](
        config = WebsocketServiceConfig(baseUrl = echoServer, username = "who", password = "what"),
        outboundActor = outbound,
        inboundMessageFactories = Set(factory.inboundFactories),
        outboundMessageFactories = Set(factory.outboundFactories),
        system = system,
        backend = AkkaHttpBackend()
      )
      for {
        _ <- socket.openWebsocket()
      } yield {
        println("connected to websocket")
        val messages = "Bearly Awake" :: "Tally Ho!" :: Nil
        val expectedResponses = messages.map(m => Json.obj("result" -> m))
        messages foreach (socket.websocketRequestActor ! _)
        // messages routed to testActor
        expectMsgAllOf(expectedResponses: _*)
        assert(true)
        assert(true)
      }
    }
    "connect to local pay server" in pending //
    // {
//
//      val a = system.actorOf(Props(new TesterActor()))
//      val factoryB = new BtcMessageFactory(a)
//      val factoryL = new LightningMessageFactory(a)
//      val socket = new WebsocketService(
//        config = WebsocketServiceConfig(baseUrl = localUrl, username = "test10", password = "secret"),
//        outboundActor = testActor,
//        inboundMessageFactories = Set(factoryB.inboundMessageFactory, factoryL.inboundMessageFactory),
//        outboundMessageFactories = Set(factoryB.outboundMessageFactory, factoryL.outboundMessageFactory),
//        system = system,
//        backend = backend
//      )
//
//      val debitRequest = DebitRequest(
//        token = SecureIdentifier(16).toString,
//        btcDebit = Some(BtcDebit(Satoshi(1000), BtcAddress("2N1zadhnf3sjZ6gtegcpvRBqzG1N1to1Kk7"))),
//        bolt11 = None,
//        `type` = PaymentType.chain
//      )
//      for {
//        p <- socket.openWebsocket()
//      } yield {
//        println(s"connected to websocket $p")
//        socket.websocketRequestActor ! WsLightningDebitRequest(
//          bolt11 = Bolt11(
//            "lnbc10n1p0fx9hdpp5qg8u0asg4vn4nyrmcr64txv84dfl9yj8g6jdz0yns38j2hqffshsdq9u2d2zxqr3jscqp2sp532kj7d6uz8hvs5ud2vhm0vk5gz3wve2mnzfpewsf9qvmuua8rnlsrzjq0087haf00y87pywja6n8lwqfp9lfc99kh9xu40hhtejk5euhtplwzgvu5qqfsgqqqqqqq6jqqqqqzsqpc9qy9qsqttal90wu83gwe3wn6kjrezzw4qhyt5upxr9ejkasruvt60nlqkz4n50s3a5aa3m80y32v6s9mq22mleqcfg0s5ztzmhd6vcg9pjsj5gqm8lm57"
//          ),
//          callbackURL = CallbackURL("http://mathbot.com"),
//          onBehalfOf = testActor.path
//        )
//        //        socket.websocketRequestActor ! WsWalletTransactionInfoRequest(
//        //          TxId("d6e889709bdd03b3dea86e4a01e6d12a51cdf4c97ac0d9f6db0fd41cea9f5109"),
//        //          a.toString()
//        //        )
//        //        socket.websocketRequestActor ! WsBtcDebitRequest(debitRequest.btcDebit.get.btcAddress,
//        //                                                         debitRequest.btcDebit.get.satoshi,
//        //                                                         CallbackURL("http://mathbot.com"),
//        //                                                         SecureIdentifier(16).toString)
//        val r = receiveN(1, 30.seconds)
//        r foreach println
//        assert(true)
//      }
//    }
  }
}
