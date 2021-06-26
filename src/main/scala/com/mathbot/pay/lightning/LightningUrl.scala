package com.mathbot.pay.lightning

import akka.http.scaladsl.model.Uri
import fr.acinq.bitcoin.Bech32
import fr.acinq.bitcoin.Bech32.{five2eight, Int5}

case class LightningUrl(url: String) {
  // validate
  Bech32.decode(url)

  override def toString: String = url

  def decode: String = {
    val (hrp, data) = Bech32.decode(url)
    new String(five2eight(data))
  }
}
object LightningUrl {
  def eight2five(input: Array[Byte]): Array[Int5] = {
    var buffer = 0L
    val output = collection.mutable.ArrayBuffer.empty[Byte]
    var count = 0
    input.foreach(b => {
      buffer = (buffer << 8) | (b & 0xff)
      count = count + 8
      while (count >= 5) {
        output.append(((buffer >> (count - 5)) & 31).toByte)
        count = count - 5
      }
    })
    if (count > 0) output.append(((buffer << (5 - count)) & 31).toByte)
    output.toArray
  }
  val hrp = "lnurl"
  def apply(uri: Uri): LightningUrl =
    LightningUrl(Bech32.encode(hrp, eight2five(uri.toString().getBytes())))
}
