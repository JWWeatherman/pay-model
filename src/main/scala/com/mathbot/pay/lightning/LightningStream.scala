package com.mathbot.pay.lightning

import java.util.concurrent.atomic.AtomicInteger
import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, GraphDSL, Source, SourceQueueWithComplete, Unzip, Zip}
import akka.stream.{ActorMaterializer, OverflowStrategy, QueueOfferResult, SourceShape}
import com.github.dwickern.macros.NameOf.nameOf
import com.mathbot.pay.lightning.url.InvoiceWithDescriptionHash
import com.typesafe.scalalogging.LazyLogging
import org.slf4j.LoggerFactory
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

/**
 * @param system operating in
 * @param ec to run
 * @param lightingFlow Flow i.e. json -> unix socket -> json
 */
class LightningStream(
    lightingFlow: Flow[LightningJson, JsValue, NotUsed]
)(implicit system: ActorSystem)
    extends LazyLogging {

  private lazy val graph =
    Source.fromGraph(
      GraphDSL.create(
        Source.queue[(JsValue => Unit, LightningJson)](bufferSize = 32, overflowStrategy = OverflowStrategy.fail)
      ) { implicit builder => queue =>
        import GraphDSL.Implicits._

        val u = builder.add(Unzip[JsValue => Unit, LightningJson])
        val z = builder.add(Zip[JsValue => Unit, JsValue])
        val flow = builder.add(lightingFlow)

        queue.out ~> u.in
        u.out0 ~> z.in0
        u.out1 ~> flow.in // send the request to unix socket
        flow.out ~> z.in1 // response from socket

        SourceShape(z.out)
      }
    )

  private lazy val queue = {
    val (q, s) = graph.preMaterialize()
    s.runForeach { case (finish, lightning) => finish(lightning) }
    q
  }

  def enqueue(lightning: LightningJson)(finish: JsValue => Unit): Future[Unit] =
    queue
      .offer((finish, lightning))
      .collect {
        case QueueOfferResult.Failure(t) =>
          logger.error(s"failure adding request to queue $lightning")
          finish(
            Json.toJson(
              LightningRequestError(
                error = ErrorMsg(code = 500, message = s"Error adding request to queue. error = $t")
              )
            )
          )
      }(system.dispatcher)

}
object LightningStream extends LazyLogging {}
