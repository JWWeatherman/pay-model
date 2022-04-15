package com.mathbot.pay.bitcoin

import play.api.libs.json.Json
import sttp.client3.SttpBackend

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

object FiatRatesService {

  val customFiatSymbols: Map[String, String] = Map(
    "rub" -> "\u20BD",
    "usd" -> "$",
    "inr" -> "₹",
    "gbp" -> "£",
    "cny" -> "CN¥",
    "jpy" -> "¥",
    "brl" -> "R$",
    "eur" -> "€",
    "krw" -> "₩"
  )

  val universallySupportedSymbols: Map[String, String] = Map(
    "usd" -> "US Dollar",
    "eur" -> "Euro",
    "jpy" -> "Japanese Yen",
    "cny" -> "Chinese Yuan",
    "inr" -> "Indian Rupee",
    "cad" -> "Canadian Dollar",
    "rub" -> "Русский Рубль",
    "brl" -> "Real Brasileiro",
    "czk" -> "Česká Koruna",
    "gbp" -> "Pound Sterling",
    "aud" -> "Australian Dollar",
    "try" -> "Turkish Lira",
    "nzd" -> "New Zealand Dollar",
    "thb" -> "Thai Baht",
    "twd" -> "New Taiwan Dollar",
    "krw" -> "South Korean won",
    "clp" -> "Chilean Peso",
    "sgd" -> "Singapore Dollar",
    "hkd" -> "Hong Kong Dollar",
    "pln" -> "Polish złoty",
    "dkk" -> "Danish Krone",
    "sek" -> "Swedish Krona",
    "chf" -> "Swiss franc",
    "huf" -> "Hungarian forint"
  )

  trait FiatRatesListener {
    def onFiatRates(rates: FiatRatesInfo): Unit
  }

  object CoinGeckoItem {
    implicit val formatCoinGeckoItem = Json.format[CoinGeckoItem]
  }
  case class CoinGeckoItem(value: Double)
  object BlockchainInfoItem {
    implicit val formatBlockchainInfoItem = Json.format[BlockchainInfoItem]
  }
  case class BlockchainInfoItem(last: Double)
  object BitpayItem {
    implicit val formatBitpayItem = Json.format[BitpayItem]
  }
  case class BitpayItem(code: String, rate: Double)

  object Bitpay {
    implicit val formatBitpay = Json.format[Bitpay]
  }
  case class Bitpay(data: FiatRatesService.BitpayItemList)
  object CoinGecko {
    implicit val formatCoinGecko = Json.format[CoinGecko]
  }
  case class CoinGecko(rates: FiatRatesService.CoinGeckoItemMap)

  case class FiatRatesInfo(
      rates: Map[String, Double],
      oldRates: Map[String, Double],
      stamp: Long
  ) {
    //  def pctDifference(code: String): Option[String] =
    //    List(rates get code, oldRates get code) match {
    //      case Some(fresh) :: Some(old) :: Nil if fresh > old =>
    //        Some(
    //          s"<font color=#8BD670><small>▲</small> ${Denomination.formatFiat format pctChange(fresh, old).abs}%</font>"
    //        )
    //      case Some(fresh) :: Some(old) :: Nil if fresh < old =>
    //        Some(
    //          s"<small>▼</small> ${Denomination.formatFiat format pctChange(fresh, old).abs}%"
    //        )
    //      case _ => None
    //    }

    def pctChange(fresh: Double, old: Double): Double = (fresh - old) / old * 100
  }
  type BlockchainInfoItemMap = Map[String, BlockchainInfoItem]
  type CoinGeckoItemMap = Map[String, CoinGeckoItem]
  implicit val formatCoinGeckoItemMap = Json.format[CoinGeckoItem]
  implicit val formatCoinGeckoItemMaps = Json.format[BlockchainInfoItem]
  type BitpayItemList = List[BitpayItem]

}

class FiatRatesService(backend: SttpBackend[Future, _])(implicit executionContext: ExecutionContext) {

  import FiatRatesService._
  import sttp.client3._
  import playJson._

  def requestRates = {
    val req = Random.shuffle(0 :: 1 :: 2 :: Nil).head match {
      case 0 =>
        basicRequest
          .get(uri"https://api.coingecko.com/api/v3/exchange_rates")
          .response(asJson[CoinGecko].mapRight(cg => {
            cg.rates.map { case (code, item) => code.toLowerCase -> item.value }
          }))

      case 1 =>
        basicRequest
          .get(uri"https://blockchain.info/ticker")
          .response(asJson[FiatRatesService.BlockchainInfoItemMap].mapRight(_.map {
            case (code, item) => code.toLowerCase -> item.last
          }))
      case _ =>
        basicRequest
          .get(uri"https://bitpay.com/rates")
          .response(asJson[Bitpay].mapRight(_.data.map {
            case BitpayItem(code, rate) =>
              code.toLowerCase -> rate
          }.toMap))
    }
    req.send(backend)
  }

  def reloadData = requestRates.map(_.body.map(m => updateInfo(m)))
  def updateInfo(newRates: Map[String, Double]): Unit = {
    info = FiatRatesInfo(rates = newRates, oldRates = info.rates, stamp = System.currentTimeMillis)
    for (lst <- listeners) lst.onFiatRates(info)
  }

  var listeners: Set[FiatRatesListener] = Set.empty
  var info: FiatRatesInfo =
    FiatRatesInfo(rates = Map.empty, oldRates = Map.empty, stamp = 0L)

}
