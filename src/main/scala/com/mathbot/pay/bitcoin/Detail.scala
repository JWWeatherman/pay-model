package com.mathbot.pay.bitcoin

import com.mathbot.pay.bitcoin.TransactionCategory.TransactionCategory
import com.mathbot.pay.json.PlayJsonSupport
import fr.acinq.bitcoin.Btc
import play.api.libs.json.{Json, OFormat}

case class Detail(address: BtcAddress, amount: Btc, category: TransactionCategory, vout: Int)

object Detail extends PlayJsonSupport {
  implicit val formatDetail: OFormat[Detail] = Json.format[Detail]
}
