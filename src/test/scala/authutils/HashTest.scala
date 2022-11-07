package authutils

import org.scalatest.funsuite.AnyFunSuite

class HashTest extends AnyFunSuite {

  test("sha256") {

    val input = "some string"
    val want = "61d034473102d7dac305902770471fd50f4c5b26f6831a56dd90b5184b3c30fc"

    val result = Hash.sha256(input)

    assert(result == want)

    val input2 = "hello world"
    val result2 = Hash.sha256(input2)
    val want2 = "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9"
    assert(result2 == want2)

    val serverseed = "293d5d2ddd365f54759283a8097ab2640cbe6f8864adc2b1b31e65c14c999f04"
    val serverseedHash = "5ac59780d512265230d5efb3cc238886dc1b457a80b54fbf1f920b99c6505801"

    val result3 = Hash.sha256(serverseed)

    assert(serverseedHash == result3)

  }

  test("hmacSha512") {

    val data = "hello"
    val key = "world"

    val hmac =
      "2b83319d3e78544e4430c4f5621968fee8b6ffa1254678b2c6fb98f7f79ff16afee2da909a7bb741488ca3bacbbf6cec8fd226c5a52eef805ea65a352e2ece8e"
    val result = Hash.hmacSha512(data, key)

    assert(hmac == result)
  }

}
