package com.mathbot.pay.lightning

import com.mathbot.pay.utils.JsonParseTest
import play.api.libs.json.Json
import org.scalatest.funsuite.AnyFunSuite

class PaymentTest extends JsonParseTest[Seq[Payment]]("payments.json")
