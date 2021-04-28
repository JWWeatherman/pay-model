package com.mathbot.pay.lightningcharge

import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

class LightningChargeInvoiceTest extends AnyWordSpec {
  "" should {
    "j" in {
      val s =
        """
          |{"id":"K6j0fJW8eJnaXvowjtdv1","msatoshi":"1822302","description":"test currency","quoted_currency":"usd","quoted_amount":1,"rhash":"ebb3fa6a14b049794a4df29092db97122212013d26ed83cac6a019af1e626fd1","payreq":"lnbc18223020p1psgnkuypp5awel56s5kpyhjjjd72gf9kuhzg3pyqfaymkc8jkx5qv678nzdlgsdq4w3jhxapqvd6hyun9de3hjxqzjccqpjsp5hjg059gpptujk8hv0fvcu8qh823lk4d496f0pv6jl8h0l00hsgfsrzjq0fhljsx2e2cme8as6a7fy9r3kz2gc3gulkpxcvqra20jsm6rrtpszxplvqq5wqqqyqqqqqpqqqqqjcqjq9qy9qsqszv86kac7eyduzu6qqx57pdc8pc68ugt80g5y4zsvvlvr44nre44sp3jcz9z3vfqdpwqfkg5rflcyqa5j324f8plsneqetuxsnh7pkgpghk8er","expires_at":1619647964,"created_at":1619647364,"metadata":null,"status":"unpaid"}""".stripMargin

      val j = Json.parse(s)

      val r = j.validate[LightningChargeInvoice]
      assert(r.isSuccess)
    }
  }
}
