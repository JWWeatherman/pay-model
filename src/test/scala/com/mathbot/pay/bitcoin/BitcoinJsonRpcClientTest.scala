package com.mathbot.pay.bitcoin

import org.scalatest.EitherValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.json.{JsArray, JsString, JsValue, Json}
import sttp.client3.StringBody
import sttp.client3.testing._

import java.text.DecimalFormat
import java.time.Instant

class BitcoinJsonRpcClientTest extends AsyncWordSpec with MockitoSugar with EitherValues with Matchers {

  val config = BitcoinJsonRpcConfig(baseUrl = "http://test.com", username = "username", password = "password")

  "BitcoinJsonRpcClient" should {
    val amt = BigDecimal(0.001)
    "get balance" in {

      val be = SttpBackendStub.asynchronousFuture
        .whenRequestMatches(request => {
          request.body match {
            case s: StringBody =>
              val j = Json.parse(s.s)
              j("method").as[String] === "getbalance"
            case _ => false
          }
        })
        .thenRespond("""{"result": 0.001, "id": "id" }""")
      val client = BitcoinJsonRpcClient(config, be)
      for {
        b <- client.getbalance
      } yield {
        val r = b.right.value
        assert(r.isInstanceOf[Btc])
        assert(r.toDouble === amt)
      }
    }

    "get wallet transaction" in {
      val txid = TxId("d6e889709bdd03b3dea86e4a01e6d12a51cdf4c97ac0d9f6db0fd41cea9f5109")
      val walletTransaction =
        WalletTransaction(
          amount = Btc(0.01),
          confirmations = 1,
          txid = txid,
          time = Instant.now(),
          timereceived = Instant.now(),
          details = None,
          hex = None
        )
      val be = SttpBackendStub.asynchronousFuture
        .whenRequestMatches(request => {

          request.body match {
            case s: StringBody =>
              val j = Json.parse(s.s)
              val a = j("method").as[String] === "gettransaction"
              val b = j("params").as[Seq[String]].head == txid.toString
              a && b
            case _ => false
          }
        })
        .thenRespond(Json.obj("result" -> walletTransaction, "id" -> "1", "error" -> null).toString())
      val client = BitcoinJsonRpcClient(config, be)
      for {
        t <- client.gettransaction(txid)
      } yield {
        val wt = t.right.value
        assert(wt.txid === walletTransaction.txid)
        assert(wt.amount === walletTransaction.amount)
      }
    }

    "send to address" in {
      val btcAddress = BtcAddress("tb1q89dagnf6g0reveq57z7yzmrer47tmtwj9s73qh")
      val amount = Satoshi(1000).toBtc
      val btcFormat = new DecimalFormat("0.########")
      val strAmount = btcFormat.format(amount.toDouble)
      val txId = TxId("d6e889709bdd03b3dea86e4a01e6d12a51cdf4c97ac0d9f6db0fd41cea9f5109")
      val jsonRpcRequestBody = Json
        .toJson(JsonRpcRequestBody("sendtoaddress", Json.arr(btcAddress.toString, strAmount)))
        .toString
      val be = SttpBackendStub.asynchronousFuture
        .whenRequestMatches(request => {
          request.body match {
            case s: StringBody =>
              val j = Json.parse(s.s)
              val a = j("method").as[String] == "sendtoaddress"
              val p = j("params").as[Seq[JsValue]].head.as[JsString]
              val b = p.value == btcAddress.toString
              a && b
            case _ => false
          }
        })
        .thenRespond(
          """{"result": "d6e889709bdd03b3dea86e4a01e6d12a51cdf4c97ac0d9f6db0fd41cea9f5109", "id": "id" }"""
        )
      val client = BitcoinJsonRpcClient(config, be)
      for {
        r <- client.sendtoaddress(btcAddress, amount)
      } yield {
        assert(r.right.value === txId)
      }
    }
  }
}
