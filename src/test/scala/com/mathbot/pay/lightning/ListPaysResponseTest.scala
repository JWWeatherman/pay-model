package com.mathbot.pay.lightning

import com.mathbot.pay.utils.ResourceHelper
import org.scalatest.funsuite.AnyFunSuite
import play.api.libs.json.Json

import java.time.Instant

class ListPaysResponseTest extends AnyFunSuite {

  test("json") {
    val json =
      """
        |{
        |   "jsonrpc": "2.0",
        |   "id": 1,
        |   "result": {
        |      "pays": [
        |      {
        |         "bolt11": "lnbc10n1p0prrmvpp5j73kspfud5d0kmlanzmmfaw2n087f2jc33g40rflhxqs0zkn6qgqdq8w3jhxaqxqr3jscqp2sp5d3nx8r57yqyftl37xupatek3vvrnwkt7xkhk40lz8wj7vjrzthhsrzjq0acy2qchcyruz54fku9y4azjydr64293wxpafqjfv2hapj6sdk3yzy095qqr3qqqyqqqquyqqqqqksqrc9qy9qsq5fken5qak3lfxkkq2hv20fhps9gq04jlc08kfl274rpd4v22at9qrm49lxhmurwtvcnxxfu722teygk9lasf9fd3kg2gjtf4j52dhxcq7rmu0k",
        |         "destination": "03902356d26efdc0812726c31a1a2e0d721f26063dd252ac89ded8280037e9ece8",
        |         "payment_hash": "97a368053c6d1afb6ffd98b7b4f5ca9bcfe4aa588c51578d3fb981078ad3d010",
        |         "status": "complete",
        |         "created_at": 1578209138,
        |         "preimage": "ce0ba01346fd03e31243c473c879d1c95873a66444512176662140912024b6b2",
        |         "amount_msat": "2000msat",
        |         "amount_sent_msat": "2000msat"
        |      }
        |   ]
        |  }
        |}
      """.stripMargin
    val r = Json.parse(json).validate[ListPaysResponse]
    assert(r.isSuccess)
  }
  val start = Instant.parse("2018-01-01T00:00:00Z")
  test("json 3 large response") {
    val js = ResourceHelper.read("/listPays.json")
    val result = js.validate[ListPaysResponse]
    assert(result.isSuccess)
    val pays = result.get.result.pays
    val f = pays.map(_.created_at)
    f.foreach(i => assert(i.isAfter(start)))
    val missingBolts = pays.filter(_.bolt11.isEmpty)
    val foo = missingBolts
    assert(true)
  }
}
