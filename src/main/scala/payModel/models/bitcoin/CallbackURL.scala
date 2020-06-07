package payModel.models.bitcoin

import payModel.models.PlayJsonSupport
import play.api.libs.json._
import sttp.model.Uri

import scala.util.Try

case class CallbackURL(callbackURL: Uri) {
  override def toString: String = callbackURL.toString()
}

object CallbackURL extends PlayJsonSupport {
  implicit val formatCallbackURL: Format[CallbackURL] = new Format[CallbackURL] {

    override def writes(o: CallbackURL): JsValue = JsString(o.callbackURL.toString())

    override def reads(json: JsValue): JsResult[CallbackURL] = json match {
      case JsString(v) =>
        Try(CallbackURL.apply(v))
          .map(_ => JsSuccess(CallbackURL(v)))
          .getOrElse(JsError(s"Invalid uri: $v"))
      case _ => JsError()
    }
  }
  def apply(callbackURL: String): CallbackURL =
    CallbackURL(Uri.parse(callbackURL).right.get)
}
