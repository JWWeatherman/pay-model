package payModel.models.bitcoin

import org.scalatest.FunSuite
import play.api.libs.json.Json

class ValidateAddressTest extends FunSuite {
  test("json") {
    val address = "3J98t1WpEZ73CNmQviecrnyiWrnqRhWNLy"
    val isscript = false
    val isvalid = true
    val scriptPubKey = "randomscriptpubkey"
    val iswitness = false
    val json =
      s"""
         |{
         |  "address" : "$address",
         |  "isscript" : $isscript,
         |  "isvalid" : $isvalid,
         |  "scriptPubKey" : "$scriptPubKey",
         |  "iswitness" : $iswitness
         |}
      """.stripMargin


    val r = Json.parse(json).validate[ValidateAddress]
    assert(r.isSuccess)
  }
}
