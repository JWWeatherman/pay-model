package com.mathbot.pay.lightning

import com.mathbot.pay.utils.JsonParseTest

class LightningCreateInvoiceTest extends JsonParseTest[Seq[LightningCreateInvoice]]("createInvoice.json")
