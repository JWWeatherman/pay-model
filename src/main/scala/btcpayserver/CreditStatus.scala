package btcpayserver

import play.api.libs.json._

/**
 * https://docs.btcpayserver.org/Invoices/#invoice-statuses
 * Invoice Status	Description	Action
 * New	Not paid, invoice timer still has not expired	None
 * New (paidPartial)	Not paid in full, invoice timer still has not expired	None
 * Expired	Not paid, invoice timer expired	None
 * Paid	Paid, but has not received sufficient amount of confirmations specified in the Store Settings	Wait for confirmations (The invoice should become - complete)
 * Confirmed*	Paid, confirmed, by reaching the number of confirmations in store settings but has not received the default number (6) of confirmations in BTCPay.	Wait for remaining confirmations, or proceed if store settings confirmations is acceptable
 * Complete	Paid, completed, received sufficient amount of confirmations in store	Fulfil the order
 * Complete (marked)	Status was manually changed to complete from an invalid or expired status	Store admin has marked the payment as complete
 * Expired (paidPartial)**	Paid, not in full amount, and expired	Contact buyer to arrange a refund or ask for them to pay their due. Optionally mark invoice as complete or invalid
 * Expired (paidLate)	Paid, in full amount, after expired	Contact buyer to arrange a refund or process order if late confirmations are acceptable.
 * Paid (paidOver)	Paid more than the invoice amount, but has not received sufficient amount of confirmations specified in the Store Settings	Wait for confirmations. The invoice should become - Complete (Paid Over)
 * Complete (paidOver)	Paid more than the invoice amount, completed, received sufficient amount of confirmations	Contact buyer to arrange a refund for the extra amount, or optionally wait for buyer to contact you
 * Invalid***	Paid, but failed to receive sufficient amount of confirmations within the time specified in store settings	Check the transaction on a blockchain explorer, if it received sufficient confirmations, mark as complete
 * Invalid (marked)	Status was manually changed to invalid from a complete or expired status	Store admin has marked the payment as invalid
 * Invalid (paidOver)	Paid more than the invoice amount, but failed to receive sufficient amount of confirmations within the time specified in store settings	Check the transaction on a blockchain explorer, if it received sufficient confirmations, mark as complete
 */
object CreditStatus extends Enumeration {
  type CreditStatus = Value

  val `new`, paid, confirmed, complete, expired, failed, underpaid, processing, invalid = Value

  implicit val formatCreditStatus = Json.formatEnum(this)
}
