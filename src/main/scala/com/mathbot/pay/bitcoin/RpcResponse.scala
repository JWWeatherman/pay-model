package com.mathbot.pay.bitcoin

trait RpcResponse[T] {
  def result: T
  def id: String
}
