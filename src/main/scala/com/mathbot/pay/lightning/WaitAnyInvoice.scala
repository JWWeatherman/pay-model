package com.mathbot.pay.lightning

import play.api.libs.json.Json

/**
 * Wait for the next invoice to be paid, after {lastpay_index} (if supplied).
 * If {timeout} seconds is reached while waiting, fail with an error.
 *
 * @param lastpay_index
 * @param timeout seconds
 */
case class WaitAnyInvoice(lastpay_index: Option[Int], timeout: Option[Int]) extends LightningJson

object WaitAnyInvoice {
  implicit val formatWaitAnyInvoice = Json.format[WaitAnyInvoice]
}
