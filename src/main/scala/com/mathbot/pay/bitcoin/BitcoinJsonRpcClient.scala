package com.mathbot.pay.bitcoin

import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger

import com.mathbot.pay.bitcoin.AddressType.AddressType
import com.mathbot.pay.bitcoin.Btc.stringify
import com.mathbot.pay.bitcoin.EstimateFeeMode.EstimateFeeMode
import com.mathbot.pay.bitcoin.SigHashType.SigHashType
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import sttp.client._
import sttp.model.MediaType

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

//noinspection SpellCheckingInspection
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

  private val baseRequest = basicRequest
    .post(uri"${config.baseUrl}")
    .contentType(MediaType.TextPlain)
    .acceptEncoding(MediaType.ApplicationJson.toString)
    .auth
    .basic(user = config.username, password = config.password)

  private def asRpcResult[T](id: String)(implicit
                                         jsonReader: Reads[T]): ResponseAs[Either[RpcResponseError, T], Nothing] =
    asString.map {
      case Left(value) => Left(RpcResponseError(id, ResponseError(500, value)))
      case Right(value) =>
        Try(Json.parse(value)) match {
          case Failure(exception) =>
            Left(RpcResponseError(id, ResponseError(500, s"Failure parsing json body=$value, error=$exception")))
          case Success(value) =>
            val validResult = (value \ "result").validate[T]
            val idJ = (value \ "id").validate[String]
            val errorOpt = value \ "error" match {
              case _: JsUndefined => None
              case JsDefined(JsNull) => None
              case JsDefined(e) => Some(e)
            }
            (validResult, idJ, errorOpt) match {

              case (JsSuccess(value, _), JsSuccess(_, _), None) => Right(value)
              case _ =>
                value
                  .validate[RpcResponseError] match {
                  case JsSuccess(value, _) => Left(value)
                  case JsError(_) =>
                    val message = s"Unknown bitcoin rpc response = ${value}"
                    logger.error(message)
                    Left(
                      RpcResponseError(id, error = ResponseError(code = 500, message = message))
                    )
                }
            }
        }
    }

  /**
   * @param method to call eg getbalance, createpsbt...
   * @param params arguments to send
   * @param jsonReader json formatter for the response [[T]]
   * @tparam T the expected response from a successful call
   * @return Error or the method's expected response object
   */
  def send[T](method: String, params: JsValueWrapper*)(implicit
                                                       jsonReader: Reads[T]): Future[Either[RpcResponseError, T]] = {
    val ID = id.toString
    val body = Json
      .toJson(JsonRpcRequestBody(method = method, params = Json.arr(params: _*), jsonrpc = config.jsonRpc, id = ID))
      .toString
    val req = baseRequest
      .body(body)
      .response(asRpcResult(ID))

    logger.debug(req.toCurl)
    req.send().map(_.body)
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

  def getreceivedbyaddress(address: BtcAddress, minconf: Option[Int] = None): Future[Either[RpcResponseError, Btc]] =
    send[Btc]("getreceivedbyaddress", address, minconf)
  def getreceivedbylabel(label: String, minconf: Option[Int] = None): Future[Either[RpcResponseError, JsValue]] =
    send[JsValue]("getreceivedbylabel", label, minconf)
  def listreceivedbyaddress(
      minconf: Option[Int] = None,
      include_empty: Option[Boolean] = None,
      include_watchonly: Option[Boolean] = None,
      address_filter: Option[BtcAddress] = None
  ): Future[Either[RpcResponseError, Seq[ListReceivedByAddress]]] =
    send[Seq[ListReceivedByAddress]]("listreceivedbyaddress", minconf, include_empty, include_watchonly, address_filter)

  def listreceivedbylabel(
      minconf: Option[Int] = None,
      include_empty: Option[Boolean] = None,
      include_watchonly: Option[Boolean] = None
  ): Future[Either[RpcResponseError, JsValue]] =
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

  def deriveaddresses(description: String, start: Int, end: Int): Future[Either[RpcResponseError, List[BtcAddress]]] =
    send[List[BtcAddress]]("deriveaddresses", description, Seq(start, end))

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
  def utxoupdatepsbt(psbt: String): Future[Either[RpcResponseError, String]] = send[String]("utxoupdatepsbt", psbt)
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
      sign: Boolean = true,
      sighashType: SigHashType = SigHashType.ALL,
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

  def importpubkey(
      publicKey: String,
      label: Option[String] = None,
      rescan: Option[Boolean] = None
  ): Future[Either[RpcResponseError, JsValue]] =
    send[JsValue]("importpubkey", publicKey, label, rescan)

  def importprivkey(
      privateKey: String,
      label: Option[String] = None,
      rescan: Option[Boolean] = None
  ): Future[Either[RpcResponseError, JsValue]] =
    send[JsValue]("importprivkey", privateKey, label, rescan)

  def getaddressinfo(address: BtcAddress): Future[Either[RpcResponseError, AddressInfo]] =
    send[AddressInfo]("getaddressinfo", address)

  def importmulti(
      description: String,
      start: Int,
      end: Int,
      watchonly: Boolean,
      timestamp: Option[Instant]
  ): Future[Either[RpcResponseError, Seq[RpcSuccess]]] =
    send[Seq[RpcSuccess]](
      method = "importmulti",
      Seq(
        ImportMulti(
          desc = description,
          range = Seq(start, end),
          watchonly = watchonly,
          timestamp = timestamp.map(_.getEpochSecond.toString).getOrElse("now")
        )
      )
    )

  def getmempoolinfo: Future[Either[RpcResponseError, MempoolInfo]] = send[MempoolInfo]("getmempoolinfo")

  def rescanblockchain(
      start_height: Option[Int] = None,
      stop_height: Option[Int] = None
  ): Future[Either[RpcResponseError, JsValue]] =
    send[JsValue]("rescanblockchain", start_height, stop_height)

  def getwalletinfo: Future[Either[RpcResponseError, WalletInfo]] = send[WalletInfo]("getwalletinfo")

  def getdescriptorinfo(descriptor: String): Future[Either[RpcResponseError, DescriptorInfo]] =
    send[DescriptorInfo]("getdescriptorinfo", descriptor)

  /**
   * @return list of wallet names
   */
  def listwallets: Future[Either[RpcResponseError, Seq[String]]] = send[Seq[String]]("listwallets")

  /**
   * Set or generate a new HD wallet seed. Non-HD wallets will not be upgraded to being a HD wallet. Wallets that are already
   * HD will have a new HD seed set so that new keys added to the keypool will be derived from this new seed.
   *
   * Note that you will need to MAKE A NEW BACKUP of your wallet after setting the HD wallet seed.
   *
   * Arguments:
   * 1. newkeypool    (boolean, optional, default=true) Whether to flush old unused addresses, including change addresses, from the keypool and regenerate it.
   * If true, the next address from getnewaddress and change address from getrawchangeaddress will be from this new seed.
   * If false, addresses (including change addresses if the wallet already had HD Chain Split enabled) from the existing
   * keypool will be used until it has been depleted.
   * 2. seed          (string, optional, default=random seed) The WIF private key to use as the new HD seed.
   * The seed value can be retrieved using the dumpwallet command. It is the private key marked hdseed=1
   *
   * @param seed
   * @return
   */
  def sethdseed(newkeypool: Boolean = true, seed: Option[String] = None) =
    seed match {
      case Some(s) => send[JsValue]("sethdseed", newkeypool, s)
      case None => send[JsValue]("sethdseed", newkeypool)
    }

  /**
   * Creates and loads a new wallet.
   *
   * Arguments:
   * 1. wallet_name             (string, required) The name for the new wallet. If this is a path, the wallet will be created at the path location.
   * 2. disable_private_keys    (boolean, optional, default=false) Disable the possibility of private keys (only watchonlys are possible in this mode).
   * 3. blank                   (boolean, optional, default=false) Create a blank wallet. A blank wallet has no keys or HD seed. One can be set using sethdseed.
   * 4. passphrase              (string) Encrypt the wallet with this passphrase.
   * 5. avoid_reuse             (boolean, optional, default=false) Keep track of coin reuse, and treat dirty and clean coins differently with privacy considerations in mind.
   *
   * Result:
   * {
   * "name" :    <wallet_name>,        (string) The wallet name if created successfully. If the wallet was created using a full path, the wallet_name will be the full path.
   * "warning" : <warning>,            (string) Warning message if wallet was not loaded cleanly.
   * }
   *
   * @return
   */
  def createwallet(
      wallet_name: String,
      disable_private_keys: Boolean = false,
      blank: Boolean = false,
      passphrase: String = "",
      avoid_reuse: Boolean = false
  ) =
    send[JsValue]("createwallet", wallet_name, disable_private_keys, blank, passphrase, avoid_reuse)

  def dumpwallet(file: String) = send[JsValue]("dumpwallet", file)

}
