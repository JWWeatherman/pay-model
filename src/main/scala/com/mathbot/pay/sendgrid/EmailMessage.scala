package com.mathbot.pay.sendgrid

trait EmailMessage {
  def asHtml: String
  def from: String
  def to: String
  def subject: String
}
