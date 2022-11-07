package authutils

import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import scala.util.Try

class EncryptionService(salt: String) {

  def encrypt(key: String, value: String): String = {
    val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, keyToSpec(key))
    Base64.getEncoder.encodeToString(cipher.doFinal(value.getBytes("UTF-8")))
  }

  @throws(classOf[javax.crypto.BadPaddingException])
  def decrypt(key: String, encryptedValue: String): String = {
    val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING")
    cipher.init(Cipher.DECRYPT_MODE, keyToSpec(key))
    new String(cipher.doFinal(Base64.getDecoder.decode(encryptedValue)))
  }

  def keyToSpec(key: String): SecretKeySpec = {
    var keyBytes: Array[Byte] = (salt + key).getBytes("UTF-8")
    val sha: MessageDigest = MessageDigest.getInstance("SHA-1")
    keyBytes = sha.digest(keyBytes)
    keyBytes = util.Arrays.copyOf(keyBytes, 16)
    new SecretKeySpec(keyBytes, "AES")
  }

  def decryptTokenFromUrl(key: String, extendedToken: String): Try[String] = {

    // https://stackoverflow.com/questions/17564837/how-to-know-if-a-url-is-decoded-encoded?rq=1
    // /%[0-9a-f]{2}/i
    val isEncoded = extendedToken.contains("%")
    Try {
      val urlDecodedToken =
        if (isEncoded) URLDecoder.decode(extendedToken, StandardCharsets.UTF_8.name()) else extendedToken
      decrypt(key, urlDecodedToken)
    }

  }

}
