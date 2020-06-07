package payModel.models.lightning

import java.time.Instant

import payModel.models.lightning.PayStatus.PayStatus
import payModel.models.{MilliSatoshi, PlayJsonSupport}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads.{min, _}
import play.api.libs.json.{Json, OWrites, Reads, __, _}

case class Payment(id: Long,
                       amount_msat: String,
                       amount_sent_msat: String,
                       bolt11: Bolt11,
                       created_at: Instant,
                       destination: String,
                       msatoshi: MilliSatoshi,
                       msatoshi_sent: MilliSatoshi,
                       payment_hash: String,
                       payment_preimage: String,
                       status: PayStatus,
                       error: Option[String])

object Payment extends PlayJsonSupport {


  implicit val readPayResponse: Reads[Payment] = (
    (__ \ 'id).read[Long](min(0L)) and
      (__ \ 'amount_msat).read[String] and
      (__ \ 'amount_sent_msat).read[String] and
      (__ \ 'bolt11).read[Bolt11] and
      (__ \ 'created_at).read[Long].map(d => Instant.ofEpochSecond(d)) and
      (__ \ 'destination).read[String] and
      (__ \ 'msatoshi).read[MilliSatoshi] and
      (__ \ 'msatoshi_sent).read[MilliSatoshi] and
      (__ \ 'payment_hash)
        .read[String]
        .filter(JsonValidationError(s"Hash must be 64 char"))(validHash) and
      (__ \ 'payment_preimage).read[String] and
      (__ \ 'status).read[String].map(s => PayStatus.withName(s)) and
      (__ \ 'error).readNullable[String]
    )(Payment.apply _)

  implicit val writesPayment: OWrites[Payment] =
    Json.writes[Payment]
}
