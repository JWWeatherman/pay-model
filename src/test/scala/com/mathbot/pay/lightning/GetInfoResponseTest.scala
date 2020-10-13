package com.mathbot.pay.lightning

import org.scalatest.FunSuite
import play.api.libs.json.Json

class GetInfoResponseTest extends FunSuite {
  test("getinfo") {
    val str =
      """{"jsonrpc":"2.0","id":0,"result":{"id":"ss","alias":"dfdf","color":"039023","num_peers":54,"num_pending_channels":1,
        |"num_active_channels":52,"num_inactive_channels":0,
        |"address":[{"type":"ipv4","address":"9","port":9735},{"type":"torv3","address":"a.onion","port":9735}],
        |"binding":[{"type":"ipv4","address":"0.0.0.0","port":9735}],
        |"version":"basedon-v0.9.0-1",
        |"blockheight":652563,"network":"bitcoin",
        |"msatoshi_fees_collected":814128,"fees_collected_msat":"814128msat",
        |"lightning-dir":"/root/.lightning/bitcoin"}}""".stripMargin
    val r = Json.parse(str).validate[GetInfoResponse]
    assert(r.isSuccess)

  }
}
