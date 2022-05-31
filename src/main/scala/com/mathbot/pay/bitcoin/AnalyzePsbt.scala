package com.mathbot.pay.bitcoin

import com.mathbot.pay.bitcoin.PsbtRole.PsbtRole
import fr.acinq.bitcoin.Btc
import play.api.libs.json.{Json, OFormat}

object Missing {
  implicit val formatMissing: OFormat[Missing] = Json.format[Missing]
}
case class Missing(
    signatures: Option[List[String]],
    redeemscript: Option[String]
)

object PsbtInputs {
  implicit val formatInputs: OFormat[PsbtInputs] = Json.format[PsbtInputs]
}
case class PsbtInputs(
    has_utxo: Boolean,
    is_final: Boolean,
    next: String,
    missing: Option[Missing]
)

object AnalyzePsbt {
  implicit val formatAnalyzePsbt: OFormat[AnalyzePsbt] = Json.format[AnalyzePsbt]
}

case class AnalyzePsbt(
    inputs: List[PsbtInputs],
    estimated_vsize: Option[Int],
    estimated_feerate: Option[Btc],
    fee: Option[Btc],
    next: PsbtRole
)

object FinalizedPsbtResponse {
  implicit val formatFinalizedPsbtResponse: OFormat[FinalizedPsbtResponse] = Json.format[FinalizedPsbtResponse]

}

/**
 * Result:
 * {
 * "psbt" : "value",          (string) The base64-encoded partially signed transaction if not extracted
 * "hex" : "value",           (string) The hex-encoded network transaction if extracted
 * "complete" : true|false,   (boolean) If the transaction has a complete set of signatures
 * ]
 * }
 * @param hex
 * @param psbt
 * @param complete
 */
case class FinalizedPsbtResponse(
    hex: Option[String],
    psbt: Option[String],
    complete: Boolean
)

object PsbtInfo {
  implicit val formatPsbtInfo: OFormat[PsbtInfo] = Json.format[PsbtInfo]
}
case class PsbtInfo(
    psbt: String,
    complete: Boolean
)
