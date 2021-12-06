package com.mathbot.pay.lightning.url

import com.mathbot.pay.lightning.Bolt11
import play.api.libs.json.{JsValue, Json, Writes}

case class AESSuccess(description: String, ciphertext: String, iv: String) {
  require(description.length <= 144, "invalid desciption")
}

case class MessageSuccess(message: String) extends SuccessAction {
  override def tag: String = "message"
}

object MessageSuccess {
  implicit val writesF = Json.writes[MessageSuccess]
}

case class UrlSucess(url: String, description: String) extends SuccessAction {
  override def tag: String = "url"
}

object UrlSucess {
  implicit val writesF = Json.writes[UrlSucess]
}

sealed trait SuccessAction {
  def tag: String
}

object SuccessAction {
  implicit val wrties = new Writes[SuccessAction] {
    override def writes(o: SuccessAction): JsValue =
      o match {
        case m: MessageSuccess => Json.toJsObject(m) ++ Json.obj("tag" -> m.tag)
        case m: UrlSucess => Json.toJsObject(m) ++ Json.obj("tag" -> m.tag)
      }
  }
}

/**
 * @param pr bolt11
 * @param successAction
 * @param disposable if null default to true
 */
case class LightningUrlPayRequest(pr: Bolt11, successAction: Option[SuccessAction], disposable: Option[Boolean] = None)

object LightningUrlPayRequest {
  implicit val formatLightningUrlPayRequest = Json.writes[LightningUrlPayRequest]
}
