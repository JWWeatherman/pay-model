package com.mathbot.pay.utils

import play.api.libs.json.{JsValue, Json}

import scala.io.Source

object ResourceHelper {

  def read(resource: String): JsValue = {
    val stream = getClass.getResourceAsStream(if (resource.startsWith("/")) resource else "/" + resource)
    val lines = Source.fromInputStream(stream).getLines
    val json = Json.parse(lines.mkString)
    stream.close()
    json
  }
}
