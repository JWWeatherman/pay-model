package com.mathbot.pay.bitcoin

import com.mathbot.pay.utils.JsonParseTest

class WalletTransactionTest extends JsonParseTest[Seq[WalletTransaction]]("testnetWalletTransactions.json")
