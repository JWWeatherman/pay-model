package com.mathbot.pay.lightning

import com.github.dwickern.macros.NameOf.nameOf
import com.mathbot.pay.lightning.url.InvoiceWithDescriptionHash
import play.api.libs.json.{JsObject, Json, OFormat}

case class LightningRpcRequest(
    method: String,
    id: String,
    params: JsObject = Json.obj(),
    jsonrpc: String = LightningRpcRequest.json2
)

object LightningRpcRequest {
  val json2 = "2.0"
  lazy implicit val formatRequest: OFormat[LightningRpcRequest] = Json.format[LightningRpcRequest]

  def apply(lj: LightningJson, id: String): LightningRpcRequest = {
    val (method, params) = lj match {
      case i: InvoiceWithDescriptionHash =>
        (nameOf(InvoiceWithDescriptionHash).toLowerCase, Json.toJsObject(i))
      case w: WaitInvoice => (nameOf(WaitInvoice).toLowerCase, Json.toJsObject(w))
      case l: LightningListOffersRequest => ("listoffers", Json.toJsObject(l))
      case l: LightningOfferRequest => ("offer", Json.toJsObject(l))
      case l: ListInvoicesRequest => ("listinvoices", Json.toJsObject(l))
      case w: WaitAnyInvoice => ("waitanyinvoice", Json.toJsObject(w))
      case p: Pay => ("pay", Json.toJsObject(p))
      case x: ListPaysRequest =>
        ("listpays", Json.toJsObject(x))
      case x: LightningDebitRequest =>
        ("pay", Json.toJsObject(x.pay))
      case _: LightningGetInfoRequest =>
        ("getinfo", Json.obj())
      case DecodePayRequest(bolt11) =>
        ("decodepay", Json.obj("bolt11" -> bolt11.bolt11))
      case i: LightningInvoice =>
        ("invoice", Json.toJsObject(i))
      case i: MultiFundChannel => ??? // TODO: implement
    }
    LightningRpcRequest(method = method, id = id, params = params)
  }
}
