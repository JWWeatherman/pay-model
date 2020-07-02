package com.mathbot.pay.bitcoin

import play.api.libs.json.{Json, OFormat}

case class WalletTransactionResponse(result: WalletTransaction, id: String) extends RpcResponse[WalletTransaction]
object WalletTransactionResponse {
  implicit val formatWalletTransactionResponse: OFormat[WalletTransactionResponse] =
    Json.format[WalletTransactionResponse]
}
