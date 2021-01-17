package com.mathbot.pay.json

import akka.actor.ActorPath
import play.api.libs.json._
import sttp.model.Uri

import scala.util.Try

trait PlayJsonSupport {

  lazy implicit val formatActorRef = new Format[ActorPath] {
    override def writes(o: ActorPath): JsValue = JsString(o.toString) // todo: use toStringWithoutAddress

    override def reads(json: JsValue): JsResult[ActorPath] =
      json match {
        case JsString(value) => JsSuccess(ActorPath.fromString(value)) // todo: read w/o requiring address
        case e => JsError("Error reading actor path")
      }
  }

  lazy implicit val formatUri: Format[Uri] = new Format[Uri] {
    override def writes(o: Uri): JsValue = JsString(o.toString)
    override def reads(json: JsValue): JsResult[Uri] =
      json match {
        case JsString(str) => JsSuccess(Uri(str))
        case _ => JsError("Invalid uri")
      }
  }
  def validHash(input: String): Boolean =
    input.length == 64 && Try(BigInt(input, 16)).isSuccess

}
