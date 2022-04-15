package com.mathbot.pay.bitcoin

import play.api.libs.json._

import scala.util.Try

case class BtcAddress(address: String) {
  require(BtcAddress.validateAddr(address), s"Invalid btc address = $address")
  override def toString: String = address
}

object BtcAddress {
  implicit val formatBtcAddress: Format[BtcAddress] = new Format[BtcAddress] {

    override def writes(o: BtcAddress): JsValue = JsString(o.address)

    override def reads(json: JsValue): JsResult[BtcAddress] =
      json match {
        case JsString(v) =>
          Try(BtcAddress.validateAddr(v))
            .map(_ => JsSuccess(BtcAddress(v)))
            .getOrElse(JsError("Invalid btc address"))
        case _ => JsError()
      }
  }
  def validateAddr(addr: String): Boolean = ??? // todo

}
