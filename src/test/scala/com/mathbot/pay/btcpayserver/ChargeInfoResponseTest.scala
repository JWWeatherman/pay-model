package com.mathbot.pay.btcpayserver

import org.scalatest
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.{JsResultException, Json}

object ChargeInfoResponseTest {

  val newCharge: String =
    """
      |{
      |    "data": {
      |        "url": "https://payments.mathbot.com/invoice?id=Qs7rFAGHffeGGTMgFfS2pf",
      |        "posData": null,
      |        "status": "new",
      |        "btcPrice": "0.00012347",
      |        "btcDue": "0.00012347",
      |        "cryptoInfo": [
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "BTCLike",
      |                "rate": 8099.75637366377,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00000000",
      |                "price": "0.00012347",
      |                "due": "0.00012347",
      |                "paymentUrls": {
      |                    "BIP21": "bitcoin:tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q?amount=0.00012347",
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": null
      |                },
      |                "address": "tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q",
      |                "url": "https://payments.mathbot.com/i/BTC/Qs7rFAGHffeGGTMgFfS2pf",
      |                "totalDue": "0.00012347",
      |                "networkFee": "0.00000000",
      |                "txCount": 0,
      |                "cryptoPaid": "0.00000000",
      |                "payments": []
      |            },
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "LightningLike",
      |                "rate": 8099.75637366377,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00000000",
      |                "price": "0.00012347",
      |                "due": "0.00012347",
      |                "paymentUrls": {
      |                    "BIP21": null,
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": "lightning:lntb123470n1pwep8khpp5lmv5g57trf7r708rnlyclmu486h29zqm3tdz898crs3qvddtpz6sdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2fukzeqgx464q20r7g6mwg3aj5tjfgu0tzkvc3a5fe9yjjp0c8tjxanwhpzg2ptujvtuskd9yjflxgygyzu7ys3axk8630x4wyhuerxcqhx9yrq"
      |                },
      |                "address": "lntb123470n1pwep8khpp5lmv5g57trf7r708rnlyclmu486h29zqm3tdz898crs3qvddtpz6sdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2fukzeqgx464q20r7g6mwg3aj5tjfgu0tzkvc3a5fe9yjjp0c8tjxanwhpzg2ptujvtuskd9yjflxgygyzu7ys3axk8630x4wyhuerxcqhx9yrq",
      |                "url": "https://payments.mathbot.com/i/BTC_LightningLike/Qs7rFAGHffeGGTMgFfS2pf",
      |                "totalDue": "0.00012347",
      |                "networkFee": "0.00000000",
      |                "txCount": 0,
      |                "cryptoPaid": "0.00000000",
      |                "payments": []
      |            }
      |        ],
      |        "price": 1.0,
      |        "currency": "USD",
      |        "exRates": {
      |            "USD": 0.0
      |        },
      |        "buyerTotalBtcAmount": null,
      |        "itemDesc": "a test invoice",
      |        "itemCode": null,
      |        "orderId": "testing",
      |        "guid": "9de3a281-05c8-40bc-8412-b0db298bcca8",
      |        "id": "Qs7rFAGHffeGGTMgFfS2pf",
      |        "invoiceTime": 1569758935000,
      |        "expirationTime": 1569759835000,
      |        "currentTime": 1569759056180,
      |        "lowFeeDetected": false,
      |        "btcPaid": "0.00000000",
      |        "rate": 8099.75637366377,
      |        "exceptionStatus": false,
      |        "paymentUrls": {
      |            "BIP21": "bitcoin:tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q?amount=0.00012347",
      |            "BIP72": null,
      |            "BIP72b": null,
      |            "BIP73": null,
      |            "BOLT11": null
      |        },
      |        "refundAddressRequestPending": false,
      |        "buyerPaidBtcMinerFee": null,
      |        "bitcoinAddress": "tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q",
      |        "token": "3myToYPfBfWyP2RbKDJoDC",
      |        "flags": {
      |            "refundable": false
      |        },
      |        "paymentSubtotals": {
      |            "BTC": 12347,
      |            "BTC_LightningLike": 12347
      |        },
      |        "paymentTotals": {
      |            "BTC": 12347,
      |            "BTC_LightningLike": 12347
      |        },
      |        "amountPaid": 0,
      |        "minerFees": {
      |            "BTC": {
      |                "satoshisPerByte": 1.0,
      |                "totalFee": 0
      |            }
      |        },
      |        "exchangeRates": {
      |            "BTC": {
      |                "USD": 0.0
      |            }
      |        },
      |        "supportedTransactionCurrencies": {
      |            "BTC": {
      |                "enabled": true
      |            }
      |        },
      |        "addresses": {
      |            "BTC": "tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q",
      |            "BTC_LightningLike": "lntb123470n1pwep8khpp5lmv5g57trf7r708rnlyclmu486h29zqm3tdz898crs3qvddtpz6sdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2fukzeqgx464q20r7g6mwg3aj5tjfgu0tzkvc3a5fe9yjjp0c8tjxanwhpzg2ptujvtuskd9yjflxgygyzu7ys3axk8630x4wyhuerxcqhx9yrq"
      |        },
      |        "paymentCodes": {
      |            "BTC": {
      |                "BIP21": "bitcoin:tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q?amount=0.00012347",
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": null
      |            },
      |            "BTC_LightningLike": {
      |                "BIP21": null,
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": "lightning:lntb123470n1pwep8khpp5lmv5g57trf7r708rnlyclmu486h29zqm3tdz898crs3qvddtpz6sdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2fukzeqgx464q20r7g6mwg3aj5tjfgu0tzkvc3a5fe9yjjp0c8tjxanwhpzg2ptujvtuskd9yjflxgygyzu7ys3axk8630x4wyhuerxcqhx9yrq"
      |            }
      |        },
      |        "buyer": {
      |            "name": "the dude",
      |            "address1": null,
      |            "address2": null,
      |            "locality": null,
      |            "region": null,
      |            "postalCode": null,
      |            "country": null,
      |            "phone": null,
      |            "email": "hello@gmail.com"
      |        }
      |    }
      |}
    """.stripMargin

  val expiredNoPayments =
    """
      |{
      |    "data": {
      |        "url": "https://payments.mathbot.com/invoice?id=XoQdQVNM3bUhzUZYYnQuvo",
      |        "posData": null,
      |        "status": "expired",
      |        "btcPrice": "0.00012356",
      |        "btcDue": "0.00012356",
      |        "cryptoInfo": [
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "BTCLike",
      |                "rate": 8093.45609557555,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00000000",
      |                "price": "0.00012356",
      |                "due": "0.00012356",
      |                "paymentUrls": {
      |                    "BIP21": "bitcoin:tb1q36ktt079p4ce4a5369hjq7yz60mjg63w3s8y72?amount=0.00012356",
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": null
      |                },
      |                "address": "tb1q36ktt079p4ce4a5369hjq7yz60mjg63w3s8y72",
      |                "url": "https://payments.mathbot.com/i/BTC/XoQdQVNM3bUhzUZYYnQuvo",
      |                "totalDue": "0.00012356",
      |                "networkFee": "0.00000000",
      |                "txCount": 0,
      |                "cryptoPaid": "0.00000000",
      |                "payments": []
      |            },
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "LightningLike",
      |                "rate": 8093.45609557555,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00000000",
      |                "price": "0.00012356",
      |                "due": "0.00012356",
      |                "paymentUrls": {
      |                    "BIP21": null,
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": "lightning:lntb123560n1pwepxeppp5sglv9cp3e7xlgszt7py3m7ryu3gtyksmrmd2q8j6v5lr8x6cutsqdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2su3exwsp9qxacy4ad6nr0qlhnnh7lge6few77tv8r4fsxupt65vqnaeumhy37fr0wrhwcxyf2z2jvqccpdw035smx375h20c4zv03wsqrfk7mr"
      |                },
      |                "address": "lntb123560n1pwepxeppp5sglv9cp3e7xlgszt7py3m7ryu3gtyksmrmd2q8j6v5lr8x6cutsqdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2su3exwsp9qxacy4ad6nr0qlhnnh7lge6few77tv8r4fsxupt65vqnaeumhy37fr0wrhwcxyf2z2jvqccpdw035smx375h20c4zv03wsqrfk7mr",
      |                "url": "https://payments.mathbot.com/i/BTC_LightningLike/XoQdQVNM3bUhzUZYYnQuvo",
      |                "totalDue": "0.00012356",
      |                "networkFee": "0.00000000",
      |                "txCount": 0,
      |                "cryptoPaid": "0.00000000",
      |                "payments": []
      |            }
      |        ],
      |        "price": 1.0,
      |        "currency": "USD",
      |        "exRates": {
      |            "USD": 0.0
      |        },
      |        "buyerTotalBtcAmount": null,
      |        "itemDesc": "a test invoice",
      |        "itemCode": null,
      |        "orderId": "testing",
      |        "guid": "a08195ca-5415-4252-8fe1-7256eaeef418",
      |        "id": "XoQdQVNM3bUhzUZYYnQuvo",
      |        "invoiceTime": 1569757985000,
      |        "expirationTime": 1569758885000,
      |        "currentTime": 1569818387669,
      |        "lowFeeDetected": false,
      |        "btcPaid": "0.00000000",
      |        "rate": 8093.45609557555,
      |        "exceptionStatus": false,
      |        "paymentUrls": {
      |            "BIP21": "bitcoin:tb1q36ktt079p4ce4a5369hjq7yz60mjg63w3s8y72?amount=0.00012356",
      |            "BIP72": null,
      |            "BIP72b": null,
      |            "BIP73": null,
      |            "BOLT11": null
      |        },
      |        "refundAddressRequestPending": false,
      |        "buyerPaidBtcMinerFee": null,
      |        "bitcoinAddress": "tb1q36ktt079p4ce4a5369hjq7yz60mjg63w3s8y72",
      |        "token": "9PuFzfrrgU5zbV4wkgzHYK",
      |        "flags": {
      |            "refundable": false
      |        },
      |        "paymentSubtotals": {
      |            "BTC": 12356,
      |            "BTC_LightningLike": 12356
      |        },
      |        "paymentTotals": {
      |            "BTC": 12356,
      |            "BTC_LightningLike": 12356
      |        },
      |        "amountPaid": 0,
      |        "minerFees": {
      |            "BTC": {
      |                "satoshisPerByte": 1.0,
      |                "totalFee": 0
      |            }
      |        },
      |        "exchangeRates": {
      |            "BTC": {
      |                "USD": 0.0
      |            }
      |        },
      |        "supportedTransactionCurrencies": {
      |            "BTC": {
      |                "enabled": true
      |            }
      |        },
      |        "addresses": {
      |            "BTC": "tb1q36ktt079p4ce4a5369hjq7yz60mjg63w3s8y72",
      |            "BTC_LightningLike": "lntb123560n1pwepxeppp5sglv9cp3e7xlgszt7py3m7ryu3gtyksmrmd2q8j6v5lr8x6cutsqdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2su3exwsp9qxacy4ad6nr0qlhnnh7lge6few77tv8r4fsxupt65vqnaeumhy37fr0wrhwcxyf2z2jvqccpdw035smx375h20c4zv03wsqrfk7mr"
      |        },
      |        "paymentCodes": {
      |            "BTC": {
      |                "BIP21": "bitcoin:tb1q36ktt079p4ce4a5369hjq7yz60mjg63w3s8y72?amount=0.00012356",
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": null
      |            },
      |            "BTC_LightningLike": {
      |                "BIP21": null,
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": "lightning:lntb123560n1pwepxeppp5sglv9cp3e7xlgszt7py3m7ryu3gtyksmrmd2q8j6v5lr8x6cutsqdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2su3exwsp9qxacy4ad6nr0qlhnnh7lge6few77tv8r4fsxupt65vqnaeumhy37fr0wrhwcxyf2z2jvqccpdw035smx375h20c4zv03wsqrfk7mr"
      |            }
      |        },
      |        "buyer": {
      |            "name": "the dude",
      |            "address1": null,
      |            "address2": null,
      |            "locality": null,
      |            "region": null,
      |            "postalCode": null,
      |            "country": null,
      |            "phone": null,
      |            "email": "hello@gmail.com"
      |        }
      |    }
      |}
    """.stripMargin

  val completeLN: String =
    """
      |{
      |    "data": {
      |        "url": "https://payments.mathbot.com/invoice?id=GkYkQVaR9qGCavh6gk7Kpm",
      |        "posData": null,
      |        "status": "complete",
      |        "btcPrice": "0.00012356",
      |        "btcDue": "0.00000000",
      |        "cryptoInfo": [
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "BTCLike",
      |                "rate": 8093.45609557555,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00012356",
      |                "price": "0.00012356",
      |                "due": "0.00000000",
      |                "paymentUrls": {
      |                    "BIP21": "bitcoin:tb1qsvw8pajdplhsc9ktvcp0gmmsav3edaaq59nl5d?amount=0.00000000",
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": null
      |                },
      |                "address": "tb1qsvw8pajdplhsc9ktvcp0gmmsav3edaaq59nl5d",
      |                "url": "https://payments.mathbot.com/i/BTC/GkYkQVaR9qGCavh6gk7Kpm",
      |                "totalDue": "0.00012356",
      |                "networkFee": "0.00000000",
      |                "txCount": 0,
      |                "cryptoPaid": "0.00000000",
      |                "payments": [
      |                    {
      |                        "id": "57de739701ef3888fc5b01900b58a81a96bda376bd0f14f216550db94173c440",
      |                        "receivedDate": "2019-09-29T11:59:06",
      |                        "value": 0.00012356,
      |                        "fee": 0.0,
      |                        "paymentType": "LightningLike",
      |                        "confirmed": true,
      |                        "completed": true,
      |                        "destination": "lntb123560n1pwepxecpp52l0889cpauug3lzmqxgqkk9gr2ttmgmkh583fusk25xmjstnc3qqdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2lzt6efpn08mvpqehf76kvgluv8gsslwdukepyqxs58qgsajef92rgd6tj533p9dtk6fdcjaerzc9wskk66mh2lm0xh8q0anqrc2cvusqer6cap"
      |                    }
      |                ]
      |            },
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "LightningLike",
      |                "rate": 8093.45609557555,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00012356",
      |                "price": "0.00012356",
      |                "due": "0.00000000",
      |                "paymentUrls": {
      |                    "BIP21": null,
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": "lightning:lntb123560n1pwepxecpp52l0889cpauug3lzmqxgqkk9gr2ttmgmkh583fusk25xmjstnc3qqdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2lzt6efpn08mvpqehf76kvgluv8gsslwdukepyqxs58qgsajef92rgd6tj533p9dtk6fdcjaerzc9wskk66mh2lm0xh8q0anqrc2cvusqer6cap"
      |                },
      |                "address": "lntb123560n1pwepxecpp52l0889cpauug3lzmqxgqkk9gr2ttmgmkh583fusk25xmjstnc3qqdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2lzt6efpn08mvpqehf76kvgluv8gsslwdukepyqxs58qgsajef92rgd6tj533p9dtk6fdcjaerzc9wskk66mh2lm0xh8q0anqrc2cvusqer6cap",
      |                "url": "https://payments.mathbot.com/i/BTC_LightningLike/GkYkQVaR9qGCavh6gk7Kpm",
      |                "totalDue": "0.00012356",
      |                "networkFee": "0.00000000",
      |                "txCount": 1,
      |                "cryptoPaid": "0.00012356",
      |                "payments": [
      |                    {
      |                        "id": "57de739701ef3888fc5b01900b58a81a96bda376bd0f14f216550db94173c440",
      |                        "receivedDate": "2019-09-29T11:59:06",
      |                        "value": 0.00012356,
      |                        "fee": 0.0,
      |                        "paymentType": "LightningLike",
      |                        "confirmed": true,
      |                        "completed": true,
      |                        "destination": "lntb123560n1pwepxecpp52l0889cpauug3lzmqxgqkk9gr2ttmgmkh583fusk25xmjstnc3qqdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2lzt6efpn08mvpqehf76kvgluv8gsslwdukepyqxs58qgsajef92rgd6tj533p9dtk6fdcjaerzc9wskk66mh2lm0xh8q0anqrc2cvusqer6cap"
      |                    }
      |                ]
      |            }
      |        ],
      |        "price": 1.0,
      |        "currency": "USD",
      |        "exRates": {
      |            "USD": 0.0
      |        },
      |        "buyerTotalBtcAmount": null,
      |        "itemDesc": "a test invoice",
      |        "itemCode": null,
      |        "orderId": "testing",
      |        "guid": "9ec45f9c-6b92-4712-8d14-dac108b74751",
      |        "id": "GkYkQVaR9qGCavh6gk7Kpm",
      |        "invoiceTime": 1569758008000,
      |        "expirationTime": 1569758908000,
      |        "currentTime": 1569758522577,
      |        "lowFeeDetected": false,
      |        "btcPaid": "0.00012356",
      |        "rate": 8093.45609557555,
      |        "exceptionStatus": false,
      |        "paymentUrls": {
      |            "BIP21": "bitcoin:tb1qsvw8pajdplhsc9ktvcp0gmmsav3edaaq59nl5d?amount=0.00000000",
      |            "BIP72": null,
      |            "BIP72b": null,
      |            "BIP73": null,
      |            "BOLT11": null
      |        },
      |        "refundAddressRequestPending": false,
      |        "buyerPaidBtcMinerFee": null,
      |        "bitcoinAddress": "tb1qsvw8pajdplhsc9ktvcp0gmmsav3edaaq59nl5d",
      |        "token": "3Gtywtqw6RjBHTuMrxaLgt",
      |        "flags": {
      |            "refundable": false
      |        },
      |        "paymentSubtotals": {
      |            "BTC": 12356,
      |            "BTC_LightningLike": 12356
      |        },
      |        "paymentTotals": {
      |            "BTC": 12356,
      |            "BTC_LightningLike": 12356
      |        },
      |        "amountPaid": 0,
      |        "minerFees": {
      |            "BTC": {
      |                "satoshisPerByte": 1.0,
      |                "totalFee": 0
      |            }
      |        },
      |        "exchangeRates": {
      |            "BTC": {
      |                "USD": 0.0
      |            }
      |        },
      |        "supportedTransactionCurrencies": {
      |            "BTC": {
      |                "enabled": true
      |            }
      |        },
      |        "addresses": {
      |            "BTC": "tb1qsvw8pajdplhsc9ktvcp0gmmsav3edaaq59nl5d",
      |            "BTC_LightningLike": "lntb123560n1pwepxecpp52l0889cpauug3lzmqxgqkk9gr2ttmgmkh583fusk25xmjstnc3qqdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2lzt6efpn08mvpqehf76kvgluv8gsslwdukepyqxs58qgsajef92rgd6tj533p9dtk6fdcjaerzc9wskk66mh2lm0xh8q0anqrc2cvusqer6cap"
      |        },
      |        "paymentCodes": {
      |            "BTC": {
      |                "BIP21": "bitcoin:tb1qsvw8pajdplhsc9ktvcp0gmmsav3edaaq59nl5d?amount=0.00000000",
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": null
      |            },
      |            "BTC_LightningLike": {
      |                "BIP21": null,
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": "lightning:lntb123560n1pwepxecpp52l0889cpauug3lzmqxgqkk9gr2ttmgmkh583fusk25xmjstnc3qqdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2lzt6efpn08mvpqehf76kvgluv8gsslwdukepyqxs58qgsajef92rgd6tj533p9dtk6fdcjaerzc9wskk66mh2lm0xh8q0anqrc2cvusqer6cap"
      |            }
      |        },
      |        "buyer": {
      |            "name": "the dude",
      |            "address1": null,
      |            "address2": null,
      |            "locality": null,
      |            "region": null,
      |            "postalCode": null,
      |            "country": null,
      |            "phone": null,
      |            "email": "hello@gmail.com"
      |        }
      |    }
      |}
    """.stripMargin

  val completeBTC: String =
    """
      |{
      |    "data": {
      |        "url": "https://payments.mathbot.com/invoice?id=Qs7rFAGHffeGGTMgFfS2pf",
      |        "posData": null,
      |        "status": "complete",
      |        "btcPrice": "0.00012347",
      |        "btcDue": "0.00000000",
      |        "cryptoInfo": [
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "BTCLike",
      |                "rate": 8099.75637366377,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00012347",
      |                "price": "0.00012347",
      |                "due": "0.00000000",
      |                "paymentUrls": {
      |                    "BIP21": "bitcoin:tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q?amount=0.00000000",
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": null
      |                },
      |                "address": "tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q",
      |                "url": "https://payments.mathbot.com/i/BTC/Qs7rFAGHffeGGTMgFfS2pf",
      |                "totalDue": "0.00012347",
      |                "networkFee": "0.00000000",
      |                "txCount": 1,
      |                "cryptoPaid": "0.00012347",
      |                "payments": [
      |                    {
      |                        "id": "2c4d02f9461648f312d658b4f53d24dce51b747323a27e7f0662f1c0b3a6fe10-1",
      |                        "receivedDate": "2019-09-29T12:12:26",
      |                        "value": 0.00012347,
      |                        "fee": 0.0,
      |                        "paymentType": "BTCLike",
      |                        "confirmed": true,
      |                        "completed": true,
      |                        "destination": "tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q"
      |                    }
      |                ]
      |            },
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "LightningLike",
      |                "rate": 8099.75637366377,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00012347",
      |                "price": "0.00012347",
      |                "due": "0.00000000",
      |                "paymentUrls": {
      |                    "BIP21": null,
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": "lightning:lntb123470n1pwep8khpp5lmv5g57trf7r708rnlyclmu486h29zqm3tdz898crs3qvddtpz6sdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2fukzeqgx464q20r7g6mwg3aj5tjfgu0tzkvc3a5fe9yjjp0c8tjxanwhpzg2ptujvtuskd9yjflxgygyzu7ys3axk8630x4wyhuerxcqhx9yrq"
      |                },
      |                "address": "lntb123470n1pwep8khpp5lmv5g57trf7r708rnlyclmu486h29zqm3tdz898crs3qvddtpz6sdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2fukzeqgx464q20r7g6mwg3aj5tjfgu0tzkvc3a5fe9yjjp0c8tjxanwhpzg2ptujvtuskd9yjflxgygyzu7ys3axk8630x4wyhuerxcqhx9yrq",
      |                "url": "https://payments.mathbot.com/i/BTC_LightningLike/Qs7rFAGHffeGGTMgFfS2pf",
      |                "totalDue": "0.00012347",
      |                "networkFee": "0.00000000",
      |                "txCount": 0,
      |                "cryptoPaid": "0.00000000",
      |                "payments": [
      |                    {
      |                        "id": "2c4d02f9461648f312d658b4f53d24dce51b747323a27e7f0662f1c0b3a6fe10-1",
      |                        "receivedDate": "2019-09-29T12:12:26",
      |                        "value": 0.00012347,
      |                        "fee": 0.0,
      |                        "paymentType": "BTCLike",
      |                        "confirmed": true,
      |                        "completed": true,
      |                        "destination": "tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q"
      |                    }
      |                ]
      |            }
      |        ],
      |        "price": 1.0,
      |        "currency": "USD",
      |        "exRates": {
      |            "USD": 0.0
      |        },
      |        "buyerTotalBtcAmount": null,
      |        "itemDesc": "a test invoice",
      |        "itemCode": null,
      |        "orderId": "testing",
      |        "guid": "72b1abe4-fe94-47e8-bdce-cacff6eac143",
      |        "id": "Qs7rFAGHffeGGTMgFfS2pf",
      |        "invoiceTime": 1569758935000,
      |        "expirationTime": 1569759835000,
      |        "currentTime": 1569817960722,
      |        "lowFeeDetected": false,
      |        "btcPaid": "0.00012347",
      |        "rate": 8099.75637366377,
      |        "exceptionStatus": false,
      |        "paymentUrls": {
      |            "BIP21": "bitcoin:tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q?amount=0.00000000",
      |            "BIP72": null,
      |            "BIP72b": null,
      |            "BIP73": null,
      |            "BOLT11": null
      |        },
      |        "refundAddressRequestPending": false,
      |        "buyerPaidBtcMinerFee": null,
      |        "bitcoinAddress": "tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q",
      |        "token": "J1BfpSd1YawKgHST9KZxrq",
      |        "flags": {
      |            "refundable": false
      |        },
      |        "paymentSubtotals": {
      |            "BTC": 12347,
      |            "BTC_LightningLike": 12347
      |        },
      |        "paymentTotals": {
      |            "BTC": 12347,
      |            "BTC_LightningLike": 12347
      |        },
      |        "amountPaid": 0,
      |        "minerFees": {
      |            "BTC": {
      |                "satoshisPerByte": 1.0,
      |                "totalFee": 0
      |            }
      |        },
      |        "exchangeRates": {
      |            "BTC": {
      |                "USD": 0.0
      |            }
      |        },
      |        "supportedTransactionCurrencies": {
      |            "BTC": {
      |                "enabled": true
      |            }
      |        },
      |        "addresses": {
      |            "BTC": "tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q",
      |            "BTC_LightningLike": "lntb123470n1pwep8khpp5lmv5g57trf7r708rnlyclmu486h29zqm3tdz898crs3qvddtpz6sdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2fukzeqgx464q20r7g6mwg3aj5tjfgu0tzkvc3a5fe9yjjp0c8tjxanwhpzg2ptujvtuskd9yjflxgygyzu7ys3axk8630x4wyhuerxcqhx9yrq"
      |        },
      |        "paymentCodes": {
      |            "BTC": {
      |                "BIP21": "bitcoin:tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q?amount=0.00000000",
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": null
      |            },
      |            "BTC_LightningLike": {
      |                "BIP21": null,
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": "lightning:lntb123470n1pwep8khpp5lmv5g57trf7r708rnlyclmu486h29zqm3tdz898crs3qvddtpz6sdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2fukzeqgx464q20r7g6mwg3aj5tjfgu0tzkvc3a5fe9yjjp0c8tjxanwhpzg2ptujvtuskd9yjflxgygyzu7ys3axk8630x4wyhuerxcqhx9yrq"
      |            }
      |        },
      |        "buyer": {
      |            "name": "the dude",
      |            "address1": null,
      |            "address2": null,
      |            "locality": null,
      |            "region": null,
      |            "postalCode": null,
      |            "country": null,
      |            "phone": null,
      |            "email": "hello@gmail.com"
      |        }
      |    }
      |}
    """.stripMargin

  // not complete
  val paidBTC: String =
    """
      |{
      |    "data": {
      |        "url": "https://payments.mathbot.com/invoice?id=Qs7rFAGHffeGGTMgFfS2pf",
      |        "posData": null,
      |        "status": "paid",
      |        "btcPrice": "0.00012347",
      |        "btcDue": "0.00000000",
      |        "cryptoInfo": [
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "BTCLike",
      |                "rate": 8099.75637366377,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00012347",
      |                "price": "0.00012347",
      |                "due": "0.00000000",
      |                "paymentUrls": {
      |                    "BIP21": "bitcoin:tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q?amount=0.00000000",
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": null
      |                },
      |                "address": "tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q",
      |                "url": "https://payments.mathbot.com/i/BTC/Qs7rFAGHffeGGTMgFfS2pf",
      |                "totalDue": "0.00012347",
      |                "networkFee": "0.00000000",
      |                "txCount": 1,
      |                "cryptoPaid": "0.00012347",
      |                "payments": [
      |                    {
      |                        "id": "2c4d02f9461648f312d658b4f53d24dce51b747323a27e7f0662f1c0b3a6fe10-1",
      |                        "receivedDate": "2019-09-29T12:12:26",
      |                        "value": 0.00012347,
      |                        "fee": 0.0,
      |                        "paymentType": "BTCLike",
      |                        "confirmed": false,
      |                        "completed": false,
      |                        "destination": "tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q"
      |                    }
      |                ]
      |            },
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "LightningLike",
      |                "rate": 8099.75637366377,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00012347",
      |                "price": "0.00012347",
      |                "due": "0.00000000",
      |                "paymentUrls": {
      |                    "BIP21": null,
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": "lightning:lntb123470n1pwep8khpp5lmv5g57trf7r708rnlyclmu486h29zqm3tdz898crs3qvddtpz6sdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2fukzeqgx464q20r7g6mwg3aj5tjfgu0tzkvc3a5fe9yjjp0c8tjxanwhpzg2ptujvtuskd9yjflxgygyzu7ys3axk8630x4wyhuerxcqhx9yrq"
      |                },
      |                "address": "lntb123470n1pwep8khpp5lmv5g57trf7r708rnlyclmu486h29zqm3tdz898crs3qvddtpz6sdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2fukzeqgx464q20r7g6mwg3aj5tjfgu0tzkvc3a5fe9yjjp0c8tjxanwhpzg2ptujvtuskd9yjflxgygyzu7ys3axk8630x4wyhuerxcqhx9yrq",
      |                "url": "https://payments.mathbot.com/i/BTC_LightningLike/Qs7rFAGHffeGGTMgFfS2pf",
      |                "totalDue": "0.00012347",
      |                "networkFee": "0.00000000",
      |                "txCount": 0,
      |                "cryptoPaid": "0.00000000",
      |                "payments": [
      |                    {
      |                        "id": "2c4d02f9461648f312d658b4f53d24dce51b747323a27e7f0662f1c0b3a6fe10-1",
      |                        "receivedDate": "2019-09-29T12:12:26",
      |                        "value": 0.00012347,
      |                        "fee": 0.0,
      |                        "paymentType": "BTCLike",
      |                        "confirmed": false,
      |                        "completed": false,
      |                        "destination": "tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q"
      |                    }
      |                ]
      |            }
      |        ],
      |        "price": 1.0,
      |        "currency": "USD",
      |        "exRates": {
      |            "USD": 0.0
      |        },
      |        "buyerTotalBtcAmount": null,
      |        "itemDesc": "a test invoice",
      |        "itemCode": null,
      |        "orderId": "testing",
      |        "guid": "ec252bbd-2c07-4826-a15b-3ec77c108f56",
      |        "id": "Qs7rFAGHffeGGTMgFfS2pf",
      |        "invoiceTime": 1569758935000,
      |        "expirationTime": 1569759835000,
      |        "currentTime": 1569759148785,
      |        "lowFeeDetected": false,
      |        "btcPaid": "0.00012347",
      |        "rate": 8099.75637366377,
      |        "exceptionStatus": false,
      |        "paymentUrls": {
      |            "BIP21": "bitcoin:tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q?amount=0.00000000",
      |            "BIP72": null,
      |            "BIP72b": null,
      |            "BIP73": null,
      |            "BOLT11": null
      |        },
      |        "refundAddressRequestPending": false,
      |        "buyerPaidBtcMinerFee": null,
      |        "bitcoinAddress": "tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q",
      |        "token": "9Xdk9Z6tWTXf1A5w84ykBv",
      |        "flags": {
      |            "refundable": false
      |        },
      |        "paymentSubtotals": {
      |            "BTC": 12347,
      |            "BTC_LightningLike": 12347
      |        },
      |        "paymentTotals": {
      |            "BTC": 12347,
      |            "BTC_LightningLike": 12347
      |        },
      |        "amountPaid": 0,
      |        "minerFees": {
      |            "BTC": {
      |                "satoshisPerByte": 1.0,
      |                "totalFee": 0
      |            }
      |        },
      |        "exchangeRates": {
      |            "BTC": {
      |                "USD": 0.0
      |            }
      |        },
      |        "supportedTransactionCurrencies": {
      |            "BTC": {
      |                "enabled": true
      |            }
      |        },
      |        "addresses": {
      |            "BTC": "tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q",
      |            "BTC_LightningLike": "lntb123470n1pwep8khpp5lmv5g57trf7r708rnlyclmu486h29zqm3tdz898crs3qvddtpz6sdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2fukzeqgx464q20r7g6mwg3aj5tjfgu0tzkvc3a5fe9yjjp0c8tjxanwhpzg2ptujvtuskd9yjflxgygyzu7ys3axk8630x4wyhuerxcqhx9yrq"
      |        },
      |        "paymentCodes": {
      |            "BTC": {
      |                "BIP21": "bitcoin:tb1qv9vjljapf0nsfvuqqufsrd5tk63fvef9lrmf0q?amount=0.00000000",
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": null
      |            },
      |            "BTC_LightningLike": {
      |                "BIP21": null,
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": "lightning:lntb123470n1pwep8khpp5lmv5g57trf7r708rnlyclmu486h29zqm3tdz898crs3qvddtpz6sdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2fukzeqgx464q20r7g6mwg3aj5tjfgu0tzkvc3a5fe9yjjp0c8tjxanwhpzg2ptujvtuskd9yjflxgygyzu7ys3axk8630x4wyhuerxcqhx9yrq"
      |            }
      |        },
      |        "buyer": {
      |            "name": "the dude",
      |            "address1": null,
      |            "address2": null,
      |            "locality": null,
      |            "region": null,
      |            "postalCode": null,
      |            "country": null,
      |            "phone": null,
      |            "email": "hello@gmail.com"
      |        }
      |    }
      |}
    """.stripMargin

  val paidPartialNotConfirmed: String =
    """
      |{
      |    "data": {
      |        "url": "https://payments.mathbot.com/invoice?id=63AUbRFKDenu32QAe9gRh1",
      |        "posData": null,
      |        "status": "new",
      |        "btcPrice": "0.00125326",
      |        "btcDue": "0.00062762",
      |        "cryptoInfo": [
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "BTCLike",
      |                "rate": 7979.21233824511,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00062664",
      |                "price": "0.00125326",
      |                "due": "0.00062762",
      |                "paymentUrls": {
      |                    "BIP21": "bitcoin:tb1qj5aa7kkhnfv795h9xavl59x24w2g2wstveu5nm?amount=0.00062762",
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": null
      |                },
      |                "address": "tb1qj5aa7kkhnfv795h9xavl59x24w2g2wstveu5nm",
      |                "url": "https://payments.mathbot.com/i/BTC/63AUbRFKDenu32QAe9gRh1",
      |                "totalDue": "0.00125426",
      |                "networkFee": "0.00000100",
      |                "txCount": 1,
      |                "cryptoPaid": "0.00062664",
      |                "payments": [
      |                    {
      |                        "id": "977fad60ce45d2210ee006276be07abd78af3828f654728fea746c0bbf0d761c-0",
      |                        "receivedDate": "2019-09-29T16:02:29",
      |                        "value": 0.00062664,
      |                        "fee": 0.0,
      |                        "paymentType": "BTCLike",
      |                        "confirmed": false,
      |                        "completed": false,
      |                        "destination": "tb1qxk7470wzdtv20e45ywltmpe3rfutmca33kyap6"
      |                    }
      |                ]
      |            },
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "LightningLike",
      |                "rate": 7979.21233824511,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00062664",
      |                "price": "0.00125326",
      |                "due": "0.00062662",
      |                "paymentUrls": {
      |                    "BIP21": null,
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": "lightning:lntb1253260n1pwep4t5pp5saq094t664xdcm7k47w30aq9g72n20qyfzuqesv4d2fj8tksx2lsdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2mqyrl7zutwl8cmcqpl83y2aatckpll30zz4d96pu7sslcfqrgswncnqls0e4j03gsvnuns4q5jnur5y4etjs8h7j7zetkq2uq5lfcggpp5tysv"
      |                },
      |                "address": "lntb1253260n1pwep4t5pp5saq094t664xdcm7k47w30aq9g72n20qyfzuqesv4d2fj8tksx2lsdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2mqyrl7zutwl8cmcqpl83y2aatckpll30zz4d96pu7sslcfqrgswncnqls0e4j03gsvnuns4q5jnur5y4etjs8h7j7zetkq2uq5lfcggpp5tysv",
      |                "url": "https://payments.mathbot.com/i/BTC_LightningLike/63AUbRFKDenu32QAe9gRh1",
      |                "totalDue": "0.00125326",
      |                "networkFee": "0.00000000",
      |                "txCount": 0,
      |                "cryptoPaid": "0.00000000",
      |                "payments": [
      |                    {
      |                        "id": "977fad60ce45d2210ee006276be07abd78af3828f654728fea746c0bbf0d761c-0",
      |                        "receivedDate": "2019-09-29T16:02:29",
      |                        "value": 0.00062664,
      |                        "fee": 0.0,
      |                        "paymentType": "BTCLike",
      |                        "confirmed": false,
      |                        "completed": false,
      |                        "destination": "tb1qxk7470wzdtv20e45ywltmpe3rfutmca33kyap6"
      |                    }
      |                ]
      |            }
      |        ],
      |        "price": 10.0,
      |        "currency": "USD",
      |        "exRates": {
      |            "USD": 0.0
      |        },
      |        "buyerTotalBtcAmount": null,
      |        "itemDesc": "testing underpaid inv",
      |        "itemCode": null,
      |        "orderId": "testing",
      |        "guid": "a3cdb9db-0e9f-40e7-8b60-cd942f80707b",
      |        "id": "63AUbRFKDenu32QAe9gRh1",
      |        "invoiceTime": 1569772916000,
      |        "expirationTime": 1569773816000,
      |        "currentTime": 1569773014324,
      |        "lowFeeDetected": false,
      |        "btcPaid": "0.00062664",
      |        "rate": 7979.21233824511,
      |        "exceptionStatus": "paidPartial",
      |        "paymentUrls": {
      |            "BIP21": "bitcoin:tb1qj5aa7kkhnfv795h9xavl59x24w2g2wstveu5nm?amount=0.00062762",
      |            "BIP72": null,
      |            "BIP72b": null,
      |            "BIP73": null,
      |            "BOLT11": null
      |        },
      |        "refundAddressRequestPending": false,
      |        "buyerPaidBtcMinerFee": null,
      |        "bitcoinAddress": "tb1qj5aa7kkhnfv795h9xavl59x24w2g2wstveu5nm",
      |        "token": "SVRKA6TbJuNVgL8rk6bPbL",
      |        "flags": {
      |            "refundable": false
      |        },
      |        "paymentSubtotals": {
      |            "BTC": 125326,
      |            "BTC_LightningLike": 125326
      |        },
      |        "paymentTotals": {
      |            "BTC": 125426,
      |            "BTC_LightningLike": 125326
      |        },
      |        "amountPaid": 0,
      |        "minerFees": {
      |            "BTC": {
      |                "satoshisPerByte": 1.0,
      |                "totalFee": 100
      |            }
      |        },
      |        "exchangeRates": {
      |            "BTC": {
      |                "USD": 0.0
      |            }
      |        },
      |        "supportedTransactionCurrencies": {
      |            "BTC": {
      |                "enabled": true
      |            }
      |        },
      |        "addresses": {
      |            "BTC": "tb1qj5aa7kkhnfv795h9xavl59x24w2g2wstveu5nm",
      |            "BTC_LightningLike": "lntb1253260n1pwep4t5pp5saq094t664xdcm7k47w30aq9g72n20qyfzuqesv4d2fj8tksx2lsdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2mqyrl7zutwl8cmcqpl83y2aatckpll30zz4d96pu7sslcfqrgswncnqls0e4j03gsvnuns4q5jnur5y4etjs8h7j7zetkq2uq5lfcggpp5tysv"
      |        },
      |        "paymentCodes": {
      |            "BTC": {
      |                "BIP21": "bitcoin:tb1qj5aa7kkhnfv795h9xavl59x24w2g2wstveu5nm?amount=0.00062762",
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": null
      |            },
      |            "BTC_LightningLike": {
      |                "BIP21": null,
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": "lightning:lntb1253260n1pwep4t5pp5saq094t664xdcm7k47w30aq9g72n20qyfzuqesv4d2fj8tksx2lsdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2mqyrl7zutwl8cmcqpl83y2aatckpll30zz4d96pu7sslcfqrgswncnqls0e4j03gsvnuns4q5jnur5y4etjs8h7j7zetkq2uq5lfcggpp5tysv"
      |            }
      |        },
      |        "buyer": {
      |            "name": "the dude",
      |            "address1": null,
      |            "address2": null,
      |            "locality": null,
      |            "region": null,
      |            "postalCode": null,
      |            "country": null,
      |            "phone": null,
      |            "email": "hello@gmail.com"
      |        }
      |    }
      |}
    """.stripMargin

  val paidPartialConfirmed: String =
    """
      |{
      |    "data": {
      |        "url": "https://payments.mathbot.com/invoice?id=63AUbRFKDenu32QAe9gRh1",
      |        "posData": null,
      |        "status": "expired",
      |        "btcPrice": "0.00125326",
      |        "btcDue": "0.00062762",
      |        "cryptoInfo": [
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "BTCLike",
      |                "rate": 7979.21233824511,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00062664",
      |                "price": "0.00125326",
      |                "due": "0.00062762",
      |                "paymentUrls": {
      |                    "BIP21": "bitcoin:tb1qj5aa7kkhnfv795h9xavl59x24w2g2wstveu5nm?amount=0.00062762",
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": null
      |                },
      |                "address": "tb1qj5aa7kkhnfv795h9xavl59x24w2g2wstveu5nm",
      |                "url": "https://payments.mathbot.com/i/BTC/63AUbRFKDenu32QAe9gRh1",
      |                "totalDue": "0.00125426",
      |                "networkFee": "0.00000100",
      |                "txCount": 1,
      |                "cryptoPaid": "0.00062664",
      |                "payments": [
      |                    {
      |                        "id": "977fad60ce45d2210ee006276be07abd78af3828f654728fea746c0bbf0d761c-0",
      |                        "receivedDate": "2019-09-29T16:02:29",
      |                        "value": 0.00062664,
      |                        "fee": 0.0,
      |                        "paymentType": "BTCLike",
      |                        "confirmed": true,
      |                        "completed": true,
      |                        "destination": "tb1qxk7470wzdtv20e45ywltmpe3rfutmca33kyap6"
      |                    }
      |                ]
      |            },
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "LightningLike",
      |                "rate": 7979.21233824511,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00062664",
      |                "price": "0.00125326",
      |                "due": "0.00062662",
      |                "paymentUrls": {
      |                    "BIP21": null,
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": "lightning:lntb1253260n1pwep4t5pp5saq094t664xdcm7k47w30aq9g72n20qyfzuqesv4d2fj8tksx2lsdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2mqyrl7zutwl8cmcqpl83y2aatckpll30zz4d96pu7sslcfqrgswncnqls0e4j03gsvnuns4q5jnur5y4etjs8h7j7zetkq2uq5lfcggpp5tysv"
      |                },
      |                "address": "lntb1253260n1pwep4t5pp5saq094t664xdcm7k47w30aq9g72n20qyfzuqesv4d2fj8tksx2lsdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2mqyrl7zutwl8cmcqpl83y2aatckpll30zz4d96pu7sslcfqrgswncnqls0e4j03gsvnuns4q5jnur5y4etjs8h7j7zetkq2uq5lfcggpp5tysv",
      |                "url": "https://payments.mathbot.com/i/BTC_LightningLike/63AUbRFKDenu32QAe9gRh1",
      |                "totalDue": "0.00125326",
      |                "networkFee": "0.00000000",
      |                "txCount": 0,
      |                "cryptoPaid": "0.00000000",
      |                "payments": [
      |                    {
      |                        "id": "977fad60ce45d2210ee006276be07abd78af3828f654728fea746c0bbf0d761c-0",
      |                        "receivedDate": "2019-09-29T16:02:29",
      |                        "value": 0.00062664,
      |                        "fee": 0.0,
      |                        "paymentType": "BTCLike",
      |                        "confirmed": true,
      |                        "completed": true,
      |                        "destination": "tb1qxk7470wzdtv20e45ywltmpe3rfutmca33kyap6"
      |                    }
      |                ]
      |            }
      |        ],
      |        "price": 10.0,
      |        "currency": "USD",
      |        "exRates": {
      |            "USD": 0.0
      |        },
      |        "buyerTotalBtcAmount": null,
      |        "itemDesc": "testing underpaid inv",
      |        "itemCode": null,
      |        "orderId": "testing",
      |        "guid": "acecb3ee-6c56-4843-8171-41a314326872",
      |        "id": "63AUbRFKDenu32QAe9gRh1",
      |        "invoiceTime": 1569772916000,
      |        "expirationTime": 1569773816000,
      |        "currentTime": 1569793471111,
      |        "lowFeeDetected": false,
      |        "btcPaid": "0.00062664",
      |        "rate": 7979.21233824511,
      |        "exceptionStatus": "paidPartial",
      |        "paymentUrls": {
      |            "BIP21": "bitcoin:tb1qj5aa7kkhnfv795h9xavl59x24w2g2wstveu5nm?amount=0.00062762",
      |            "BIP72": null,
      |            "BIP72b": null,
      |            "BIP73": null,
      |            "BOLT11": null
      |        },
      |        "refundAddressRequestPending": false,
      |        "buyerPaidBtcMinerFee": null,
      |        "bitcoinAddress": "tb1qj5aa7kkhnfv795h9xavl59x24w2g2wstveu5nm",
      |        "token": "XwS5sNBtUz7AqrcLjqp2MA",
      |        "flags": {
      |            "refundable": false
      |        },
      |        "paymentSubtotals": {
      |            "BTC": 125326,
      |            "BTC_LightningLike": 125326
      |        },
      |        "paymentTotals": {
      |            "BTC": 125426,
      |            "BTC_LightningLike": 125326
      |        },
      |        "amountPaid": 0,
      |        "minerFees": {
      |            "BTC": {
      |                "satoshisPerByte": 1.0,
      |                "totalFee": 100
      |            }
      |        },
      |        "exchangeRates": {
      |            "BTC": {
      |                "USD": 0.0
      |            }
      |        },
      |        "supportedTransactionCurrencies": {
      |            "BTC": {
      |                "enabled": true
      |            }
      |        },
      |        "addresses": {
      |            "BTC": "tb1qj5aa7kkhnfv795h9xavl59x24w2g2wstveu5nm",
      |            "BTC_LightningLike": "lntb1253260n1pwep4t5pp5saq094t664xdcm7k47w30aq9g72n20qyfzuqesv4d2fj8tksx2lsdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2mqyrl7zutwl8cmcqpl83y2aatckpll30zz4d96pu7sslcfqrgswncnqls0e4j03gsvnuns4q5jnur5y4etjs8h7j7zetkq2uq5lfcggpp5tysv"
      |        },
      |        "paymentCodes": {
      |            "BTC": {
      |                "BIP21": "bitcoin:tb1qj5aa7kkhnfv795h9xavl59x24w2g2wstveu5nm?amount=0.00062762",
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": null
      |            },
      |            "BTC_LightningLike": {
      |                "BIP21": null,
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": "lightning:lntb1253260n1pwep4t5pp5saq094t664xdcm7k47w30aq9g72n20qyfzuqesv4d2fj8tksx2lsdpc2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueefxqzurcqp2mqyrl7zutwl8cmcqpl83y2aatckpll30zz4d96pu7sslcfqrgswncnqls0e4j03gsvnuns4q5jnur5y4etjs8h7j7zetkq2uq5lfcggpp5tysv"
      |            }
      |        },
      |        "buyer": {
      |            "name": "the dude",
      |            "address1": null,
      |            "address2": null,
      |            "locality": null,
      |            "region": null,
      |            "postalCode": null,
      |            "country": null,
      |            "phone": null,
      |            "email": "hello@gmail.com"
      |        }
      |    }
      |}
    """.stripMargin

  val paidLateNotConfirmed: String =
    """
      |{
      |    "data": {
      |        "url": "https://payments.mathbot.com/invoice?id=4nt3KwDVHBAbMPz3YWBLEc",
      |        "posData": null,
      |        "status": "expired",
      |        "btcPrice": "0.00123569",
      |        "btcDue": "0.00000000",
      |        "cryptoInfo": [
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "BTCLike",
      |                "rate": 8092.65834961917,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00123569",
      |                "price": "0.00123569",
      |                "due": "0.00000000",
      |                "paymentUrls": {
      |                    "BIP21": "bitcoin:tb1q6x8f7kf8vyak6pla3cya9qdy0um4ezvxzwxcy3?amount=0.00000000",
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": null
      |                },
      |                "address": "tb1q6x8f7kf8vyak6pla3cya9qdy0um4ezvxzwxcy3",
      |                "url": "https://payments.mathbot.com/i/BTC/4nt3KwDVHBAbMPz3YWBLEc",
      |                "totalDue": "0.00123569",
      |                "networkFee": "0.00000000",
      |                "txCount": 1,
      |                "cryptoPaid": "0.00123569",
      |                "payments": [
      |                    {
      |                        "id": "b9995fb9082a976dde028d1eb2746a613a59300cf86dcd35cf80e22d1ba669cb-1",
      |                        "receivedDate": "2019-09-30T00:26:34",
      |                        "value": 0.00123569,
      |                        "fee": 0.0,
      |                        "paymentType": "BTCLike",
      |                        "confirmed": false,
      |                        "completed": false,
      |                        "destination": "tb1q6x8f7kf8vyak6pla3cya9qdy0um4ezvxzwxcy3"
      |                    }
      |                ]
      |            },
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "LightningLike",
      |                "rate": 8092.65834961917,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00123569",
      |                "price": "0.00123569",
      |                "due": "0.00000000",
      |                "paymentUrls": {
      |                    "BIP21": null,
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": "lightning:lntb1235690n1pwezvtkpp57jr6axzv4yu5hn06yhf8q85sa627fx6d56m3mx6lymfgy44pmu2qdzd2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueeqd3shgefqwpshjmt9de6zjxqzurcqp2gu3z9j9t0dcs62nrs447c3exvtm46ess97290ttxpg00e0avjphxakwcgaqx6aas2mu7jqqprh0ykae6epyawlwamu8rxgra8kqf50sq7rq6wu"
      |                },
      |                "address": "lntb1235690n1pwezvtkpp57jr6axzv4yu5hn06yhf8q85sa627fx6d56m3mx6lymfgy44pmu2qdzd2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueeqd3shgefqwpshjmt9de6zjxqzurcqp2gu3z9j9t0dcs62nrs447c3exvtm46ess97290ttxpg00e0avjphxakwcgaqx6aas2mu7jqqprh0ykae6epyawlwamu8rxgra8kqf50sq7rq6wu",
      |                "url": "https://payments.mathbot.com/i/BTC_LightningLike/4nt3KwDVHBAbMPz3YWBLEc",
      |                "totalDue": "0.00123569",
      |                "networkFee": "0.00000000",
      |                "txCount": 0,
      |                "cryptoPaid": "0.00000000",
      |                "payments": [
      |                    {
      |                        "id": "b9995fb9082a976dde028d1eb2746a613a59300cf86dcd35cf80e22d1ba669cb-1",
      |                        "receivedDate": "2019-09-30T00:26:34",
      |                        "value": 0.00123569,
      |                        "fee": 0.0,
      |                        "paymentType": "BTCLike",
      |                        "confirmed": false,
      |                        "completed": false,
      |                        "destination": "tb1q6x8f7kf8vyak6pla3cya9qdy0um4ezvxzwxcy3"
      |                    }
      |                ]
      |            }
      |        ],
      |        "price": 10.0,
      |        "currency": "USD",
      |        "exRates": {
      |            "USD": 0.0
      |        },
      |        "buyerTotalBtcAmount": null,
      |        "itemDesc": "testing late payment",
      |        "itemCode": null,
      |        "orderId": "testing late payment",
      |        "guid": "d22040bd-dd5c-4a2a-abdd-adf5e97d1e49",
      |        "id": "4nt3KwDVHBAbMPz3YWBLEc",
      |        "invoiceTime": 1569796470000,
      |        "expirationTime": 1569797370000,
      |        "currentTime": 1569803220257,
      |        "lowFeeDetected": false,
      |        "btcPaid": "0.00123569",
      |        "rate": 8092.65834961917,
      |        "exceptionStatus": "paidLate",
      |        "paymentUrls": {
      |            "BIP21": "bitcoin:tb1q6x8f7kf8vyak6pla3cya9qdy0um4ezvxzwxcy3?amount=0.00000000",
      |            "BIP72": null,
      |            "BIP72b": null,
      |            "BIP73": null,
      |            "BOLT11": null
      |        },
      |        "refundAddressRequestPending": false,
      |        "buyerPaidBtcMinerFee": null,
      |        "bitcoinAddress": "tb1q6x8f7kf8vyak6pla3cya9qdy0um4ezvxzwxcy3",
      |        "token": "VapKTK5auuJjmjJqJ6SkeJ",
      |        "flags": {
      |            "refundable": false
      |        },
      |        "paymentSubtotals": {
      |            "BTC": 123569,
      |            "BTC_LightningLike": 123569
      |        },
      |        "paymentTotals": {
      |            "BTC": 123569,
      |            "BTC_LightningLike": 123569
      |        },
      |        "amountPaid": 0,
      |        "minerFees": {
      |            "BTC": {
      |                "satoshisPerByte": 1.0,
      |                "totalFee": 0
      |            }
      |        },
      |        "exchangeRates": {
      |            "BTC": {
      |                "USD": 0.0
      |            }
      |        },
      |        "supportedTransactionCurrencies": {
      |            "BTC": {
      |                "enabled": true
      |            }
      |        },
      |        "addresses": {
      |            "BTC": "tb1q6x8f7kf8vyak6pla3cya9qdy0um4ezvxzwxcy3",
      |            "BTC_LightningLike": "lntb1235690n1pwezvtkpp57jr6axzv4yu5hn06yhf8q85sa627fx6d56m3mx6lymfgy44pmu2qdzd2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueeqd3shgefqwpshjmt9de6zjxqzurcqp2gu3z9j9t0dcs62nrs447c3exvtm46ess97290ttxpg00e0avjphxakwcgaqx6aas2mu7jqqprh0ykae6epyawlwamu8rxgra8kqf50sq7rq6wu"
      |        },
      |        "paymentCodes": {
      |            "BTC": {
      |                "BIP21": "bitcoin:tb1q6x8f7kf8vyak6pla3cya9qdy0um4ezvxzwxcy3?amount=0.00000000",
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": null
      |            },
      |            "BTC_LightningLike": {
      |                "BIP21": null,
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": "lightning:lntb1235690n1pwezvtkpp57jr6axzv4yu5hn06yhf8q85sa627fx6d56m3mx6lymfgy44pmu2qdzd2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueeqd3shgefqwpshjmt9de6zjxqzurcqp2gu3z9j9t0dcs62nrs447c3exvtm46ess97290ttxpg00e0avjphxakwcgaqx6aas2mu7jqqprh0ykae6epyawlwamu8rxgra8kqf50sq7rq6wu"
      |            }
      |        },
      |        "buyer": {
      |            "name": "the dude",
      |            "address1": null,
      |            "address2": null,
      |            "locality": null,
      |            "region": null,
      |            "postalCode": null,
      |            "country": null,
      |            "phone": null,
      |            "email": "hello@gmail.com"
      |        }
      |    }
      |}
    """.stripMargin

  val paidLateConfirmed: String =
    """
      |{
      |    "data": {
      |        "url": "https://payments.mathbot.com/invoice?id=4nt3KwDVHBAbMPz3YWBLEc",
      |        "posData": null,
      |        "status": "expired",
      |        "btcPrice": "0.00123569",
      |        "btcDue": "0.00000000",
      |        "cryptoInfo": [
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "BTCLike",
      |                "rate": 8092.65834961917,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00123569",
      |                "price": "0.00123569",
      |                "due": "0.00000000",
      |                "paymentUrls": {
      |                    "BIP21": "bitcoin:tb1q6x8f7kf8vyak6pla3cya9qdy0um4ezvxzwxcy3?amount=0.00000000",
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": null
      |                },
      |                "address": "tb1q6x8f7kf8vyak6pla3cya9qdy0um4ezvxzwxcy3",
      |                "url": "https://payments.mathbot.com/i/BTC/4nt3KwDVHBAbMPz3YWBLEc",
      |                "totalDue": "0.00123569",
      |                "networkFee": "0.00000000",
      |                "txCount": 1,
      |                "cryptoPaid": "0.00123569",
      |                "payments": [
      |                    {
      |                        "id": "b9995fb9082a976dde028d1eb2746a613a59300cf86dcd35cf80e22d1ba669cb-1",
      |                        "receivedDate": "2019-09-30T00:26:34",
      |                        "value": 0.00123569,
      |                        "fee": 0.0,
      |                        "paymentType": "BTCLike",
      |                        "confirmed": true,
      |                        "completed": false,
      |                        "destination": "tb1q6x8f7kf8vyak6pla3cya9qdy0um4ezvxzwxcy3"
      |                    }
      |                ]
      |            },
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "LightningLike",
      |                "rate": 8092.65834961917,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00123569",
      |                "price": "0.00123569",
      |                "due": "0.00000000",
      |                "paymentUrls": {
      |                    "BIP21": null,
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": "lightning:lntb1235690n1pwezvtkpp57jr6axzv4yu5hn06yhf8q85sa627fx6d56m3mx6lymfgy44pmu2qdzd2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueeqd3shgefqwpshjmt9de6zjxqzurcqp2gu3z9j9t0dcs62nrs447c3exvtm46ess97290ttxpg00e0avjphxakwcgaqx6aas2mu7jqqprh0ykae6epyawlwamu8rxgra8kqf50sq7rq6wu"
      |                },
      |                "address": "lntb1235690n1pwezvtkpp57jr6axzv4yu5hn06yhf8q85sa627fx6d56m3mx6lymfgy44pmu2qdzd2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueeqd3shgefqwpshjmt9de6zjxqzurcqp2gu3z9j9t0dcs62nrs447c3exvtm46ess97290ttxpg00e0avjphxakwcgaqx6aas2mu7jqqprh0ykae6epyawlwamu8rxgra8kqf50sq7rq6wu",
      |                "url": "https://payments.mathbot.com/i/BTC_LightningLike/4nt3KwDVHBAbMPz3YWBLEc",
      |                "totalDue": "0.00123569",
      |                "networkFee": "0.00000000",
      |                "txCount": 0,
      |                "cryptoPaid": "0.00000000",
      |                "payments": [
      |                    {
      |                        "id": "b9995fb9082a976dde028d1eb2746a613a59300cf86dcd35cf80e22d1ba669cb-1",
      |                        "receivedDate": "2019-09-30T00:26:34",
      |                        "value": 0.00123569,
      |                        "fee": 0.0,
      |                        "paymentType": "BTCLike",
      |                        "confirmed": true,
      |                        "completed": false,
      |                        "destination": "tb1q6x8f7kf8vyak6pla3cya9qdy0um4ezvxzwxcy3"
      |                    }
      |                ]
      |            }
      |        ],
      |        "price": 10.0,
      |        "currency": "USD",
      |        "exRates": {
      |            "USD": 0.0
      |        },
      |        "buyerTotalBtcAmount": null,
      |        "itemDesc": "testing late payment",
      |        "itemCode": null,
      |        "orderId": "testing late payment",
      |        "guid": "dc1faa1a-ac22-4de8-9636-399f9ec68700",
      |        "id": "4nt3KwDVHBAbMPz3YWBLEc",
      |        "invoiceTime": 1569796470000,
      |        "expirationTime": 1569797370000,
      |        "currentTime": 1569807476410,
      |        "lowFeeDetected": false,
      |        "btcPaid": "0.00123569",
      |        "rate": 8092.65834961917,
      |        "exceptionStatus": "paidLate",
      |        "paymentUrls": {
      |            "BIP21": "bitcoin:tb1q6x8f7kf8vyak6pla3cya9qdy0um4ezvxzwxcy3?amount=0.00000000",
      |            "BIP72": null,
      |            "BIP72b": null,
      |            "BIP73": null,
      |            "BOLT11": null
      |        },
      |        "refundAddressRequestPending": false,
      |        "buyerPaidBtcMinerFee": null,
      |        "bitcoinAddress": "tb1q6x8f7kf8vyak6pla3cya9qdy0um4ezvxzwxcy3",
      |        "token": "59akquCQcSShpn3SFsWFPn",
      |        "flags": {
      |            "refundable": false
      |        },
      |        "paymentSubtotals": {
      |            "BTC": 123569,
      |            "BTC_LightningLike": 123569
      |        },
      |        "paymentTotals": {
      |            "BTC": 123569,
      |            "BTC_LightningLike": 123569
      |        },
      |        "amountPaid": 0,
      |        "minerFees": {
      |            "BTC": {
      |                "satoshisPerByte": 1.0,
      |                "totalFee": 0
      |            }
      |        },
      |        "exchangeRates": {
      |            "BTC": {
      |                "USD": 0.0
      |            }
      |        },
      |        "supportedTransactionCurrencies": {
      |            "BTC": {
      |                "enabled": true
      |            }
      |        },
      |        "addresses": {
      |            "BTC": "tb1q6x8f7kf8vyak6pla3cya9qdy0um4ezvxzwxcy3",
      |            "BTC_LightningLike": "lntb1235690n1pwezvtkpp57jr6axzv4yu5hn06yhf8q85sa627fx6d56m3mx6lymfgy44pmu2qdzd2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueeqd3shgefqwpshjmt9de6zjxqzurcqp2gu3z9j9t0dcs62nrs447c3exvtm46ess97290ttxpg00e0avjphxakwcgaqx6aas2mu7jqqprh0ykae6epyawlwamu8rxgra8kqf50sq7rq6wu"
      |        },
      |        "paymentCodes": {
      |            "BTC": {
      |                "BIP21": "bitcoin:tb1q6x8f7kf8vyak6pla3cya9qdy0um4ezvxzwxcy3?amount=0.00000000",
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": null
      |            },
      |            "BTC_LightningLike": {
      |                "BIP21": null,
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": "lightning:lntb1235690n1pwezvtkpp57jr6axzv4yu5hn06yhf8q85sa627fx6d56m3mx6lymfgy44pmu2qdzd2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gs8getnw35kueeqd3shgefqwpshjmt9de6zjxqzurcqp2gu3z9j9t0dcs62nrs447c3exvtm46ess97290ttxpg00e0avjphxakwcgaqx6aas2mu7jqqprh0ykae6epyawlwamu8rxgra8kqf50sq7rq6wu"
      |            }
      |        },
      |        "buyer": {
      |            "name": "the dude",
      |            "address1": null,
      |            "address2": null,
      |            "locality": null,
      |            "region": null,
      |            "postalCode": null,
      |            "country": null,
      |            "phone": null,
      |            "email": "hello@gmail.com"
      |        }
      |    }
      |}
    """.stripMargin

  val invalid =
    """
      |{
      |    "data": {
      |        "url": "https://payments.mathbot.com/invoice?id=CKqUVEWrtbuxxiqwPqi7uy",
      |        "posData": null,
      |        "status": "invalid",
      |        "btcPrice": "0.00012489",
      |        "btcDue": "0.00012489",
      |        "cryptoInfo": [
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "BTCLike",
      |                "rate": 8007.14021651841,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00000000",
      |                "price": "0.00012489",
      |                "due": "0.00012489",
      |                "paymentUrls": {
      |                    "BIP21": "bitcoin:tb1qk260tpf0nq2akff4jkrdapzt3kszwhtczqfqul?amount=0.00012489",
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": null
      |                },
      |                "address": "tb1qk260tpf0nq2akff4jkrdapzt3kszwhtczqfqul",
      |                "url": "https://payments.mathbot.com/i/BTC/CKqUVEWrtbuxxiqwPqi7uy",
      |                "totalDue": "0.00012489",
      |                "networkFee": "0.00000000",
      |                "txCount": 0,
      |                "cryptoPaid": "0.00000000",
      |                "payments": []
      |            },
      |            {
      |                "cryptoCode": "BTC",
      |                "paymentType": "LightningLike",
      |                "rate": 8007.14021651841,
      |                "exRates": {
      |                    "USD": 0.0
      |                },
      |                "paid": "0.00000000",
      |                "price": "0.00012489",
      |                "due": "0.00012489",
      |                "paymentUrls": {
      |                    "BIP21": null,
      |                    "BIP72": null,
      |                    "BIP72b": null,
      |                    "BIP73": null,
      |                    "BOLT11": "lightning:lntb124890n1pwc67z5pp5rt4xphuekklqgcm742h5fjgc62pjj523yp8lcypk968x0drcxuqqdpd2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gszjxqzurcqp2jntptn0nfn6cgd9vhuy6k3eh9uasxy7fv09z6ts46th7mzdhs5w3eqh3tpld9yvvdf6jzf89tjyuryz9nmju78jk0qh3d2ql4tc0s9cqu0c09y"
      |                },
      |                "address": "lntb124890n1pwc67z5pp5rt4xphuekklqgcm742h5fjgc62pjj523yp8lcypk968x0drcxuqqdpd2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gszjxqzurcqp2jntptn0nfn6cgd9vhuy6k3eh9uasxy7fv09z6ts46th7mzdhs5w3eqh3tpld9yvvdf6jzf89tjyuryz9nmju78jk0qh3d2ql4tc0s9cqu0c09y",
      |                "url": "https://payments.mathbot.com/i/BTC_LightningLike/CKqUVEWrtbuxxiqwPqi7uy",
      |                "totalDue": "0.00012489",
      |                "networkFee": "0.00000000",
      |                "txCount": 0,
      |                "cryptoPaid": "0.00000000",
      |                "payments": []
      |            }
      |        ],
      |        "price": 1.0,
      |        "currency": "USD",
      |        "exRates": {
      |            "USD": 0.0
      |        },
      |        "buyerTotalBtcAmount": null,
      |        "itemDesc": null,
      |        "itemCode": null,
      |        "orderId": null,
      |        "guid": "b1597901-e3df-4e34-a738-755d964753c1",
      |        "id": "CKqUVEWrtbuxxiqwPqi7uy",
      |        "invoiceTime": 1569552468000,
      |        "expirationTime": 1569553368000,
      |        "currentTime": 1569818806909,
      |        "lowFeeDetected": false,
      |        "btcPaid": "0.00000000",
      |        "rate": 8007.14021651841,
      |        "exceptionStatus": "marked",
      |        "paymentUrls": {
      |            "BIP21": "bitcoin:tb1qk260tpf0nq2akff4jkrdapzt3kszwhtczqfqul?amount=0.00012489",
      |            "BIP72": null,
      |            "BIP72b": null,
      |            "BIP73": null,
      |            "BOLT11": null
      |        },
      |        "refundAddressRequestPending": false,
      |        "buyerPaidBtcMinerFee": null,
      |        "bitcoinAddress": "tb1qk260tpf0nq2akff4jkrdapzt3kszwhtczqfqul",
      |        "token": "YBr6n1ZzGY7X27AaeBiqyL",
      |        "flags": {
      |            "refundable": false
      |        },
      |        "paymentSubtotals": {
      |            "BTC": 12489,
      |            "BTC_LightningLike": 12489
      |        },
      |        "paymentTotals": {
      |            "BTC": 12489,
      |            "BTC_LightningLike": 12489
      |        },
      |        "amountPaid": 0,
      |        "minerFees": {
      |            "BTC": {
      |                "satoshisPerByte": 1.0,
      |                "totalFee": 0
      |            }
      |        },
      |        "exchangeRates": {
      |            "BTC": {
      |                "USD": 0.0
      |            }
      |        },
      |        "supportedTransactionCurrencies": {
      |            "BTC": {
      |                "enabled": true
      |            }
      |        },
      |        "addresses": {
      |            "BTC": "tb1qk260tpf0nq2akff4jkrdapzt3kszwhtczqfqul",
      |            "BTC_LightningLike": "lntb124890n1pwc67z5pp5rt4xphuekklqgcm742h5fjgc62pjj523yp8lcypk968x0drcxuqqdpd2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gszjxqzurcqp2jntptn0nfn6cgd9vhuy6k3eh9uasxy7fv09z6ts46th7mzdhs5w3eqh3tpld9yvvdf6jzf89tjyuryz9nmju78jk0qh3d2ql4tc0s9cqu0c09y"
      |        },
      |        "paymentCodes": {
      |            "BTC": {
      |                "BIP21": "bitcoin:tb1qk260tpf0nq2akff4jkrdapzt3kszwhtczqfqul?amount=0.00012489",
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": null
      |            },
      |            "BTC_LightningLike": {
      |                "BIP21": null,
      |                "BIP72": null,
      |                "BIP72b": null,
      |                "BIP73": null,
      |                "BOLT11": "lightning:lntb124890n1pwc67z5pp5rt4xphuekklqgcm742h5fjgc62pjj523yp8lcypk968x0drcxuqqdpd2pskjepqw3hjqmtpw35xymm5yq5y7unyv4ezqj2y8gszjxqzurcqp2jntptn0nfn6cgd9vhuy6k3eh9uasxy7fv09z6ts46th7mzdhs5w3eqh3tpld9yvvdf6jzf89tjyuryz9nmju78jk0qh3d2ql4tc0s9cqu0c09y"
      |            }
      |        },
      |        "buyer": {
      |            "name": "the dude",
      |            "address1": null,
      |            "address2": null,
      |            "locality": null,
      |            "region": null,
      |            "postalCode": null,
      |            "country": null,
      |            "phone": null,
      |            "email": "dude@protonmail.com"
      |        }
      |    }
      |}
    """.stripMargin

}
class ChargeInfoResponseTest extends scalatest.funsuite.AnyFunSuite with Matchers {

  import ChargeInfoResponseTest._
  test("json parse") {

    val stream = getClass.getResourceAsStream("/btcpayserverInvoiceResponse.json")
    val lines = io.Source.fromInputStream(stream).getLines
    val json = Json.parse(lines.mkString)
    stream.close()
    val r = json.validate[ChargeInfoResponse]
    assert(r.isSuccess)
  }

  test("fails when no derivation scheme") {

    val json =
      """
        |{"facade":"pos/invoice",
        |"data":{
        |"url":"https://payments.mathbot.com/invoice?id=6Rh3rp3TRnbia3zYho9buK",
        |"posData":null,
        |"status":"new",
        |"btcPrice":null,"btcDue":null,
        |"cryptoInfo":[{"cryptoCode":"BTC","paymentType":"LightningLike","rate":7160.19770442063,"exRates":{"USD":0},"paid":"0.00000000","price":"0.00013967","due":"0.00013967","paymentUrls":{"BIP21":null,"BIP72":null,"BIP72b":null,"BIP73":null,"BOLT11":"lightning:lnbc139670n1pwlu09epp5h8sd2jh7hyyjp4nzxmknfw5hmzynv74wp36wnwuf4c36vl84gqpsdzl2pskjepqw3hjqmtpw35xymm5ypehgmmjv5szsnmjv3jhygzfgsazqmtpw35xymm59e3k7mfq95sxvatwvss8qmrp09jhy2gxqzurcqp2nhyvtnkdatt6wzjux2928ygrsjhvmsxwtgdar4ec4qhf55cf0wzszxgemvqg0na9xapulrhhlpn3jrezum7nvdegy6j42l0f7pnkd2sqf3d9xq"},"address":"lnbc139670n1pwlu09epp5h8sd2jh7hyyjp4nzxmknfw5hmzynv74wp36wnwuf4c36vl84gqpsdzl2pskjepqw3hjqmtpw35xymm5ypehgmmjv5szsnmjv3jhygzfgsazqmtpw35xymm59e3k7mfq95sxvatwvss8qmrp09jhy2gxqzurcqp2nhyvtnkdatt6wzjux2928ygrsjhvmsxwtgdar4ec4qhf55cf0wzszxgemvqg0na9xapulrhhlpn3jrezum7nvdegy6j42l0f7pnkd2sqf3d9xq","url":"https://payments.mathbot.com/i/BTC_LightningLike/6Rh3rp3TRnbia3zYho9buK","totalDue":"0.00013967","networkFee":"0.00000000","txCount":0,"cryptoPaid":"0.00000000","payments":[]}],"price":1,"currency":"USD","exRates":null,"buyerTotalBtcAmount":null,"itemDesc":"mathbot.com - fund player","itemCode":null,"orderId":"mathbot.com - fund player","guid":"d9c6967e-642c-4364-b46c-9e798583a97f","id":"6Rh3rp3TRnbia3zYho9buK","invoiceTime":1576942777000,"expirationTime":1576943677000,"currentTime":1576942777492,"lowFeeDetected":false,"btcPaid":null,"rate":0,"exceptionStatus":false,"paymentUrls":null,"refundAddressRequestPending":false,"buyerPaidBtcMinerFee":null,"bitcoinAddress":null,"token":"STmedpHxaT9bqzvFewweg8","flags":{"refundable":false},"paymentSubtotals":{"BTC_LightningLike":13967},"paymentTotals":{"BTC_LightningLike":13967},"amountPaid":0,"minerFees":{},"exchangeRates":{"BTC":{"USD":0}},"supportedTransactionCurrencies":{"BTC":{"enabled":true}},"addresses":{"BTC_LightningLike":"lnbc139670n1pwlu09epp5h8sd2jh7hyyjp4nzxmknfw5hmzynv74wp36wnwuf4c36vl84gqpsdzl2pskjepqw3hjqmtpw35xymm5ypehgmmjv5szsnmjv3jhygzfgsazqmtpw35xymm59e3k7mfq95sxvatwvss8qmrp09jhy2gxqzurcqp2nhyvtnkdatt6wzjux2928ygrsjhvmsxwtgdar4ec4qhf55cf0wzszxgemvqg0na9xapulrhhlpn3jrezum7nvdegy6j42l0f7pnkd2sqf3d9xq"},"paymentCodes":{"BTC_LightningLike":{"BIP21":null,"BIP72":null,"BIP72b":null,"BIP73":null,"BOLT11":"lightning:lnbc139670n1pwlu09epp5h8sd2jh7hyyjp4nzxmknfw5hmzynv74wp36wnwuf4c36vl84gqpsdzl2pskjepqw3hjqmtpw35xymm5ypehgmmjv5szsnmjv3jhygzfgsazqmtpw35xymm59e3k7mfq95sxvatwvss8qmrp09jhy2gxqzurcqp2nhyvtnkdatt6wzjux2928ygrsjhvmsxwtgdar4ec4qhf55cf0wzszxgemvqg0na9xapulrhhlpn3jrezum7nvdegy6j42l0f7pnkd2sqf3d9xq"}},"buyer":{"name":"test","address1":null,"address2":null,"locality":null,"region":null,"postalCode":null,"country":null,"phone":null,"email":"test@gmail.com"}}}
      """.stripMargin

    assertThrows[JsResultException] {
      Json.parse(json).as[ChargeInfoResponse]
    }

  }

  test("json parsing") {

    val json =
      """
        |{"facade":"pos/invoice","data":{"url":"https://payments.mathbot.com/invoice?id=F2vU2rFGpBv3ZY1R1qrVCp","posData":null,"status":"new","btcPrice":"0.00014048","btcDue":"0.00014048","cryptoInfo":[{"cryptoCode":"BTC","paymentType":"BTCLike","rate":7118.82364667061,"exRates":{"USD":0.0},"paid":"0.00000000","price":"0.00014048","due":"0.00014048","paymentUrls":{"BIP21":"bitcoin:tb1q3td5v9knh4unsnvs04c07r4035jplecqev59xc?amount=0.00014048","BIP72":null,"BIP72b":null,"BIP73":null,"BOLT11":null},"address":"tb1q3td5v9knh4unsnvs04c07r4035jplecqev59xc","url":"https://payments.mathbot.com/i/BTC/F2vU2rFGpBv3ZY1R1qrVCp","totalDue":"0.00014048","networkFee":"0.00000000","txCount":0,"cryptoPaid":"0.00000000","payments":[]},{"cryptoCode":"BTC","paymentType":"LightningLike","rate":7118.82364667061,"exRates":{"USD":0.0},"paid":"0.00000000","price":"0.00014048","due":"0.00014048","paymentUrls":{"BIP21":null,"BIP72":null,"BIP72b":null,"BIP73":null,"BOLT11":"lightning:lntb140480n1pwlvcyfpp5cp3ue8c9lrmgd7vc8t6rq6tew8a6rdv644jmgmzc4xlkmy5wtphqdzl2pskjepqw3hjqmtpw35xymm5ypehgmmjv5szsnmjv3jhygzfgsazqmtpw35xymm59e3k7mfq95sxvatwvss8qmrp09jhy2gxqzurcqp2rzjqwyx8nu2hygyvgc02cwdtvuxe0lcxz06qt3lpsldzcdr46my5epmjxye4gqqqtgqqqqqqqlgqqqqqqgq9qsfplnpznjl08rkqc938ex3nj0zjt28yre0wmwkwhmhjzz7505razf28avunnl0ktnv7l3x6dkk3w6t0talthjq8tkvynf07ul2w8aggqvuedgv"},"address":"lntb140480n1pwlvcyfpp5cp3ue8c9lrmgd7vc8t6rq6tew8a6rdv644jmgmzc4xlkmy5wtphqdzl2pskjepqw3hjqmtpw35xymm5ypehgmmjv5szsnmjv3jhygzfgsazqmtpw35xymm59e3k7mfq95sxvatwvss8qmrp09jhy2gxqzurcqp2rzjqwyx8nu2hygyvgc02cwdtvuxe0lcxz06qt3lpsldzcdr46my5epmjxye4gqqqtgqqqqqqqlgqqqqqqgq9qsfplnpznjl08rkqc938ex3nj0zjt28yre0wmwkwhmhjzz7505razf28avunnl0ktnv7l3x6dkk3w6t0talthjq8tkvynf07ul2w8aggqvuedgv","url":"https://payments.mathbot.com/i/BTC_LightningLike/F2vU2rFGpBv3ZY1R1qrVCp","totalDue":"0.00014048","networkFee":"0.00000000","txCount":0,"cryptoPaid":"0.00000000","payments":[]}],"price":1.0,"currency":"USD","exRates":{"USD":0.0},"buyerTotalBtcAmount":null,"itemDesc":"mathbot.com - fund player","itemCode":null,"orderId":"mathbot.com - fund player","guid":"cbaf9ddb-03da-427a-b09f-0c63cc583418","id":"F2vU2rFGpBv3ZY1R1qrVCp","invoiceTime":1576427657000,"expirationTime":1576428557000,"currentTime":1576427657575,"lowFeeDetected":false,"btcPaid":"0.00000000","rate":7118.82364667061,"exceptionStatus":false,"paymentUrls":{"BIP21":"bitcoin:tb1q3td5v9knh4unsnvs04c07r4035jplecqev59xc?amount=0.00014048","BIP72":null,"BIP72b":null,"BIP73":null,"BOLT11":null},"refundAddressRequestPending":false,"buyerPaidBtcMinerFee":null,"bitcoinAddress":"tb1q3td5v9knh4unsnvs04c07r4035jplecqev59xc","token":"51uRuhyLXTV236HayJeuvX","flags":{"refundable":false},"paymentSubtotals":{"BTC":14048,"BTC_LightningLike":14048},"paymentTotals":{"BTC":14048,"BTC_LightningLike":14048},"amountPaid":0,"minerFees":{"BTC":{"satoshisPerByte":1.0,"totalFee":0}},"exchangeRates":{"BTC":{"USD":0.0}},"supportedTransactionCurrencies":{"BTC":{"enabled":true}},"addresses":{"BTC":"tb1q3td5v9knh4unsnvs04c07r4035jplecqev59xc","BTC_LightningLike":"lntb140480n1pwlvcyfpp5cp3ue8c9lrmgd7vc8t6rq6tew8a6rdv644jmgmzc4xlkmy5wtphqdzl2pskjepqw3hjqmtpw35xymm5ypehgmmjv5szsnmjv3jhygzfgsazqmtpw35xymm59e3k7mfq95sxvatwvss8qmrp09jhy2gxqzurcqp2rzjqwyx8nu2hygyvgc02cwdtvuxe0lcxz06qt3lpsldzcdr46my5epmjxye4gqqqtgqqqqqqqlgqqqqqqgq9qsfplnpznjl08rkqc938ex3nj0zjt28yre0wmwkwhmhjzz7505razf28avunnl0ktnv7l3x6dkk3w6t0talthjq8tkvynf07ul2w8aggqvuedgv"},"paymentCodes":{"BTC":{"BIP21":"bitcoin:tb1q3td5v9knh4unsnvs04c07r4035jplecqev59xc?amount=0.00014048","BIP72":null,"BIP72b":null,"BIP73":null,"BOLT11":null},"BTC_LightningLike":{"BIP21":null,"BIP72":null,"BIP72b":null,"BIP73":null,"BOLT11":"lightning:lntb140480n1pwlvcyfpp5cp3ue8c9lrmgd7vc8t6rq6tew8a6rdv644jmgmzc4xlkmy5wtphqdzl2pskjepqw3hjqmtpw35xymm5ypehgmmjv5szsnmjv3jhygzfgsazqmtpw35xymm59e3k7mfq95sxvatwvss8qmrp09jhy2gxqzurcqp2rzjqwyx8nu2hygyvgc02cwdtvuxe0lcxz06qt3lpsldzcdr46my5epmjxye4gqqqtgqqqqqqqlgqqqqqqgq9qsfplnpznjl08rkqc938ex3nj0zjt28yre0wmwkwhmhjzz7505razf28avunnl0ktnv7l3x6dkk3w6t0talthjq8tkvynf07ul2w8aggqvuedgv"}},"buyer":{"name":"test","address1":null,"address2":null,"locality":null,"region":null,"postalCode":null,"country":null,"phone":null,"email":"test@gmail.com"}}}
      """.stripMargin

    val result = Json.parse(json).validate[ChargeInfoResponse]

    assert(result.isSuccess)
  }

  test("newCharge") {
    Json.parse(newCharge).validate[ChargeInfoResponse].isSuccess shouldBe true
  }
  test("expiredNoPayments") {

    Json.parse(newCharge).validate[ChargeInfoResponse].isSuccess shouldBe true
  }
  test("completeLN") {

    Json.parse(completeLN).validate[ChargeInfoResponse].isSuccess shouldBe true
  }
  test("completeBTC") {

    Json.parse(completeBTC).validate[ChargeInfoResponse].isSuccess shouldBe true
  }
  test("paidBTC") {

    Json.parse(paidBTC).validate[ChargeInfoResponse].isSuccess shouldBe true
  }
  test("paidPartialNotConfirmed") {

    Json.parse(paidPartialNotConfirmed).validate[ChargeInfoResponse].isSuccess shouldBe true
  }
  test("paidPartialConfirmed") {

    Json.parse(paidPartialConfirmed).validate[ChargeInfoResponse].isSuccess shouldBe true
  }
  test("paidLateNotConfirmed") {

    Json.parse(paidLateNotConfirmed).validate[ChargeInfoResponse].isSuccess shouldBe true
  }
  test("paidLateConfirmed") {

    Json.parse(paidLateConfirmed).validate[ChargeInfoResponse].isSuccess shouldBe true
  }
  test("invalid") {

    Json.parse(invalid).validate[ChargeInfoResponse].isSuccess shouldBe true
  }
}
