package com.mathbot.pay.bitcoin

import com.mathbot.pay.json.PlayJsonSupport
import play.api.libs.json.{JsValue, Json, OFormat}

case class GetBlockChainInfoResponse(result: JsValue, id: String) extends RpcResponse[JsValue] {

  def getBlocks: Option[Int] = result("blocks").asOpt[Int]
}
object GetBlockChainInfoResponse extends PlayJsonSupport {
  implicit val formatGetBlockChainResponse: OFormat[GetBlockChainInfoResponse] = Json.format[GetBlockChainInfoResponse]
}
