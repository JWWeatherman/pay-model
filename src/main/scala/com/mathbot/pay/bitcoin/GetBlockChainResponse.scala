package com.mathbot.pay.bitcoin

import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.json.{Json, OFormat}

case class GetBlockChainResponse(result: Int, id: String) extends RpcResponse[Int]
object GetBlockChainResponse extends PlayJsonSupport {
  implicit val formatGetBlockChainResponse: OFormat[GetBlockChainResponse] = Json.format[GetBlockChainResponse]
}

