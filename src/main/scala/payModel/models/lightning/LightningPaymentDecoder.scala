package payModel.models.lightning

import payModel.models.MilliSatoshi

object LightningPaymentDecoder {

  @throws(classOf[RuntimeException])
  def parseAmount(bolt11: String): MilliSatoshi = {
    val idx = bolt11.lastIndexOf("1")
    val regex = s"${LightningPrefix.mainNet.name}|${LightningPrefix.testNet.name}".r
    val raw = regex.replaceAllIn(bolt11.take(idx), "")
    raw.toLowerCase match {
      case empty if raw.isEmpty => throw new RuntimeException("No amount")
      case a if a.last == 'p' => MilliSatoshi(a.dropRight(1).toLong / 10L) // 1 pico-bitcoin == 10 milli-satoshi
      case a if a.last == 'n' => MilliSatoshi(a.dropRight(1).toLong * 100L)
      case a if a.last == 'u' => MilliSatoshi(a.dropRight(1).toLong * 100000L)
      case a if a.last == 'm' => MilliSatoshi(a.dropRight(1).toLong * 100000000L)
      case a => MilliSatoshi(a.toLong * 100000000000L)
    }
  }
}
