package payModel.models.lightning

import org.scalatest.funsuite.AnyFunSuite
import play.api.libs.json.Json

class PaymentTest extends AnyFunSuite {

  test("json") {

    val stream = getClass.getResourceAsStream("/payments.json")
    val lines = io.Source.fromInputStream(stream).getLines
    val json = Json.parse(lines.mkString)
    stream.close()
    val r = json.validate[Seq[Payment]]
    assert(r.isSuccess)
  }
}
