package com.mathbot.pay.bitcoin

import play.api.libs.json.{Json, OFormat}

case class BlockCount(blocks: Int)

object BlockCount {
  implicit val formatBlockCount: OFormat[BlockCount] = Json.format[BlockCount]
}
