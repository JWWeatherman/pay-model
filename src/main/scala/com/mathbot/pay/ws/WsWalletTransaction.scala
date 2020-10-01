package com.mathbot.pay.ws

import akka.actor.ActorPath
import com.mathbot.pay.bitcoin.WalletTransaction
import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.json.Json

case class WsWalletTransaction(walletTransaction: WalletTransaction, onBehalfOf: ActorPath) extends WebsocketMessage

object WsWalletTransaction extends PlayJsonSupport {
  implicit val formatWsWalletTransaction = Json.format[WsWalletTransaction]
}
