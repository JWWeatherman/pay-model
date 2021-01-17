package com.mathbot.pay.lightning

import java.util.concurrent.atomic.AtomicInteger

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, GraphDSL, Source, SourceQueueWithComplete, Unzip, Zip}
import akka.stream.{ActorMaterializer, OverflowStrategy, QueueOfferResult, SourceShape}
import org.slf4j.LoggerFactory
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

/**
 *
 * @param system operating in
 * @param ec to run
 * @param lightingFlow Flow i.e. json -> unix socket -> json
 */
class LightningStream(system: ActorSystem,
                      ec: ExecutionContext,
                      lightingFlow: Flow[LightningJson, LightningJson, NotUsed]) {

  private implicit val as = system
  private implicit val mat = ActorMaterializer()
  val logger = LoggerFactory.getLogger(this.getClass)

  private lazy val graph: Source[(LightningJson => Unit, LightningJson), SourceQueueWithComplete[
    (LightningJson => Unit, LightningJson)
  ]] =
    Source.fromGraph(
      GraphDSL.create(
        Source.queue[(LightningJson => Unit, LightningJson)](bufferSize = 32, overflowStrategy = OverflowStrategy.fail)
      ) { implicit builder => queue =>
        import GraphDSL.Implicits._

        val u = builder.add(Unzip[LightningJson => Unit, LightningJson]) // (finish, lighting request)
        val z = builder.add(Zip[LightningJson => Unit, LightningJson]) // (finish, lightning response)
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

  def enqueue(lightning: LightningJson)(finish: LightningJson => Unit): Future[Unit] =
    queue
      .offer((finish, lightning))
      .collect {
        case QueueOfferResult.Failure(t) =>
          logger.error(s"failure adding request to queue $lightning")
          finish(
            LightningRequestError(error = ErrorMsg(code = 500, message = s"Error adding request to queue. error = $t"),
                                  bolt11 = None)
          )
      }(ec)

}
object LightningStream {

  val logger = LoggerFactory.getLogger("LightningStream")

  def convertToString(lj: LightningJson, idGen: AtomicInteger): String = {
    import LightningRequestMethods._
    val (method, params) = lj match {
      case x: ListPaysRequest =>
        (listpays, Json.obj("bolt11" -> x.bolt11.bolt11))
      case _: ListAllPays =>
        (listpays, Json.obj())
      case x: LightningDebitRequest =>
        (pay, Json.obj("bolt11" -> x.bolt11.bolt11))
      case _: LightningGetInfoRequest =>
        (getinfo, Json.obj())
      case DecodePayRequest(bolt11) =>
        (decodepay, Json.obj("bolt11" -> bolt11.bolt11))
      case i: InvoiceRequest =>
        (invoice, Json.obj("msatoshi" -> i.msatoshi.toLong, "label" -> i.label, "description" -> i.description))
      case i: MultiFundChannel => ??? // todo implement
      case s: SetChannelFee =>
        (setchannelfee, Json.obj("id" -> s.id, "base" -> s.base.map(_.toLong), "ppm" -> s.ppm.map(_.toLong)))
      case i: NewAddressRequest =>
        (newaddr, Json.obj("addresstype" -> i.addresstype.toString))
    }
    val request =
      Request(method = method.toString, id = idGen.getAndIncrement(), params = params, jsonrpc = Request.json2)
    logger.debug(s"Request $request")
    Json.toJson(request).toString
  }

  def convertToLightingJson(s: String): LightningJson =
    Try(Json.parse(s)) match {
      case Failure(exception) =>
        val message = s"Json parsing error response = $s, error = $exception"
        logger.error(message)
        LightningRequestError(
          error = ErrorMsg(code = 500, message = message),
          bolt11 = None
        )
      case Success(js) =>
        logger.debug(s"Response ${js.toString()}")
        val success = js.asOpt[ListPaysResponse] orElse
        js.asOpt[PayResponse] orElse
        js.asOpt[GetInfoResponse] orElse
        js.asOpt[LightningRequestError]
        success getOrElse {
          val message = s"Unknown response from lightning node $js"
          logger.error(message)
          LightningRequestError(error = ErrorMsg(code = 500, message = message), bolt11 = None)
        }

    }
}
