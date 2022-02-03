package com.mathbot.pay.lightning.spark

import com.mathbot.pay.lightning._
import play.api.libs.json._
import sttp.capabilities.akka.AkkaStreams
import sttp.client3.SttpBackend
import sttp.model.sse.ServerSentEvent

import scala.concurrent.Future

class SparkLightningWalletService(config: SparkLightningWalletServiceConfig,
                                  val backend: SttpBackend[Future, AkkaStreams])
    extends RpcLightningService {
  import SparkLightningWalletService._
  import sttp.client3._
  val baseUrl = config.baseUrl
  val base = basicRequest
    .headers(Map("X-Access" -> config.accessKey.value))

  /**
   * Helper to handle the server sent events. spark only returns btcusd and parid invoices
   * @param event
   * @return
   */
  def onEvent(event: ServerSentEvent): Either[String, SparkWalletSSE] = {
    (event.eventType, event.data) match {
      case (Some(event), Some(data)) =>
        event match {
          case "btcusd" =>
            val cleanData = data.replace("\"", "").trim
            Right(BtcUsd(BigDecimal(cleanData)))
          case "inv-paid" =>
            Json.parse(data).validate[ListInvoice] match {
              case JsSuccess(value, _) => Right(InvoicePaid(value))
              case JsError(errors) => Left(errors.mkString(","))
            }
          case other => Left(other)
        }
      case noEventTypeOrData => Left(event.toString())
    }
  }

}

object SparkLightningWalletService {
  trait SparkWalletSSE
  case class BtcUsd(btcusd: BigDecimal) extends SparkWalletSSE
  case class InvoicePaid(listPay: ListInvoice) extends SparkWalletSSE

}
