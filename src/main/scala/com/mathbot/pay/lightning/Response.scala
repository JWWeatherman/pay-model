package com.mathbot.pay.lightning

trait Response[T] extends LightningJson {

  def id: Long
  def jsonrpc: String
  def result: T
}
