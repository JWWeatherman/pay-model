package com.mathbot.pay.lightning

import play.api.libs.json.{Json, OFormat}

case class InfoResponse(
id: String,
alias: String,
color: String,
num_peers: Int,
num_pending_channels: Int,
num_active_channels: Int,
num_inactive_channels: Int,
address: Seq[Address],
version: String,
blockheight: Int,
network: String,
msatoshi_fees_collected: Int,
feesCollected_msat: String,
lightningDir: String
)

object InfoResponse {
  lazy implicit val formatInfoResponse: OFormat[InfoResponse] = Json.format[InfoResponse]
}


