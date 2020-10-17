package com.mathbot.pay.lightning

import com.mathbot.pay.bitcoin.AddressType.AddressType

case class NewAddressRequest(addresstype: AddressType) extends LightningJson
