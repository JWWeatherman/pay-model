package com.mathbot.pay.bitcoin

object BitcoinRPCErrorCode {
  final val RPC_MISC_ERROR: Int = -1 //!< std::exception thrown in command handling

  final val RPC_FORBIDDEN_BY_SAFE_MODE: Int = -2 //!< Server is in safe mode, and command is not allowed in safe mode

  final val RPC_TYPE_ERROR: Int = -3 //!< Unexpected type was passed as parameter

  final val RPC_INVALID_ADDRESS_OR_KEY: Int = -5 //!< Invalid address or key

  final val RPC_OUT_OF_MEMORY: Int = -7 //!< Ran out of memory during operation

  final val RPC_INVALID_PARAMETER: Int = -8 //!< Invalid, missing or duplicate parameter

  final val RPC_DATABASE_ERROR: Int = -20 //!< Database error

  final val RPC_DESERIALIZATION_ERROR: Int = -22 //!< Error parsing or validating structure in raw format

  final val RPC_VERIFY_ERROR: Int = -25 //!< General error during transaction or block submission

  final val RPC_VERIFY_REJECTED: Int = -26 //!< Transaction or block was rejected by network rules

  final val RPC_VERIFY_ALREADY_IN_CHAIN: Int = -27 //!< Transaction already in chain

  final val RPC_IN_WARMUP: Int = -28 //!< Client still warming up

}
