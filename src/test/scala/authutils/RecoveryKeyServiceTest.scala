package authutils

import com.mathbot.pay.SecureIdentifier
import org.scalatest.funsuite.AnyFunSuite

class RecoveryKeyServiceTest extends AnyFunSuite {
  test("recovery key") {

    val e = new EncryptionService(SecureIdentifier(8).toString())
    val s = new RecoveryKeyService(encryption = e, key = "abcxyz")
    val tid = SecureIdentifier(8)
    val rc = s.makeKey(tid)
    val result = s.tokenId(rc)
    assert(result.isDefined)
    assert(result.get === tid.toString())
  }
}
