package com.mathbot.pay.bitcoin

import com.mathbot.pay.lightning.DecodePayResponse
import com.mathbot.pay.utils.ResourceHelper
import org.scalatest.FunSuite

class BlockchainInfoTest extends FunSuite {
  test("decode main") {
    val js = ResourceHelper.read("/blockchainInfo.json")
    val decodePay = js("result").validate[BlockchainInfo]
    assert(decodePay.isSuccess)
  }
  test("decode testnet") {
    val js = ResourceHelper.read("/blockchainInfoTestnet.json")
    val decodePay = js("result").validate[BlockchainInfo]
    assert(decodePay.isSuccess)
  }

}
