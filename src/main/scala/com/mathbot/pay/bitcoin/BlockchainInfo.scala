package com.mathbot.pay.bitcoin

import play.api.libs.json.Json

object BlockchainInfo {
  implicit val formatBlockchainInfo = Json.format[BlockchainInfo]
}
case class BlockchainInfo(
    chain: String,
    blocks: Long,
    headers: Long,
    bestblockhash: String,
    difficulty: BigDecimal,
    mediantime: Long,
    verificationprogress: BigDecimal,
    initialblockdownload: Boolean,
    chainwork: String,
    size_on_disk: Long,
    pruned: Boolean,
    pruneheight: Option[Long],
    automatic_pruning: Option[Boolean],
    prune_target_size: Option[Double],
//    softforks: Softforks, // testnet seeing an array
    warnings: Option[String]
) {
  def isMain = chain == "main"
  def isTestNet = chain == "test"
}
