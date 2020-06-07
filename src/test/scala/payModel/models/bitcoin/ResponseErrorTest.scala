package payModel.models.bitcoin

import org.scalatest.FunSuite
import play.api.libs.json.Json

class ResponseErrorTest extends FunSuite {
  test("json") {
    val message = "random message"
    val code = 1
    val json =
      s"""
         |{
         |  "message" : "$message",
         |  "code" : $code
         |}
      """.stripMargin


    val r = Json.parse(json).validate[ResponseError]
    assert(r.isSuccess)
  }

}
