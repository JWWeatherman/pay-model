package com.mathbot.pay.lightning

// Fund channels to an array of {destinations},
//        each entry of which is a dict of node {id}
//        and {amount} to fund, and optionally whether
//        to {announce} and how much {push_msat} to
//        give outright to the node.
//        You may optionally specify {feerate},
//        {minconf} depth, and the {utxos} set to use
//        for the single transaction that funds all
//        the channels.
// https://github.com/ElementsProject/lightning/blob/master/doc/lightning-multifundchannel.7.md
// todo
case class MultiFundChannel()
