package authutils

import com.google.common.hash.Hashing

import java.nio.charset.StandardCharsets

object Hash {
  def hmacSha512(data: String, key: String): String =
    Hashing.hmacSha512(key.getBytes()).hashString(data, StandardCharsets.UTF_8).toString

  def sha256(data: String): String =
    Hashing.sha256().hashString(data, StandardCharsets.UTF_8).toString

}
