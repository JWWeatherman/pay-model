package payModel.models.bitcoin

import org.scalatest.FunSuite

class TxIdTest extends FunSuite {
  test("ToString") {
    val txId = "f4184fc596403b9d638783cf57adfe4c75c605f6356fbc91338530e9831e9e16"

    val r = TxId(txId).toString()
    assert(r === txId)
  }
  test("validateTxId") {
    val txId = "f4184fc596403b9d638783cf57adfe4c75c605f6356fbc91338530e9"
    val txId2 = "f4184fc596403b9d638783cf57adfe4c75c605f6356fbc91338530e9831e9e16"
    val r = TxId.validateTxId(txId)
    val r2 = TxId.validateTxId(txId2)
    assert(r === false)
    assert(r2 === true)
  }
}

