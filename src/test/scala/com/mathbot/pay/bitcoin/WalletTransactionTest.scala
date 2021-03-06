package com.mathbot.pay.bitcoin

import org.scalatest.EitherValues
import play.api.libs.json.Json
import org.scalatest.funsuite.AnyFunSuite

class WalletTransactionTest extends AnyFunSuite with EitherValues {

  test("json reading file") {
    val stream = getClass.getResourceAsStream("/testnetWalletTransactions.json")
    val lines = io.Source.fromInputStream(stream).getLines
    val json = Json.parse(lines.mkString)
    stream.close()
    val r = json.validate[Seq[WalletTransaction]]
    assert(r.isSuccess)
  }
}
