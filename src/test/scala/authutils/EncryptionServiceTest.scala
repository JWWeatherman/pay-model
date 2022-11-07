package authutils

import com.mathbot.pay.SecureIdentifier
import org.scalatest.funsuite.AnyFunSuite

class EncryptionServiceTest extends AnyFunSuite {
  test("encryption") {
    val e = new EncryptionService(salt = SecureIdentifier(8).toString())
    val d = e.encrypt(key = "key", value = "value")
    val e1 = e.decrypt(key = "key", encryptedValue = d)
    assert(e1 === "value")
  }
}
