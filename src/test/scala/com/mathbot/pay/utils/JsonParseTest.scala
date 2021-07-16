package com.mathbot.pay.utils

import org.scalatest.funsuite.AnyFunSuite
import play.api.libs.json.{Json, Reads}

import scala.io.Source

abstract class JsonParseTest[T](filePath: String)(implicit rt: Reads[T]) extends AnyFunSuite {

  test("json parse") {
    val s = Source.fromResource(filePath).getLines().mkString("")
    val j = Json.parse(s)
    val r = j.validate[T]
    assert(r.isSuccess, s"$r")
  }
}
