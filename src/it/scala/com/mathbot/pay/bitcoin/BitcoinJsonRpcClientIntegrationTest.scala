package com.mathbot.pay.bitcoin

import com.mathbot.pay.BaseIntegrationTest
import com.softwaremill.macwire.wire
import sttp.client3.akkahttp.AkkaHttpBackend

import java.util.concurrent.atomic.AtomicInteger

//noinspection SpellCheckingInspection
class BitcoinJsonRpcClientIntegrationTest extends BaseIntegrationTest {

  val config = BitcoinJsonRpcConfig(
    baseUrl = sys.env("BITCOIN_HOST"),
    username = sys.env("BITCOIN_RPC_USER"),
    password = sys.env("BITCOIN_RPC_PASS")
  )
  val idGen = new AtomicInteger()
  val service = wire[BitcoinJsonRpcClient]

  def validateRight[T](value: Either[RpcResponseError, T]) = {
    assert(value.isRight, value)
  }

  "BitcoinJsonRpcClientTest" should {
    "estimatesmartfee" in {
      service.estimatesmartfee(1).map(validateRight)
    }
    "getblockchaininfo" in {
      service.getblockchaininfo
        .map(validateRight)

    }
    "getbalances" in {
      service.getbalances.map(validateRight)
    }
    "getbalance" in {
      service.getbalance.map(validateRight)
    }
    "getblockcount" in {
      service.getblockcount.map(validateRight)
    }
    "listtransactions" in {
      service.listtransactions.map(validateRight)
    }
    "sendtoaddress" in pending
    "getreceivedbyaddress" in pending
    "getreceivedbylabel" in pending
    "listreceivedbyaddress" in pending
    "listreceivedbylabel" in pending
    "getblockhash" in pending
    "deriveaddresses" in pending
    "createpsbt" in pending
    "gettransaction" in pending
    "getnewaddress" in pending
    "sendrawtransaction" in pending
    "utxoupdatepsbt" in pending
    "finalizepsbt" in pending
    "walletprocesspsbt" in pending
    "analyzepsbt" in pending
    "combinepsbt" in pending
    "listunspent" in pending
    "getaddressesbylabel" in pending
    "addmultisigaddress" in pending
    "createmultisig" in pending
    "dumpprivkey" in pending
    "settxfee" in pending
    "pruneblockchain" in pending
    "setlabel" in pending
    "importaddress" in pending
    "importpubkey" in pending
    "importprivkey" in pending
    "getaddressinfo" in pending
    "importmulti" in pending
    "getmempoolinfo" in pending
    "rescanblockchain" in pending
    "getwalletinfo" in {
      service.getwalletinfo.map(validateRight)
    }
    "getdescriptorinfo" in pending
    "listwallets" in {
      service.listwallets.map(validateRight)
    }
    "sethdseed" in pending
    "createwallet" in pending
    "dumpwallet" in pending
  }
}
