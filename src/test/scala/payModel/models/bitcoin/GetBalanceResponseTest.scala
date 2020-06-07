package payModel.models.bitcoin

import org.scalatest.FunSuite
import play.api.libs.json.Json

class GetBalanceResponseTest extends FunSuite {
  test("json") {
    val result = BigDecimal(0.0002)
    val id = "id"
    val json =
      s"""
         |{
         |  "result" : $result,
         |  "id" : "$id"
         |}
      """.stripMargin
    val r = Json.parse(json).validate[GetBalanceResponse]
    assert(r.isSuccess)
  }
}
