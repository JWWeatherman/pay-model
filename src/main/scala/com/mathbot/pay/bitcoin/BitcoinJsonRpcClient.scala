package com.mathbot.pay.bitcoin

import com.mathbot.pay.bitcoin.AddressType.AddressType
import com.mathbot.pay.bitcoin.Btc.stringify
import com.mathbot.pay.bitcoin.EstimateFeeMode.EstimateFeeMode
import com.mathbot.pay.bitcoin.SigHashType.SigHashType
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import sttp.client._
import sttp.model.MediaType

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.{ExecutionContext, Future}

case class BitcoinJsonRpcClient(
    config: BitcoinJsonRpcConfig,
    ec: ExecutionContext,
    backend: SttpBackend[Future, Nothing, NothingT],
    logger: Logger = LoggerFactory.getLogger("BitcoinJsonRpcClient")
) {
  private implicit val _ec: ExecutionContext = ec
  implicit val sttpBackend: SttpBackend[Future, Nothing, NothingT] = backend
  private val idGen = new AtomicInteger(1)
  private def id = idGen.getAndIncrement()

  /**
   * @param method to call eg getbalance, createpsbt...
   * @param params arguments to send
   * @param jsonReader json formatter for the response [[T]]
   * @tparam T the expected response from a successful call
   * @return Error or the method's expected response object
   */
  def send[T](method: String, params: JsValueWrapper*)(implicit
      jsonReader: Reads[T]
  ): Future[Either[RpcResponseError, T]] = {
    val ID = id.toString
    val body = Json
      .toJson(JsonRpcRequestBody(method = method, params = Json.arr(params: _*), jsonrpc = config.jsonRpc, id = ID))
      .toString
    val req = basicRequest
      .post(uri"${config.baseUrl}")
      .contentType(MediaType.TextPlain)
      .acceptEncoding(MediaType.ApplicationJson.toString)
      .auth
      .basic(user = config.username, password = config.password)
      .body(body)
      .response(asStringAlways.map(Json.parse))
    logger.debug(req.toCurl)
    (for {
      response <- req.send()
    } yield {
      logger.debug(s"Response code = {} body = {}", response.code, response.body)
      val validResult = (response.body \ "result").validate[T]
      val idJ = (response.body \ "id").validate[String]
      val errorOpt = response.body \ "error" match {
        case _: JsUndefined => None
        case JsDefined(JsNull) => None
        case JsDefined(e) => Some(e)
      }
      (validResult, idJ, errorOpt) match {
        case (JsSuccess(value, _), JsSuccess(_, _), None) => Right(value)
        case _ =>
          response.body
            .validate[RpcResponseError] match {
            case JsSuccess(value, _) => Left(value)
            case JsError(_) =>
              val message = s"Unknown bitcoin rpc response = ${response.body}"
              logger.error(message)
              Left(
                RpcResponseError(ID, error = ResponseError(code = response.code.code, message = message))
              )
          }
      }
    }) recover {
      case err =>
        logger.error(s"Error in bitcoin rpc call $err")
        Left(
          RpcResponseError(id = ID, error = ResponseError(code = 500, message = "Internal server error"))
        )
    }
  }

  /**
   * sendtoaddress "address" amount ( "comment" "comment_to" subtractfeefromamount replaceable conf_target "estimate_mode" avoid_reuse )
   *
   * Send an amount to a given address.
   *
   * Arguments:
   * 1. address                  (string, required) The bitcoin address to send to.
   * 2. amount                   (numeric or string, required) The amount in BTC to send. eg 0.1
   * 3. comment                  (string, optional) A comment used to store what the transaction is for.
   * This is not part of the transaction, just kept in your wallet.
   * 4. comment_to               (string, optional) A comment to store the name of the person or organization
   * to which you're sending the transaction. This is not part of the
   * transaction, just kept in your wallet.
   * 5. subtractfeefromamount    (boolean, optional, default=false) The fee will be deducted from the amount being sent.
   * The recipient will receive less bitcoins than you enter in the amount field.
   * 6. replaceable              (boolean, optional, default=wallet default) Allow this transaction to be replaced by a transaction with higher fees via BIP 125
   * 7. conf_target              (numeric, optional, default=wallet default) Confirmation target (in blocks)
   * 8. estimate_mode            (string, optional, default=UNSET) The fee estimate mode, must be one of:
   * "UNSET"
   * "ECONOMICAL"
   * "CONSERVATIVE"
   * 9. avoid_reuse              (boolean, optional, default=true) (only available if avoid_reuse wallet flag is set) Avoid spending from dirty addresses; addresses are considered
   * dirty if they have previously been used in a transaction.
   *
   * Result:
   * "txid"                  (string) The transaction id.
   *
   * @param address
   * @param btc
   * @return
   */
  def sendtoaddress(
      address: BtcAddress,
      btc: Btc,
      comment: Option[String] = None,
      comment_to: Option[String] = None,
      subtractfeefromamount: Option[Boolean] = None,
      replaceable: Option[Boolean] = None,
      conf_target: Option[Int] = None,
      estimate_mode: Option[EstimateFeeMode] = None,
      avoid_reuse: Option[Boolean] = None
  ): Future[Either[RpcResponseError, TxId]] = {
    send[TxId](
      method = "sendtoaddress",
      address,
      btc,
      comment,
      comment_to,
      subtractfeefromamount,
      replaceable,
      conf_target,
      estimate_mode,
      avoid_reuse
    )
  }

  def getreceivedbyaddress(address: BtcAddress, minconf: Option[Int] = None) =
    send[JsValue]("getreceivedbyaddress", address, minconf)
  def getreceivedbylabel(label: String, minconf: Option[Int] = None) =
    send[JsValue]("getreceivedbylabel", label, minconf)
  def listreceivedbyaddress(
      minconf: Option[Int] = None,
      include_empty: Option[Boolean] = None,
      include_watchonly: Option[Boolean] = None,
      address_filter: Option[BtcAddress] = None
  ) =
    send[Seq[ListReceivedByAddress]]("listreceivedbyaddress", minconf, include_empty, include_watchonly, address_filter)

  def listreceivedbylabel(
      minconf: Option[Int] = None,
      include_empty: Option[Boolean] = None,
      include_watchonly: Option[Boolean] = None
  ) =
    send[JsValue]("listreceivedbylabel", minconf, include_empty, include_watchonly)

  def getbalance: Future[Either[RpcResponseError, Btc]] =
    send[Btc]("getbalance")

  def getbalances: Future[Either[RpcResponseError, OnChainBalance]] =
    send[OnChainBalance]("getbalances")

  def getblockhash(block: Int): Future[Either[RpcResponseError, String]] =
    send[String]("getblockhash", block)

  def getnetworkinfo: Future[Either[RpcResponseError, NetworkInfo]] =
    send[NetworkInfo]("getnetworkinfo")

  def getblockchaininfo: Future[Either[RpcResponseError, BlockchainInfo]] =
    send[BlockchainInfo]("getblockchaininfo")

  // todo: create json writes for outputs
  /**
   * createpsbt [{"txid":"hex","vout":n,"sequence":n},...] [{"address":amount},{"data":"hex"},...] ( locktime replaceable )
   *
   * Creates a transaction in the Partially Signed Transaction format.
   * Implements the Creator role.
   *
   * Arguments:
   * 1. inputs                      (json array, required) A json array of json objects
   * [
   * {                       (json object)
   * "txid": "hex",        (string, required) The transaction id
   * "vout": n,            (numeric, required) The output number
   * "sequence": n,        (numeric, optional, default=depends on the value of the 'replaceable' and 'locktime' arguments) The sequence number
   * },
   * ...
   * ]
   * 2. outputs                     (json array, required) a json array with outputs (key-value pairs), where none of the keys are duplicated.
   * That is, each address can only appear once and there can only be one 'data' object.
   * For compatibility reasons, a dictionary, which holds the key-value pairs directly, is also
   * accepted as second parameter.
   * [
   * {                       (json object)
   * "address": amount,    (numeric or string, required) A key-value pair. The key (string) is the bitcoin address, the value (float or string) is the amount in BTC
   * },
   * {                       (json object)
   * "data": "hex",        (string, required) A key-value pair. The key must be "data", the value is hex-encoded data
   * },
   * ...
   * ]
   * 3. locktime                    (numeric, optional, default=0) Raw locktime. Non-0 value also locktime-activates inputs
   * 4. replaceable                 (boolean, optional, default=false) Marks this transaction as BIP125 replaceable.
   * Allows this transaction to be replaced by a transaction with higher fees. If provided, it is an error if explicit sequence numbers are incompatible.
   *
   * Result:
   * "psbt"        (string)  The resulting raw transaction (base64-encoded string)
   *
   * @param inputs
   * @param outputs
   * @return
   */
  def createpsbt(
      inputs: Seq[Input],
      outputs: Map[BtcAddress, Btc],
      locktime: Option[Int] = None,
      replaceable: Option[Boolean] = None
  ): Future[Either[RpcResponseError, String]] =
    send[String]("createpsbt", inputs, outputs.map { case (a, b) => a.address -> stringify(b) }, locktime, replaceable)

  def getblockcount: Future[Either[RpcResponseError, Int]] =
    send[Int]("getblockcount")

  def listtransactions: Future[Either[RpcResponseError, Seq[WalletTransaction]]] =
    send[Seq[WalletTransaction]]("listtransactions")

  /**
   * Get tx info for wallet
   * This cannot fetch tx's not sent from wallet
   *
   * @param txId
   * @return
   */
  def gettransaction(txId: TxId): Future[Either[RpcResponseError, WalletTransaction]] =
    send[WalletTransaction](method = "gettransaction", txId)

  def getnewaddress(
      label: Option[String] = None,
      addressType: Option[AddressType] = None
  ): Future[Either[RpcResponseError, BtcAddress]] =
    send[BtcAddress](method = "getnewaddress", label, addressType)

  def sendrawtransaction(transaction: String): Future[Either[RpcResponseError, String]] =
    send[String]("sendrawtransaction", transaction)

  /**
   * utxoupdatepsbt "psbt" ( ["",{"desc":"str","range":n or [n,n]},...] )
   *
   * Updates all segwit inputs and outputs in a PSBT with data from output descriptors, the UTXO set or the mempool.
   *
   * Arguments:
   * 1. psbt                          (string, required) A base64 string of a PSBT
   * 2. descriptors                   (json array, optional) An array of either strings or objects
   * [
   * "",                       (string) An output descriptor
   * {                         (json object) An object with an output descriptor and extra information
   * "desc": "str",          (string, required) An output descriptor
   * "range": n or [n,n],    (numeric or array, optional, default=1000) Up to what index HD chains should be explored (either end or [begin,end])
   * },
   * ...
   * ]
   *
   * Result:
   * "psbt"          (string) The base64-encoded partially signed transaction with inputs updated
   *
   * @param psbt
   * @return
   */
  // todo: descriptors
  def utxoupdatepsbt(psbt: String) = send[String]("utxoupdatepsbt", psbt)
  def finalizepsbt(psbt: String): Future[Either[RpcResponseError, FinalizedPsbtResponse]] =
    send[FinalizedPsbtResponse]("finalizepsbt", psbt)

  /**
   * walletprocesspsbt "psbt" ( sign "sighashtype" bip32derivs )
   *
   * Update a PSBT with input information from our wallet and then sign inputs
   * that we can sign for.
   *
   * Arguments:
   * 1. psbt           (string, required) The transaction base64 string
   * 2. sign           (boolean, optional, default=true) Also sign the transaction when updating
   * 3. sighashtype    (string, optional, default=ALL) The signature hash type to sign with if not specified by the PSBT. Must be one of
   * "ALL"
   * "NONE"
   * "SINGLE"
   * "ALL|ANYONECANPAY"
   * "NONE|ANYONECANPAY"
   * "SINGLE|ANYONECANPAY"
   * 4. bip32derivs    (boolean, optional, default=false) If true, includes the BIP 32 derivation paths for public keys if we know them
   *
   * Result:
   * {
   * "psbt" : "value",          (string) The base64-encoded partially signed transaction
   * "complete" : true|false,   (boolean) If the transaction has a complete set of signatures
   * ]
   * }
   *
   * @param psbt
   * @param sign
   * @param sighashType
   * @param bip32derivs
   * @return
   */
  def walletprocesspsbt(
      psbt: String,
      sign: Option[Boolean] = None,
      sighashType: Option[SigHashType] = None,
      bip32derivs: Option[Boolean] = None
  ): Future[Either[RpcResponseError, PsbtInfo]] =
    send[PsbtInfo]("walletprocesspsbt", psbt, sign, sighashType, bip32derivs)
  def analyzepsbt(psbt: String): Future[Either[RpcResponseError, AnalyzePsbt]] = send[AnalyzePsbt]("analyzepsbt", psbt)
  def combinepsbt(psbt: String*): Future[Either[RpcResponseError, String]] = send[String]("combinepsbt", psbt)

  def listunspent: Future[Either[RpcResponseError, Seq[UnspentTransaction]]] =
    send[Seq[UnspentTransaction]]("listunspent")

  //  object Purpose {
  //    implicit val formatPurpose: OFormat[Purpose] = Json.format[Purpose]
  //  }
  //  case class Purpose(purpose: String)
  // todo: returns Map[BtcAddress, Purpose]
  def getaddressesbylabel(label: String): Future[Either[RpcResponseError, JsValue]] =
    send[JsValue]("getaddressesbylabel", label)

  /**
   * Add a nrequired-to-sign multisignature address to the wallet. Requires a new wallet backup.
   * Each key is a Bitcoin address or hex-encoded public key.
   * This functionality is only intended for use with non-watchonly addresses.
   * See `importaddress` for watchonly p2sh address support.
   * If 'label' is specified, assign address to that label.
   *
   * @param nrequired
   * @param keys
   * @return
   */
  def addmultisigaddress(
      nrequired: Int,
      keys: Seq[String],
      label: Option[String] = None,
      addressType: Option[AddressType] = None
  ): Future[Either[RpcResponseError, AddMultisigSuccess]] =
    send[AddMultisigSuccess]("addmultisigaddress", nrequired, keys, label, addressType)

  /**
   * createmultisig nrequired ["key",...] ( "address_type" )
   *
   * Creates a multi-signature address with n signature of m keys required.
   * It returns a json object with the address and redeemScript.
   *
   * Arguments:
   * 1. nrequired       (numeric, required) The number of required signatures out of the n keys.
   * 2. keys            (json array, required) A json array of hex-encoded public keys.
   * [
   * "key",      (string) The hex-encoded public key
   * ...
   * ]
   * 3. address_type    (string, optional, default=legacy) The address type to use. Options are "legacy", "p2sh-segwit", and "bech32".
   *
   * Result:
   * {
   * "address":"multisigaddress",  (string) The value of the new multisig address.
   * "redeemScript":"script"       (string) The string value of the hex-encoded redemption script.
   * }
   *
   * @param nrequired
   * @param keys
   * @return
   */
  def createmultisig(
      nrequired: Int,
      keys: Seq[String],
      address_type: Option[AddressType] = None
  ): Future[Either[RpcResponseError, AddMultisigSuccess]] =
    send[AddMultisigSuccess]("createmultisig", nrequired, keys, address_type)

  def dumpprivkey(address: BtcAddress): Future[Either[RpcResponseError, String]] =
    send[String]("dumpprivkey", address.address)

  def settxfee(amount: BigDecimal): Future[Either[RpcResponseError, Boolean]] =
    send[Boolean]("settxfee", amount)

  /**
   * 1. height    (numeric, required) The block height to prune up to. May be set to a discrete height, or a unix timestamp
   * to prune blocks whose block time is at least 2 hours older than the provided timestamp.
   *
   * Result:
   * n    (numeric) Height of the last block pruned.
   *
   * @param height
   * @return
   */
  def pruneblockchain(height: Int): Future[Either[RpcResponseError, JsValue]] = send[JsValue]("pruneblockchain", height)

  // returns nothing
  def setlabel(address: BtcAddress, label: String): Future[Either[RpcResponseError, JsValue]] =
    send[JsValue]("setlabel", address.address, label)

  /**
   * importaddress "address" ( "label" rescan p2sh )
   *
   * Adds an address or script (in hex) that can be watched as if it were in your wallet but cannot be used to spend. Requires a new wallet backup.
   *
   * Note: This call can take over an hour to complete if rescan is true, during that time, other rpc calls
   * may report that the imported address exists but related transactions are still missing, leading to temporarily incorrect/bogus balances and unspent outputs until rescan completes.
   * If you have the full public key, you should call importpubkey instead of this.
   * Hint: use importmulti to import more than one address.
   *
   * Note: If you import a non-standard raw script in hex form, outputs sending to it will be treated
   * as change, and not show up in many RPCs.
   * Note: Use "getwalletinfo" to query the scanning progress.
   *
   * Arguments:
   * 1. address    (string, required) The Bitcoin address (or hex-encoded script)
   * 2. label      (string, optional, default="") An optional label
   * 3. rescan     (boolean, optional, default=true) Rescan the wallet for transactions
   * 4. p2sh       (boolean, optional, default=false) Add the P2SH version of the script as well
   *
   * @param address
   * @param label
   * @param rescan
   * @param p2sh
   * @return
   */
  def importaddress(
      address: BtcAddress,
      label: Option[String] = None,
      rescan: Option[Boolean] = None,
      p2sh: Option[Boolean] = None
  ): Future[Either[RpcResponseError, JsValue]] =
    send[JsValue]("importaddress", address, label, rescan, p2sh)

  def importpubkey(publicKey: String, label: Option[String] = None, rescan: Option[Boolean] = None) =
    send[JsValue]("importpubkey", publicKey, label, rescan)

  def importprivkey(privateKey: String, label: Option[String] = None, rescan: Option[Boolean] = None) =
    send[JsValue]("importprivkey", privateKey, label, rescan)

  def getaddressinfo(address: BtcAddress): Future[Either[RpcResponseError, AddressInfo]] =
    send[AddressInfo]("getaddressinfo", address)

  def importmulti = ???

  def getmempoolinfo: Future[Either[RpcResponseError, MempoolInfo]] = send[MempoolInfo]("getmempoolinfo")

  def rescanblockchain(start_height: Option[Int] = None, stop_height: Option[Int] = None) =
    send[JsValue]("rescanblockchain", start_height, stop_height)

  def getwalletinfo: Future[Either[RpcResponseError, WalletInfo]] = send[WalletInfo]("getwalletinfo")

}
