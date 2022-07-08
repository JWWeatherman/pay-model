package com.mathbot.pay.lightning

import fr.acinq.eclair.MilliSatoshi
import fr.acinq.eclair.payment.Bolt11Invoice
import play.api.libs.json._

import java.time.Instant
import scala.util.{Failure, Success, Try}

/**
 * A wrapper around Bolt11Invoice for backwards compatability
 * @param invoice
 */
case class Bolt11(invoice: Bolt11Invoice) extends LightningJson {

  val bolt11: String = invoice.toString
  val milliSatoshi = invoice.amountOpt.getOrElse(MilliSatoshi(0))
  def isExpired = invoice.isExpired()
  val createdAt = Instant.ofEpochSecond(invoice.createdAt)

  override def toString: String = bolt11
}

object Bolt11 {

  val prefix = "lightning:"

  lazy implicit val formatBolt11: Format[Bolt11] = new Format[Bolt11] {
    override def writes(o: Bolt11): JsValue = JsString(o.toString)

    override def reads(json: JsValue): JsResult[Bolt11] =
      json match {
        case JsString(bolt11) =>
          Try(Bolt11(bolt11)) match {
            case Failure(exception) =>
              JsError(s"Not a Bolt11 input=$bolt11 error=$exception")
            case Success(value) =>
              JsSuccess(value)
          }
      }
  }

  def trimPrefix(bolt11: String): String = if (bolt11.startsWith(prefix)) bolt11.replace(prefix, "") else bolt11

  def apply(input: String): Bolt11 =
    Bolt11(Bolt11Invoice.fromString(trimPrefix(input)))
}
