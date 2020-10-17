package com.mathbot.pay.lightning

import com.mathbot.pay.bitcoin.{MilliSatoshi, Satoshi}

/**
 * https://github.com/ElementsProject/lightning/blob/master/doc/lightning-setchannelfee.7.md
 * Set routing fees for a channel/peer {id} (or 'all'). {base} is a value in millisatoshi
 * that is added as base fee to any routed payment. {ppm} is a value added proportionally
 * per-millionths to any routed payment volume in satoshi.
 *
 * @param id
 * @param base
 * @param ppm
 */
case class SetChannelFee(id: String = "all", base: Option[MilliSatoshi], ppm: Option[Satoshi])
