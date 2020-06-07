package payModel.models.lightning

import org.scalatest.FunSuite
import play.api.libs.json.Json

class ListPaysResponseTest extends FunSuite {

  test("json") {
    val json =
      """
        |{
        |   "jsonrpc": "2.0",
        |   "id": 1,
        |   "result": {
        |     "pays": [
        |       {
        |           "bolt11": "lnbc10n1p0prrmvpp5j73kspfud5d0kmlanzmmfaw2n087f2jc33g40rflhxqs0zkn6qgqdq8w3jhxaqxqr3jscqp2sp5d3nx8r57yqyftl37xupatek3vvrnwkt7xkhk40lz8wj7vjrzthhsrzjq0acy2qchcyruz54fku9y4azjydr64293wxpafqjfv2hapj6sdk3yzy095qqr3qqqyqqqquyqqqqqksqrc9qy9qsq5fken5qak3lfxkkq2hv20fhps9gq04jlc08kfl274rpd4v22at9qrm49lxhmurwtvcnxxfu722teygk9lasf9fd3kg2gjtf4j52dhxcq7rmu0k",
        |           "status": "complete",
        |           "preimage": "ce0ba01346fd03e31243c473c879d1c95873a66444512176662140912024b6b2",
        |           "amount_sent_msat": "2000msat"
        |       }
        |     ]
        |  }
        |}
      """.stripMargin
    val r = Json.parse(json).validate[ListPaysResponse]
    assert(r.isSuccess)
  }
  test("json 2") {
    val json =
      """
        |{"jsonrpc":"2.0","id":0,"result":{"pays":[
        |{"bolt11":"lnbc10n1p0prrn6pp57fquas0r5yc04pvytupj66tlxnr8k7uahst30y57mn9fy9g59j9qdq9u2d2zxqr3jscqp2sp5tjgqshwhlamcwrnjqyfqxz2x7v9dkuepgu3yds5a5zk9z8kk6k4qrzjq2rnwvp7zt9cgeparuqcrqft2kd9dm6a0z6vg0gucrqurutaezrjyz2gevqqxzqqqyqqqqxgqqqqq9qq9q9qy9qsq734saa34edk4a3uahgpjgxqxk2cmtauxx324eqmtfaz4guc8vm5qhj3xp0yjrhfmdrrhx8s5jvt0jx7t0eclmmvuunue0lkldtlrrsgqa6xcfh",
        |"status":"complete",
        |"preimage":"acafe641db55e5879cd03b09759ba6014478e5b057add70fe9bcfa0f48bfe615","amount_sent_msat":"1000msat"}]}}
      """.stripMargin

    val r = Json.parse(json).validate[ListPaysResponse]
    assert(r.isSuccess)
  }
}
