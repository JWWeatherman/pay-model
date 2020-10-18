package com.mathbot.pay.bitcoin

import play.api.libs.json.{Json, OFormat}

case class ListTransactionsResponse(result: Seq[WalletTransaction], id: String)
    extends RpcResponse[Seq[WalletTransaction]]
object ListTransactionsResponse {
  implicit val formatListTransactionsResponse: OFormat[ListTransactionsResponse] =
    Json.format[ListTransactionsResponse]
}
