package payModel.models.lightning

import org.scalatest.FunSuite
import payModel.models._

import scala.language.postfixOps

class Bolt11Test extends FunSuite {

  test("apply") {
    val bitrefillBolt11 =
      "lnbc22435140n1p0zxgcupp5hkn3lnlkk6kmq0670a9u2xd8reuefc7dw704pwe7dqqm06nnzpcsdz2gf5hgun9ve5kcmpqv9jx2v3e8pjkgtf5xaskxtf5venxvttp8pjrvttrvgerzvm9x93r2dehxgfppjue4tflpg2hule862xylcsnu0p0mrjvmnxqrp9s2xnqfz900f0mq36dnkseqped68nzjm2nvh85uxw4nkhezkd4zzlymw93q96mf9glupqxrfd46rps7dztqm5rerusxpx77curwrpal9qqenm4p0"
    val b = Bolt11(bitrefillBolt11)
    assert(b.amount.toSatoshi === (2243514 satoshi))
  }
}
