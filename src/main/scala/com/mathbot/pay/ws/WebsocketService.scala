package com.mathbot.pay.ws

import java.io.IOException

import akka.NotUsed
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.ByteString
import com.mathbot.pay.ws.SocketMessageFactoryTypes.{InboundMessageFactory, OutboundMessageFactory}
import com.softwaremill.tagging._
import play.api.libs.json._
import sttp.client._
import sttp.client.ws.WebSocketResponse

import scala.concurrent.Future

case class WebsocketServiceConfig(baseUrl: String, username: String, password: String)

class WebsocketService[Tag](
    config: WebsocketServiceConfig,
    outboundActor: ActorRef @@ Tag,
    inboundMessageFactories: Set[InboundMessageFactory],
    outboundMessageFactories: Set[OutboundMessageFactory],
    system: ActorSystem,
    backend: SttpBackend[
      Future,
      Source[ByteString, Any],
      ({
        type λ[NotUsed] = Flow[Message, TextMessage.Strict, NotUsed]
      })#λ
    ]
) {
  implicit lazy val s = system
  implicit lazy val b = backend

  sealed trait WebsocketRequestActor
  class ResponseErrorActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case any =>
        log.error(s"websocketRequestActor is not set")
        // todo: respond with error that all actors will recognize
        sender() ! "Error"
    }
  }
  private val responseErrorActor: ActorRef @@ WebsocketRequestActor =
    system.actorOf(Props(new ResponseErrorActor()), "WebsocketRequestActor").taggedWith[WebsocketRequestActor]

  private var _websocketRequestActor: ActorRef @@ WebsocketRequestActor = responseErrorActor
  def websocketRequestActor: ActorRef @@ WebsocketRequestActor = _websocketRequestActor
  def websocketRequestActor_=(actorRef: ActorRef): Unit =
    _websocketRequestActor = actorRef.taggedWith[WebsocketRequestActor]

  private val outboundFactories: OutboundMessageFactory =
    outboundMessageFactories.toList match {
      case Nil =>
        // No outbound message factories
        new OutboundMessageFactory {
          override def isDefinedAt(x: AnyRef): Boolean = false

          override def apply(v1: AnyRef): JsValue = {
            assert(false, "This code should never execute")
            Json.toJson(WsError("No output message handler"))
          }
        }
      case head :: Nil =>
        // Just one handler
        head
      case head :: rest =>
        rest.foldLeft(head)((a, b) => a.orElse(b))
    }

  // Note: {case _ => defaultHandler} is a way of saying "always do this if nothing else matches" so you can call .apply(js) without a possibility of an exception
  private val inboundFactories: InboundMessageFactory =
    inboundMessageFactories.toList match {
      case Nil =>
        // No message factories so we have to create a factory that just does nothing
        new InboundMessageFactory {
          override def isDefinedAt(x: JsValue): Boolean = false

          override def apply(v1: JsValue): (JsResult[AnyRef], ActorRef) = {
            assert(false, "This code should never execute")
            (JsError("No inbound message handlers configured"), ActorRef.noSender)
          }
        }
      case head :: Nil =>
        // Just one handler
        head
      case head :: rest =>
        // At least two, so join them together
        rest.foldLeft(head)((a, b) => a.orElse(b))
    }
  // Extract incoming message string

  def openWebsocket(): Future[WebSocketResponse[NotUsed]] = {
    val sink: Sink[Message, NotUsed] = Flow[Message]
      // Extract incoming message string
      .map(msg => msg.asTextMessage.getStrictText)
      // parse string into json
      .map(Json.parse)
      // find the message handler for the json and convert it into a message
      .map(js =>
        inboundFactories
          .applyOrElse(js, (unknownJs: JsValue) => (JsError("No matching inbound handler"), outboundActor))
      )
      .map {
        case (JsSuccess(msg, _), destination) =>
          // If there was a matching handler then group it with the sending actor, the destination actor, and the message
          (outboundActor, destination, msg)
        case (jsErr: JsError, destination) =>
          // This captures the case of valid json that has a matching handler bu
          // the json had a missing property or an bad property (like  a null when it wasn't expected)
          (ActorRef.noSender, destination, WsError(jsErr))

      }
      .recover {
        // When there is an exception caused by a malformed json message (usually)
        // we just try to get an error back to the creator of the socket. There is no sender
        // because we were unable to find an adequate handler
        case ex: IOException =>
          (ActorRef.noSender, outboundActor, WsError(ex.toString))
        case _ =>
          (ActorRef.noSender, outboundActor, WsError("unknown"))
      }
      .to(
        // Tuple of Sender, Receiver, Message
        Sink.foreach[(ActorRef, ActorRef, AnyRef)] {
          case (sender, receiver, msg) => receiver.tell(msg, sender)
        }
      )

    // todo: config
    lazy val source = Source
      .actorRef[AnyRef](bufferSize = 10, overflowStrategy = OverflowStrategy.dropHead)
      .map(m =>
        outboundFactories
          .applyOrElse(
            m,
            (unmatchedMsg: AnyRef) =>
              Json.toJson(
                WsError(s"No output message handler for type ${unmatchedMsg.getClass.getCanonicalName}")
              )
          )
      )
      .map(_.toString)
      .map(TextMessage(_))
      .mapMaterializedValue(v => { websocketRequestActor = v; v; })

    val req = basicRequest
      .get(uri"${config.baseUrl}")
      .auth
      // todo:
      .bearer("TODO")
    //            .readTimeout(concurrent.duration.Duration("10 seconds")) // automatically close connection after X
    req.openWebsocket(Flow.fromSinkAndSource(sink, source))
  }

}
