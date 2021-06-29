package com.mathbot.pay.lightning

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.funsuite.AnyFunSuite
import play.api.libs.json.Json

class PaidOfferTest extends AnyFunSuite {
  test("response") {
    val json =
      """
        |{"label":"1faf0eaf11af9880f5020ab4fcf26d6e4ff088cd7397d620e6833979cbfe529e-6f40892d01b3a10e65004c95fb2c10d7c6504a1578945c3e07ff64cda5f67036-0","bolt12":"lni1qsspltcw4ug6lxyq75pq4d8u7fkkunls3rxh897kyrngxwtee0l998sgqgp7szsagejk2epqgd5xjcmtv4h8xgzqypcx7mrvdanx2ety9e3k7mgvpkqqqqqqqqqqqqqqqqpgyqq7yzgzx4kjdm7upqf8ymp35x3wp4ep7fsx8hf99tyfmmvzsqpha8kwsf3qdaqgjtgpkwssuegqfj2lktqs6lr9qjs40z29c0s8lajvmf0kwqmzsprqmvyuv23q2jzudtke98rnps62x4fnexl8hntuh8xmz5n22p2nlx0v2gqzryrzuqgjxggqqcx2vaqds2tuh9plu239vdtwauzqje2uwdrtgj5nt9zk9y6n5nnh6xqqem9mlej0w3ze2lh7y3w2lqfvpqa3n3tc2kdh0ec5c7pl725wcdj38sa9097qpahl33p950hctfc","payment_hash":"5485c6aed929c730c34a35533c9be7bcd7cb9cdb1526a50553f99ec520021906","msatoshi":1000,"amount_msat":"1000msat","status":"paid","pay_index":14346,"msatoshi_received":1000,"amount_received_msat":"1000msat","paid_at":1624967638,"payment_preimage":"9eb44af328d9754fce3a8b66f9bb6e703aa9ea218e5adb3b31cef732f3da8289","description":"Feed Chickens @ pollofeed.com","expires_at":1624974822,"local_offer_id":"1faf0eaf11af9880f5020ab4fcf26d6e4ff088cd7397d620e6833979cbfe529e"}""".stripMargin
    val r = Json.parse(json).validate[PaidOffer]
    assert(r.isSuccess)

  }
}
