package com.mathbot.pay.lightning

import com.mathbot.pay.bitcoin.CallbackURL
import org.scalatest.FunSuite
import play.api.libs.json.Json

class LightningDebitRequestTest extends FunSuite {
  test("json") {

    val d = LightningDebitRequest(
      Bolt11(
        "lnbc10u1p0gm7h8pp5d5ltqpvq66m4uu48n30j96xjx0kwcg79m9ey5cjz9qu3gfrwj3mqdp0gejk2epqgd5xjcmtv4h8xgzqypcx7mrvdanx2ety9e3k7mgxqzjccqp2sp5w7snnw2ur8g8mq6lyqe6ns6yad9d5ydv58duv82pyh6yqu0qpjaqrzjqd04ydkhumrdzcg8c8uxu3g5umxd66evz0p2hswh4q7dymktfswsuzxpugqqp3cqqyqqqqqqqqqqpjqqrc9qy9qsqw2qafvd5uh0geam2us93elq3580wluhjmux22uye6w0qjtjcshjxpzxc4ew3n7cqgaenup6rq0c29lcwh23lwvd48f99xrfwp8qkffqqxgpzah"
      ),
      CallbackURL("http://example.com")
    )
    val j = Json.toJson(d)
    println(j)
  }
}
