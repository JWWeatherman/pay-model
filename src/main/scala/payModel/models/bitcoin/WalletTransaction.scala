package payModel.models.bitcoin

import play.api.libs.json.{Json, OFormat}

case class WalletTransaction(amount: Double,
                             confirmations: Int,
                             txid: TxId,
                             time: Long,
                             timereceived: Long,
                             details: Seq[Detail],
                             hex: String)

object WalletTransaction {
  implicit val formatWalletTransactionPlay: OFormat[WalletTransaction] = Json.format[WalletTransaction]
}
