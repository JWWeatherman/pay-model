package com.mathbot.pay.bitcoin

import fr.acinq.bitcoin.Btc
import play.api.libs.json.Json

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

object Scanning {
  implicit val formatScanning = Json.format[Scanning]
}
case class Scanning(
    duration: Long, // seconds
    progress: BigDecimal
) {
  val durationScanning = FiniteDuration(duration, TimeUnit.SECONDS)
}
object WalletInfo {
  implicit val formatWalletInfo = Json.format[WalletInfo]
}

/**
 * {
 * "walletname": xxxxx,               (string) the wallet name
 * "walletversion": xxxxx,            (numeric) the wallet version
 * "balance": xxxxxxx,                (numeric) DEPRECATED. Identical to getbalances().mine.trusted
 * "unconfirmed_balance": xxx,        (numeric) DEPRECATED. Identical to getbalances().mine.untrusted_pending
 * "immature_balance": xxxxxx,        (numeric) DEPRECATED. Identical to getbalances().mine.immature
 * "txcount": xxxxxxx,                (numeric) the total number of transactions in the wallet
 * "keypoololdest": xxxxxx,           (numeric) the timestamp (seconds since Unix epoch) of the oldest pre-generated key in the key pool
 * "keypoolsize": xxxx,               (numeric) how many new keys are pre-generated (only counts external keys)
 * "keypoolsize_hd_internal": xxxx,   (numeric) how many new keys are pre-generated for internal use (used for change outputs, only appears if the wallet is using this feature, otherwise external keys are used)
 * "unlocked_until": ttt,             (numeric) the timestamp in seconds since epoch (midnight Jan 1 1970 GMT) that the wallet is unlocked for transfers, or 0 if the wallet is locked
 * "paytxfee": x.xxxx,                (numeric) the transaction fee configuration, set in BTC/kB
 * "hdseedid": "<hash160>"            (string, optional) the Hash160 of the HD seed (only present when HD is enabled)
 * "private_keys_enabled": true|false (boolean) false if privatekeys are disabled for this wallet (enforced watch-only wallet)
 * "avoid_reuse": true|false          (boolean) whether this wallet tracks clean/dirty coins in terms of reuse
 * "scanning":                        (json object) current scanning details, or false if no scan is in progress
 * {
 * "duration" : xxxx              (numeric) elapsed seconds since scan start
 * "progress" : x.xxxx,           (numeric) scanning progress percentage [0.0, 1.0]
 * }
 * }
 *
 * @param walletname
 * @param walletversion
 * @param balance
 * @param unconfirmed_balance
 * @param immature_balance
 * @param txcount
 * @param keypoololdest
 * @param keypoolsize
 * @param keypoolsize_hd_internal
 * @param paytxfee
 * @param hdseedid
 * @param private_keys_enabled
 * @param avoid_reuse
 * @param scanning
 */
case class WalletInfo(
    walletname: String,
    walletversion: Int,
    balance: Btc,
    unconfirmed_balance: Btc,
    immature_balance: Btc,
    txcount: Long,
    keypoololdest: Long,
    keypoolsize: Long,
    keypoolsize_hd_internal: Long,
    paytxfee: Btc,
    hdseedid: String,
    private_keys_enabled: Boolean,
    avoid_reuse: Boolean,
//    scanning: Option[Scanning], // fixme can be false when not scanning
    unlocked_until: Option[Int]
) {
//  val isScanning = scanning match {
//    case Some(x) => x.progress < 0.999
//    case None => false
//  }
}
