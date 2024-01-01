package com.mathbot.pay.bitcoin

import com.mathbot.pay.json.PlayJsonSupport
import com.mathbot.pay.lightning.LightningNodeInfo
import fr.acinq.bitcoin.Btc
import play.api.libs.json.{Json, OFormat}

case class GetBalanceResponse(result: Btc, id: String) extends RpcResponse[Btc]
object GetBalanceResponse extends PlayJsonSupport {
  implicit val formatGetBalanceResponse: OFormat[GetBalanceResponse] = Json.format[GetBalanceResponse]
}

case class GetInfoResponse(result: LightningNodeInfo, id: String) extends RpcResponse[LightningNodeInfo]
object GetInfoResponse extends PlayJsonSupport {
  implicit val formatGetInfoResponse: OFormat[GetInfoResponse] = Json.format[GetInfoResponse]
}
