package com.mathbot.pay.btcpayserver

import play.api.libs.json.{JsResultException, Json}

class ChargeInfoResponseTest extends org.scalatest.FunSuite {

  test("json parse") {

    val stream = getClass.getResourceAsStream("/btcpayserverInvoiceResponse.json")
    val lines = io.Source.fromInputStream(stream).getLines
    val json = Json.parse(lines.mkString)
    stream.close()
    val r = json.validate[ChargeInfoResponse]
    assert(r.isSuccess)
  }

  test("fails when no derivation scheme") {

    val json =
      """
        |{"facade":"pos/invoice",
        |"data":{
        |"url":"https://payments.mathbot.com/invoice?id=6Rh3rp3TRnbia3zYho9buK",
        |"posData":null,
        |"status":"new",
        |"btcPrice":null,"btcDue":null,
        |"cryptoInfo":[{"cryptoCode":"BTC","paymentType":"LightningLike","rate":7160.19770442063,"exRates":{"USD":0},"paid":"0.00000000","price":"0.00013967","due":"0.00013967","paymentUrls":{"BIP21":null,"BIP72":null,"BIP72b":null,"BIP73":null,"BOLT11":"lightning:lnbc139670n1pwlu09epp5h8sd2jh7hyyjp4nzxmknfw5hmzynv74wp36wnwuf4c36vl84gqpsdzl2pskjepqw3hjqmtpw35xymm5ypehgmmjv5szsnmjv3jhygzfgsazqmtpw35xymm59e3k7mfq95sxvatwvss8qmrp09jhy2gxqzurcqp2nhyvtnkdatt6wzjux2928ygrsjhvmsxwtgdar4ec4qhf55cf0wzszxgemvqg0na9xapulrhhlpn3jrezum7nvdegy6j42l0f7pnkd2sqf3d9xq"},"address":"lnbc139670n1pwlu09epp5h8sd2jh7hyyjp4nzxmknfw5hmzynv74wp36wnwuf4c36vl84gqpsdzl2pskjepqw3hjqmtpw35xymm5ypehgmmjv5szsnmjv3jhygzfgsazqmtpw35xymm59e3k7mfq95sxvatwvss8qmrp09jhy2gxqzurcqp2nhyvtnkdatt6wzjux2928ygrsjhvmsxwtgdar4ec4qhf55cf0wzszxgemvqg0na9xapulrhhlpn3jrezum7nvdegy6j42l0f7pnkd2sqf3d9xq","url":"https://payments.mathbot.com/i/BTC_LightningLike/6Rh3rp3TRnbia3zYho9buK","totalDue":"0.00013967","networkFee":"0.00000000","txCount":0,"cryptoPaid":"0.00000000","payments":[]}],"price":1,"currency":"USD","exRates":null,"buyerTotalBtcAmount":null,"itemDesc":"mathbot.com - fund player","itemCode":null,"orderId":"mathbot.com - fund player","guid":"d9c6967e-642c-4364-b46c-9e798583a97f","id":"6Rh3rp3TRnbia3zYho9buK","invoiceTime":1576942777000,"expirationTime":1576943677000,"currentTime":1576942777492,"lowFeeDetected":false,"btcPaid":null,"rate":0,"exceptionStatus":false,"paymentUrls":null,"refundAddressRequestPending":false,"buyerPaidBtcMinerFee":null,"bitcoinAddress":null,"token":"STmedpHxaT9bqzvFewweg8","flags":{"refundable":false},"paymentSubtotals":{"BTC_LightningLike":13967},"paymentTotals":{"BTC_LightningLike":13967},"amountPaid":0,"minerFees":{},"exchangeRates":{"BTC":{"USD":0}},"supportedTransactionCurrencies":{"BTC":{"enabled":true}},"addresses":{"BTC_LightningLike":"lnbc139670n1pwlu09epp5h8sd2jh7hyyjp4nzxmknfw5hmzynv74wp36wnwuf4c36vl84gqpsdzl2pskjepqw3hjqmtpw35xymm5ypehgmmjv5szsnmjv3jhygzfgsazqmtpw35xymm59e3k7mfq95sxvatwvss8qmrp09jhy2gxqzurcqp2nhyvtnkdatt6wzjux2928ygrsjhvmsxwtgdar4ec4qhf55cf0wzszxgemvqg0na9xapulrhhlpn3jrezum7nvdegy6j42l0f7pnkd2sqf3d9xq"},"paymentCodes":{"BTC_LightningLike":{"BIP21":null,"BIP72":null,"BIP72b":null,"BIP73":null,"BOLT11":"lightning:lnbc139670n1pwlu09epp5h8sd2jh7hyyjp4nzxmknfw5hmzynv74wp36wnwuf4c36vl84gqpsdzl2pskjepqw3hjqmtpw35xymm5ypehgmmjv5szsnmjv3jhygzfgsazqmtpw35xymm59e3k7mfq95sxvatwvss8qmrp09jhy2gxqzurcqp2nhyvtnkdatt6wzjux2928ygrsjhvmsxwtgdar4ec4qhf55cf0wzszxgemvqg0na9xapulrhhlpn3jrezum7nvdegy6j42l0f7pnkd2sqf3d9xq"}},"buyer":{"name":"test","address1":null,"address2":null,"locality":null,"region":null,"postalCode":null,"country":null,"phone":null,"email":"test@gmail.com"}}}
      """.stripMargin

    assertThrows[JsResultException] {
      Json.parse(json).as[ChargeInfoResponse]
    }

  }

  test("json parsing") {

    val json =
      """
        |{"facade":"pos/invoice","data":{"url":"https://payments.mathbot.com/invoice?id=F2vU2rFGpBv3ZY1R1qrVCp","posData":null,"status":"new","btcPrice":"0.00014048","btcDue":"0.00014048","cryptoInfo":[{"cryptoCode":"BTC","paymentType":"BTCLike","rate":7118.82364667061,"exRates":{"USD":0.0},"paid":"0.00000000","price":"0.00014048","due":"0.00014048","paymentUrls":{"BIP21":"bitcoin:tb1q3td5v9knh4unsnvs04c07r4035jplecqev59xc?amount=0.00014048","BIP72":null,"BIP72b":null,"BIP73":null,"BOLT11":null},"address":"tb1q3td5v9knh4unsnvs04c07r4035jplecqev59xc","url":"https://payments.mathbot.com/i/BTC/F2vU2rFGpBv3ZY1R1qrVCp","totalDue":"0.00014048","networkFee":"0.00000000","txCount":0,"cryptoPaid":"0.00000000","payments":[]},{"cryptoCode":"BTC","paymentType":"LightningLike","rate":7118.82364667061,"exRates":{"USD":0.0},"paid":"0.00000000","price":"0.00014048","due":"0.00014048","paymentUrls":{"BIP21":null,"BIP72":null,"BIP72b":null,"BIP73":null,"BOLT11":"lightning:lntb140480n1pwlvcyfpp5cp3ue8c9lrmgd7vc8t6rq6tew8a6rdv644jmgmzc4xlkmy5wtphqdzl2pskjepqw3hjqmtpw35xymm5ypehgmmjv5szsnmjv3jhygzfgsazqmtpw35xymm59e3k7mfq95sxvatwvss8qmrp09jhy2gxqzurcqp2rzjqwyx8nu2hygyvgc02cwdtvuxe0lcxz06qt3lpsldzcdr46my5epmjxye4gqqqtgqqqqqqqlgqqqqqqgq9qsfplnpznjl08rkqc938ex3nj0zjt28yre0wmwkwhmhjzz7505razf28avunnl0ktnv7l3x6dkk3w6t0talthjq8tkvynf07ul2w8aggqvuedgv"},"address":"lntb140480n1pwlvcyfpp5cp3ue8c9lrmgd7vc8t6rq6tew8a6rdv644jmgmzc4xlkmy5wtphqdzl2pskjepqw3hjqmtpw35xymm5ypehgmmjv5szsnmjv3jhygzfgsazqmtpw35xymm59e3k7mfq95sxvatwvss8qmrp09jhy2gxqzurcqp2rzjqwyx8nu2hygyvgc02cwdtvuxe0lcxz06qt3lpsldzcdr46my5epmjxye4gqqqtgqqqqqqqlgqqqqqqgq9qsfplnpznjl08rkqc938ex3nj0zjt28yre0wmwkwhmhjzz7505razf28avunnl0ktnv7l3x6dkk3w6t0talthjq8tkvynf07ul2w8aggqvuedgv","url":"https://payments.mathbot.com/i/BTC_LightningLike/F2vU2rFGpBv3ZY1R1qrVCp","totalDue":"0.00014048","networkFee":"0.00000000","txCount":0,"cryptoPaid":"0.00000000","payments":[]}],"price":1.0,"currency":"USD","exRates":{"USD":0.0},"buyerTotalBtcAmount":null,"itemDesc":"mathbot.com - fund player","itemCode":null,"orderId":"mathbot.com - fund player","guid":"cbaf9ddb-03da-427a-b09f-0c63cc583418","id":"F2vU2rFGpBv3ZY1R1qrVCp","invoiceTime":1576427657000,"expirationTime":1576428557000,"currentTime":1576427657575,"lowFeeDetected":false,"btcPaid":"0.00000000","rate":7118.82364667061,"exceptionStatus":false,"paymentUrls":{"BIP21":"bitcoin:tb1q3td5v9knh4unsnvs04c07r4035jplecqev59xc?amount=0.00014048","BIP72":null,"BIP72b":null,"BIP73":null,"BOLT11":null},"refundAddressRequestPending":false,"buyerPaidBtcMinerFee":null,"bitcoinAddress":"tb1q3td5v9knh4unsnvs04c07r4035jplecqev59xc","token":"51uRuhyLXTV236HayJeuvX","flags":{"refundable":false},"paymentSubtotals":{"BTC":14048,"BTC_LightningLike":14048},"paymentTotals":{"BTC":14048,"BTC_LightningLike":14048},"amountPaid":0,"minerFees":{"BTC":{"satoshisPerByte":1.0,"totalFee":0}},"exchangeRates":{"BTC":{"USD":0.0}},"supportedTransactionCurrencies":{"BTC":{"enabled":true}},"addresses":{"BTC":"tb1q3td5v9knh4unsnvs04c07r4035jplecqev59xc","BTC_LightningLike":"lntb140480n1pwlvcyfpp5cp3ue8c9lrmgd7vc8t6rq6tew8a6rdv644jmgmzc4xlkmy5wtphqdzl2pskjepqw3hjqmtpw35xymm5ypehgmmjv5szsnmjv3jhygzfgsazqmtpw35xymm59e3k7mfq95sxvatwvss8qmrp09jhy2gxqzurcqp2rzjqwyx8nu2hygyvgc02cwdtvuxe0lcxz06qt3lpsldzcdr46my5epmjxye4gqqqtgqqqqqqqlgqqqqqqgq9qsfplnpznjl08rkqc938ex3nj0zjt28yre0wmwkwhmhjzz7505razf28avunnl0ktnv7l3x6dkk3w6t0talthjq8tkvynf07ul2w8aggqvuedgv"},"paymentCodes":{"BTC":{"BIP21":"bitcoin:tb1q3td5v9knh4unsnvs04c07r4035jplecqev59xc?amount=0.00014048","BIP72":null,"BIP72b":null,"BIP73":null,"BOLT11":null},"BTC_LightningLike":{"BIP21":null,"BIP72":null,"BIP72b":null,"BIP73":null,"BOLT11":"lightning:lntb140480n1pwlvcyfpp5cp3ue8c9lrmgd7vc8t6rq6tew8a6rdv644jmgmzc4xlkmy5wtphqdzl2pskjepqw3hjqmtpw35xymm5ypehgmmjv5szsnmjv3jhygzfgsazqmtpw35xymm59e3k7mfq95sxvatwvss8qmrp09jhy2gxqzurcqp2rzjqwyx8nu2hygyvgc02cwdtvuxe0lcxz06qt3lpsldzcdr46my5epmjxye4gqqqtgqqqqqqqlgqqqqqqgq9qsfplnpznjl08rkqc938ex3nj0zjt28yre0wmwkwhmhjzz7505razf28avunnl0ktnv7l3x6dkk3w6t0talthjq8tkvynf07ul2w8aggqvuedgv"}},"buyer":{"name":"test","address1":null,"address2":null,"locality":null,"region":null,"postalCode":null,"country":null,"phone":null,"email":"test@gmail.com"}}}
      """.stripMargin

    val result = Json.parse(json).validate[ChargeInfoResponse]

    assert(result.isSuccess)
  }
}
