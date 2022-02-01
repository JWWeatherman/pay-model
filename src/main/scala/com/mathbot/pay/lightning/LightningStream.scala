package com.mathbot.pay.lightning

import java.util.concurrent.atomic.AtomicInteger
import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, GraphDSL, Source, SourceQueueWithComplete, Unzip, Zip}
import akka.stream.{ActorMaterializer, OverflowStrategy, QueueOfferResult, SourceShape}
import com.github.dwickern.macros.NameOf.nameOf
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
    system: ActorSystem,
    ec: ExecutionContext,
    lightingFlow: Flow[LightningJson, JsValue, NotUsed]
) extends LazyLogging {

  private implicit val as = system

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
                error = ErrorMsg(code = 500, message = s"Error adding request to queue. error = $t"),
                bolt11 = None
              )
            )
          )
      }(ec)

}
object LightningStream extends LazyLogging {

  def convertToString(lj: LightningJson, idGen: AtomicInteger): String = {
    val (method, params) = lj match {
      case w: WaitInvoice => (nameOf(WaitInvoice).toLowerCase, Json.toJsObject(w))
      case l: LightningListOffersRequest => ("listoffers", Json.toJsObject(l))
      case l: LightningOfferRequest => ("offer", Json.toJsObject(l))
      case l: ListInvoicesRequest => ("listinvoices", Json.toJsObject(l))
      case w: WaitAnyInvoice => ("waitanyinvoice", Json.toJsObject(w))
      case p: Pay => ("pay", Json.toJsObject(p))
      case x: ListPaysRequest =>
        ("listpays", Json.toJsObject(x))
      case x: LightningDebitRequest =>
        ("pay", Json.toJsObject(x.pay))
      case _: LightningGetInfoRequest =>
        ("getinfo", Json.obj())
      case DecodePayRequest(bolt11) =>
        ("decodepay", Json.obj("bolt11" -> bolt11.bolt11))
      case i: LightningInvoice =>
        ("invoice", Json.obj("msatoshi" -> i.msatoshi.toLong, "label" -> i.label, "description" -> i.description))
      case i: MultiFundChannel => ??? // TODO: implement
      case s: SetChannelFee =>
        ("setchannelfee", Json.obj("id" -> s.id, "base" -> s.base.map(_.toLong), "ppm" -> s.ppm.map(_.toLong)))
      case i: NewAddressRequest =>
        ("newaddr", Json.obj("addresstype" -> i.addresstype.toString))
    }
    val request =
      Request(method = method, id = idGen.getAndIncrement(), params = params, jsonrpc = Request.json2)
    logger.debug(s"Request $request")
    Json.toJson(request).toString
  }

}
