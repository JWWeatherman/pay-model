package com.mathbot.pay.utils

import play.api.libs.json.Json

object ResourceHelper {

  def read(resource: String) = {
    val stream = getClass.getResourceAsStream(resource)
    val lines = io.Source.fromInputStream(stream).getLines
    val json = Json.parse(lines.mkString)
    stream.close()
    json
  }
}
