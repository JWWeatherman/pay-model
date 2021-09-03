package com.mathbot.pay

case class Sensitive(value: String) {
  override def toString: String = "***"
}
