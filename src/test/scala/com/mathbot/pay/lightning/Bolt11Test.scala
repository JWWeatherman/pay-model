package com.mathbot.pay.lightning

import org.scalatest.funsuite.AnyFunSuite

import scala.language.postfixOps

class Bolt11Test extends AnyFunSuite {

  test("apply 2") {
    val bolt =
      "lnbc1p06zke0pp568h63w0gkukjtpukx6dg5q5v0wd89htqpnufw4r0yakc3yf2dmzsdqqxqyjw5q9qtzqqqqqq9qsqsp5at5dgy94efptuxfav4mdl4my8esrlevllgdh2dlmh2jlgh5dmu4qrzjqwryaup9lh50kkranzgcdnn2fgvx390wgj5jd07rwr3vxeje0glcll64qnu5f2l44qqqqqlgqqqqqeqqjqgvauvvlfg7s9epffvewmkzthmh0png9f0ptsspknyl3cad7hud2596c95anj56vfplxnrk4d8ftcatg095t6qcx2nnjcwm644lkyygspt2gkff"
    val b = Bolt11(bolt)
    assert(true)
  }
}

object Bolt11Test {
  val bitrefillBolt11 =
    "lnbc22435140n1p0zxgcupp5hkn3lnlkk6kmq0670a9u2xd8reuefc7dw704pwe7dqqm06nnzpcsdz2gf5hgun9ve5kcmpqv9jx2v3e8pjkgtf5xaskxtf5venxvttp8pjrvttrvgerzvm9x93r2dehxgfppjue4tflpg2hule862xylcsnu0p0mrjvmnxqrp9s2xnqfz900f0mq36dnkseqped68nzjm2nvh85uxw4nkhezkd4zzlymw93q96mf9glupqxrfd46rps7dztqm5rerusxpx77curwrpal9qqenm4p0"
  val bolt11 = Bolt11(bitrefillBolt11)
}
