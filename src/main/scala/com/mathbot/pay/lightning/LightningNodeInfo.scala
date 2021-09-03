package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class LightningNodeInfo(
    id: String,
    alias: String,
    color: String,
    num_peers: Int,
    num_pending_channels: Int,
    num_active_channels: Int,
    num_inactive_channels: Int,
//    address: Seq[Address],
    version: String,
    blockheight: Int,
    network: String,
    msatoshi_fees_collected: Int,
    fees_collected_msat: String,
    `lightning-dir`: String
) {
  override def toString: String = Json.toJson(this).toString()
}

object LightningNodeInfo {
  lazy implicit val formatInfoResponse: OFormat[LightningNodeInfo] = Json.format[LightningNodeInfo]
}
