package com.mathbot.pay.lightning.url

import com.mathbot.pay.lightning.Bolt11
import com.mathbot.pay.lightning.Bolt11Test.bolt11
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

class LightningUrlPayRequestTest extends AnyWordSpec {
  s"Ln" should {
    "w" in {

      val j = LightningUrlPayRequest(bolt11, Some(MessageSuccess("hi")))

      val json = Json.toJson(j)
      val e =
        s"""{"pr":"$bolt11","successAction":{"message":"hi","tag":"message"}}""".stripMargin
      assert(json.toString() === e)
    }
  }
}
