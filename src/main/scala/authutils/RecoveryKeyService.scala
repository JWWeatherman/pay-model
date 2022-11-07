package authutils

import com.mathbot.pay.SecureIdentifier
import play.api.libs.json.{Json, OFormat}

import java.time.Instant
import scala.concurrent.duration._
import scala.util.Try

class RecoveryKeyService(encryption: EncryptionService, key: String) {

  def makeKey(tokenId: SecureIdentifier): RecoveryKey = {
    val n = System.currentTimeMillis()
    val extendedToken = s"${SecureIdentifier(8).toString},$n,$tokenId"
    val encrypted = encryption.encrypt(key = key, value = extendedToken)
    new RecoveryKey(encrypted)
  }

  def validTime(t: Long): Boolean = {
    val ti = Instant.ofEpochMilli(t)
    Instant.now().minusSeconds(1.day.toSeconds).isBefore(ti)
  }

  def tokenId(recoveryKey: RecoveryKey): Option[String] = tokenId(recoveryKey.key)

  def tokenId(value: String): Option[String] =
    (Try(encryption.decrypt(key = key, encryptedValue = value)) map { decrypted =>
      decrypted.split(",").toSeq match {
        case Seq(_, t, tokenId) if validTime(t.toLong) => Some(tokenId)
        case _ => None
      }
    }).toOption.flatten

}
