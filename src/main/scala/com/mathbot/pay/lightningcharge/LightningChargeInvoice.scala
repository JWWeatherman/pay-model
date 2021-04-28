package com.mathbot.pay.lightningcharge

import com.mathbot.pay.bitcoin.{MilliSatoshi, Satoshi}
import com.mathbot.pay.lightning.Bolt11
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

import java.time.Instant

case class LightningChargeInvoice(id: String,
                                  msatoshi: MilliSatoshi,
                                  description: String,
                                  rhash: String,
                                  payreq: Bolt11,
                                  expires_at: Instant,
                                  created_at: Instant,
                                  status: LightningChargeInvoiceStatus,
                                  pay_index: Long,
                                  quoted_currency: Option[String] = None,
                                  quoted_amount: Option[String] = None,
                                  paid_at: Option[Instant],
                                  msatoshi_received: Option[MilliSatoshi],
                                  metadata: Option[JsValue]) {

  lazy val bolt11: Bolt11 = payreq
  lazy val label: String = id
  lazy val satoshi: Satoshi = msatoshi
}

object LightningChargeInvoice {

  object status {
    val unpaid = "unpaid"
    val expired = "expired"
    val paid = "paid"
  }

  implicit val readsInvoiceResponse: Reads[LightningChargeInvoice] = (
    (__ \ "id").read[String] and
    (__ \ "msatoshi").readWithDefault[MilliSatoshi](MilliSatoshi(0)) and
    (__ \ "description").readWithDefault[String]("") and
    (__ \ "rhash").read[String] and
    (__ \ "payreq").read[Bolt11] and
    (__ \ "expires_at").read[Long].map(d => Instant.ofEpochSecond(d)) and
    (__ \ "created_at").read[Long].map(d => Instant.ofEpochSecond(d)) and
    (__ \ "status").read[LightningChargeInvoiceStatus] and
    (__ \ "pay_index").readWithDefault[Long](-1) and
    (__ \ "quoted_currency").readNullable[String] and
    (__ \ "quoted_amount").readNullable[String] and
    (__ \ "paid_at")
      .readNullable[Long]
      .map(_.map(d => Instant.ofEpochSecond(d))) and
    (__ \ "msatoshi_received").readNullable[MilliSatoshi] and
    (__ \ "metadata").readNullable[JsValue]
  )(LightningChargeInvoice.apply _)

  implicit val writesInvoiceResponse: OWrites[LightningChargeInvoice] =
    Json.writes[LightningChargeInvoice]

}
