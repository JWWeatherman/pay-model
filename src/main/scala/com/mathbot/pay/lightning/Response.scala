package com.mathbot.pay.lightning

trait Response[T] {

  def id: Long
  def jsonrpc: String
  def result: T
}
