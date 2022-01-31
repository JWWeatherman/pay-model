package com.mathbot.pay.lightning

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.Flow
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.ByteString
import com.softwaremill.macwire.wire
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.{AnyWordSpecLike, AsyncWordSpecLike}
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.json.Json

import java.util.concurrent.atomic.AtomicInteger
import scala.language.postfixOps

class LightningStreamTest(_system: ActorSystem)
    extends TestKit(_system)
    with AsyncWordSpecLike
    with MockitoSugar
    with ImplicitSender
    with BeforeAndAfterAll {

  def this() = this(ActorSystem("HelloAkkaSpec"))

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(_system)
  }
//  implicit val ec = system.dispatcher
  val idAtom = new AtomicInteger(1)
  "LightningStream" should {
    "test bolt11 -> bolt11" in {
      val lightningStream = {
        val fakeFlow: Flow[ByteString, ByteString, NotUsed] = Flow[ByteString].map(b => b)
        val lightingFlow = Flow
          .fromFunction[LightningJson, String](LightningStream.convertToString(_, idAtom))
          .map(str => {

            println(s"converted to string " + str)
            str
          })
          .map(ByteString.apply)
          .via(fakeFlow)
          .map(_.utf8String)
          .map(Json.parse)
          .map(str => {

            println(s"response converted to json " + str)
            str
          })
          .recover {
            case t =>
              println("recover from error " + t)
              Json.toJson(LightningRequestError(ErrorMsg(500, t.toString)))
          }
        wire[LightningStream]
      }
      val bolt11 = Bolt11(
        "lnbc22435140n1p0zxgcupp5hkn3lnlkk6kmq0670a9u2xd8reuefc7dw704pwe7dqqm06nnzpcsdz2gf5hgun9ve5kcmpqv9jx2v3e8pjkgtf5xaskxtf5venxvttp8pjrvttrvgerzvm9x93r2dehxgfppjue4tflpg2hule862xylcsnu0p0mrjvmnxqrp9s2xnqfz900f0mq36dnkseqped68nzjm2nvh85uxw4nkhezkd4zzlymw93q96mf9glupqxrfd46rps7dztqm5rerusxpx77curwrpal9qqenm4p0"
      )

      for {
        s <- lightningStream.enqueue(Pay(bolt11)) {
          jsValue =>
            println(jsValue)
        }
      } yield {
        assert(true)
      }
//      lightningStream.enqueue(Pay(bolt11)) {
//        case json if json.toString().nonEmpty =>
//          println("json")
//        case other =>
//          println("err")
//      }.map(r => {
//        assert(true)
//      }).recover(err => {
//        println("error in stream")
//        println(err)
//        assert(false)
//      })
    }
//    "test exception in flow" in {
//      val lightningStream = {
//        val errorFlow: Flow[ByteString, ByteString, NotUsed] = Flow[ByteString].map(b => throw new RuntimeException())
//        val lightingFlow = Flow
//          .fromFunction[LightningJson, String](LightningStream.convertToString(_, idAtom))
//          .map(ByteString.apply)
//          .via(errorFlow)
//          .map(_.utf8String)
//          .map(Json.parse)
//          .recover {
//            case t => Json.toJson(LightningRequestError(ErrorMsg(500, t.toString)))
//          }
//        wire[LightningStream]
//      }
//      val bolt11 = Bolt11(
//        "lnbc22435140n1p0zxgcupp5hkn3lnlkk6kmq0670a9u2xd8reuefc7dw704pwe7dqqm06nnzpcsdz2gf5hgun9ve5kcmpqv9jx2v3e8pjkgtf5xaskxtf5venxvttp8pjrvttrvgerzvm9x93r2dehxgfppjue4tflpg2hule862xylcsnu0p0mrjvmnxqrp9s2xnqfz900f0mq36dnkseqped68nzjm2nvh85uxw4nkhezkd4zzlymw93q96mf9glupqxrfd46rps7dztqm5rerusxpx77curwrpal9qqenm4p0"
//      )
//      lightningStream.enqueue(bolt11)(result => {
//        assert(result.validate[LightningRequestError].isSuccess)
//        ()
//      }).map(_ => {
//        println("false")
//        assert(false)
//      })
//    }
  }
}
