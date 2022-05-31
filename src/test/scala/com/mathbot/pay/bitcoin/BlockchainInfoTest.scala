package com.mathbot.pay.bitcoin

import com.mathbot.pay.utils.ResourceHelper
import org.scalatest.funsuite.AnyFunSuite

class BlockchainInfoTest extends AnyFunSuite {
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

  test("decode testnet 2") {
    val js = ResourceHelper.read("/infoTn2.json")
    val decodePay = js("result").validate[BlockchainInfo]
    assert(decodePay.isSuccess)
  }

}
