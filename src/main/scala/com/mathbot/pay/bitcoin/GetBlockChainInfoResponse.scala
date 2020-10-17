package com.mathbot.pay.bitcoin

import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.json.{Json, OFormat}

case class GetBlockChainInfoResponse(result: BlockchainInfo, id: String) extends RpcResponse[BlockchainInfo] {}
object GetBlockChainInfoResponse extends PlayJsonSupport {
  implicit val formatGetBlockChainResponse: OFormat[GetBlockChainInfoResponse] = Json.format[GetBlockChainInfoResponse]
}

case class BitcoinRpcResponse[T](result: T, id: String) extends RpcResponse[T]
