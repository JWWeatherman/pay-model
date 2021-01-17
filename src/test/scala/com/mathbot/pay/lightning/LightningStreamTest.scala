package com.mathbot.pay.lightning

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.Flow
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.ByteString
import com.softwaremill.macwire.wire
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.mockito.MockitoSugar

import java.util.concurrent.atomic.AtomicInteger
import scala.language.postfixOps

class LightningStreamTest(_system: ActorSystem)
    extends TestKit(_system)
    with AnyWordSpecLike
    with MockitoSugar
    with ImplicitSender
    with BeforeAndAfterAll {

  def this() = this(ActorSystem("HelloAkkaSpec"))

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(_system)
  }
  val ec = system.dispatcher
  val idAtom = new AtomicInteger(1)
  "LightningStream" should {
    "test bolt11 -> bolt11" in {
      val lightningStream = {
        val fakeFlow: Flow[ByteString, ByteString, NotUsed] = Flow[ByteString].map(b => b)
        val lightingFlow = Flow
          .fromFunction[LightningJson, String](LightningStream.convertToString(_, idAtom))
          .map(ByteString.apply)
          .via(fakeFlow)
          .map(_.utf8String)
          .map(LightningStream.convertToLightingJson)
          .recover {
            case t => LightningRequestError(ErrorMsg(500, t.toString))
          }
        wire[LightningStream]
      }
      val bolt11 = Bolt11(
        "lnbc22435140n1p0zxgcupp5hkn3lnlkk6kmq0670a9u2xd8reuefc7dw704pwe7dqqm06nnzpcsdz2gf5hgun9ve5kcmpqv9jx2v3e8pjkgtf5xaskxtf5venxvttp8pjrvttrvgerzvm9x93r2dehxgfppjue4tflpg2hule862xylcsnu0p0mrjvmnxqrp9s2xnqfz900f0mq36dnkseqped68nzjm2nvh85uxw4nkhezkd4zzlymw93q96mf9glupqxrfd46rps7dztqm5rerusxpx77curwrpal9qqenm4p0"
      )
      lightningStream.enqueue(bolt11)(result => {
        assert(result == bolt11)
      })
    }
    "test exception in flow" in {
      val lightningStream = {
        val errorFlow: Flow[ByteString, ByteString, NotUsed] = Flow[ByteString].map(b => throw new RuntimeException())
        val lightingFlow = Flow
          .fromFunction[LightningJson, String](LightningStream.convertToString(_, idAtom))
          .map(ByteString.apply)
          .via(errorFlow)
          .map(_.utf8String)
          .map(LightningStream.convertToLightingJson)
          .recover {
            case t => LightningRequestError(ErrorMsg(500, t.toString))
          }
        wire[LightningStream]
      }
      val bolt11 = Bolt11(
        "lnbc22435140n1p0zxgcupp5hkn3lnlkk6kmq0670a9u2xd8reuefc7dw704pwe7dqqm06nnzpcsdz2gf5hgun9ve5kcmpqv9jx2v3e8pjkgtf5xaskxtf5venxvttp8pjrvttrvgerzvm9x93r2dehxgfppjue4tflpg2hule862xylcsnu0p0mrjvmnxqrp9s2xnqfz900f0mq36dnkseqped68nzjm2nvh85uxw4nkhezkd4zzlymw93q96mf9glupqxrfd46rps7dztqm5rerusxpx77curwrpal9qqenm4p0"
      )
      lightningStream.enqueue(bolt11)(result => {
        assert(result.isInstanceOf[LightningRequestError])
      })
    }
  }
}
