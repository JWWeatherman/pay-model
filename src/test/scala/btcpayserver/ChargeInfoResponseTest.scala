package btcpayserver

import com.mathbot.pay.utils.JsonParseTest
import btcpayserver.ChargeInfoResponse

class ChargeInfoResponseTest extends JsonParseTest[ChargeInfoResponse]("btcpayserver/chargeResponse.json")
