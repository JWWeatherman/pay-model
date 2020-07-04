package com.mathbot.pay.lightning

import org.scalatest.FunSuite
import play.api.libs.json.Json

class ListSendPaysResponseTest extends FunSuite {

  test("json") {

    val json =
      """
        |{
        |"jsonrpc": "test",
        |"id": 1,
        |"result": {
        |   "btcpayserver": [
        |      {
        |         "id": 23,
        |         "payment_hash": "3b431bcca56592809b07fe3da4027af933ccc7a3ad2e9c3079c1cfa3bfec62c4",
        |         "destination": "03c445a245209e328a10806d9e5acf5da8238d8dd0274670a7225a890ab941f456",
        |         "msatoshi": 2000,
        |         "amount_msat": "2000msat",
        |         "msatoshi_sent": 2900,
        |         "amount_sent_msat": "2900msat",
        |         "created_at": 1577910125,
        |         "status": "complete",
        |         "payment_preimage": "bfbb42ca95e2e93032658d444579d76ec913e819a5e127ace33a9f8125e471da",
        |         "bolt11": "lnbc10n1p0qelm9pp58dp3hn99vkfgpxc8lc76gqn6lyeue3ar45hfcvrec88680lvvtzqdq9u2d2zxqr3jscqp2sp5v8ckq3cuxymkqst3czv3cssjww6cqc7vqnjq64gcj3js9y2m4aasrzjqty36649r255qcytf9akhm4ukxhvqklrc3msfd5zkwyfgfr8njjfqz2vdgqqxnsqqqqqqquyqqqqqksqrc9qy9qsqfy36trlla5f6h9ucdwv4vpqfd53s8tud5j3elcqdq8xldjcwqwxx3dtn37ejpatwz93jx8hvku8cwtkq2jg6vtnhmdnd90k524cuzlqpq3pk2w"
        |      }
        |   ]
        |}
        |}
      """.stripMargin
    val r = Json.parse(json).validate[ListSendPaysResponse]

    assert(r.isSuccess)
  }
}
