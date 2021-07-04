package com.mathbot.pay.lightning

import com.mathbot.pay.bitcoin.MilliSatoshi
import com.mathbot.pay.lightning.LightningPaymentRequest._
import fr.acinq.bitcoin.Crypto.{PrivateKey, PublicKey}
import fr.acinq.bitcoin.{Bech32, ByteVector32, ByteVector64, Crypto}
import scodec.bits.{BitVector, ByteOrdering, ByteVector}
import scodec.codecs.{
  bits,
  bytes,
  bytesStrict,
  choice,
  discriminated,
  int64,
  limitedSizeBytes,
  list,
  listOfN,
  paddedVarAlignedBits,
  ubyte,
  uint,
  uint16,
  uint32,
  ulong,
  utf8,
  _
}
import scodec.{Attempt, Codec, DecodeResult, Err}

import scala.concurrent.duration.{FiniteDuration, MILLISECONDS}

/**
 * Lightning Payment Request
 * see https://github.com/lightningnetwork/lightning-rfc/blob/master/11-payment-encoding.md
 *
 * @param prefix    currency prefix; lnbc for bitcoin, lntb for bitcoin testnet
 * @param amount    amount to pay (empty string means no amount is specified)
 * @param timestamp request timestamp (UNIX format)
 * @param nodeId    id of the node emitting the payment request ,
 * @param tags      payment tags; must include a single PaymentHash tag and a single PaymentSecret tag.
 * @param signature request signature that will be checked against node id
 */
case class LightningPaymentRequest(
    prefix: String,
    amount: Option[MilliSatoshi],
    timestamp: Long,
    nodeId: PublicKey,
    tags: List[TaggedField],
    signature: ByteVector
) {

  /**
   * @return the payment hash
   */
  lazy val paymentHash = tags.collectFirst { case p: PaymentHash => p.hash }.get

  /**
   * @return the payment secret
   */
  lazy val paymentSecret = tags.collectFirst { case p: PaymentSecret => p.secret }

  /**
   * @return the description of the payment, or its hash
   */
  lazy val description: Either[String, ByteVector32] = tags.collectFirst {
    case Description(d) => Left(d)
    case DescriptionHash(h) => Right(h)
  }.get

//  /**
//   * @return the fallback address if any. It could be a script address, pubkey address, ..
//   */
//  def fallbackAddress(): Option[String] = tags.collectFirst {
//    case f: FallbackAddress => FallbackAddress.toAddress(f, prefix)
//  }

  lazy val routingInfo: Seq[Seq[ExtraHop]] = tags.collect { case t: RoutingInfo => t.path }

  lazy val expiry: Option[Long] = tags.collectFirst {
    case expiry: Expiry => expiry.toLong
  }

  lazy val minFinalCltvExpiryDelta: Option[CltvExpiryDelta] = tags.collectFirst {
    case cltvExpiry: MinFinalCltvExpiry => cltvExpiry.toCltvExpiryDelta
  }

  lazy val features: PaymentRequestFeatures =
    tags.collectFirst { case f: PaymentRequestFeatures => f }.getOrElse(PaymentRequestFeatures(BitVector.empty))

  def isExpired: Boolean =
    expiry match {
      case Some(expiryTime) =>
        timestamp + expiryTime <= FiniteDuration(System.currentTimeMillis, MILLISECONDS).toSeconds
      case None =>
        timestamp + DEFAULT_EXPIRY_SECONDS <= FiniteDuration(System.currentTimeMillis, MILLISECONDS).toSeconds
    }

  /**
   * @return the hash of this payment request
   */
  def hash: ByteVector32 = {
    val hrp = s"$prefix${encode(amount)}".getBytes("UTF-8")
    val data = Bolt11Data(timestamp, tags, ByteVector.fill(65)(0)) // fake sig that we are going to strip next
    val bin = bolt11DataCodec.encode(data).require
    val message = ByteVector.view(hrp) ++ bin.dropRight(520).toByteVector
    Crypto.sha256(message)
  }

  /**
   * @param priv private key
   * @return a signed payment request
   */
  def sign(priv: PrivateKey): LightningPaymentRequest = {
    val sig64 = Crypto.sign(hash, priv)
    // in order to tell what the recovery id is, we actually recover the pubkey ourselves and compare it to the real one
    val pub0 = Crypto.recoverPublicKey(sig64, hash, 0.toByte)
    val recid = if (nodeId == pub0) 0.toByte else 1.toByte
    val signature = sig64 :+ recid
    this.copy(signature = signature)
  }
}

/**
 * Features supported or required for receiving this payment.
 */
case class PaymentRequestFeatures(bitmask: BitVector) extends TaggedField {
  lazy val features: Features = Features(bitmask)
  lazy val allowMultiPart: Boolean = features.hasFeature(Features.BasicMultiPartPayment)
  lazy val allowPaymentSecret: Boolean = features.hasFeature(Features.PaymentSecret)
  lazy val requirePaymentSecret: Boolean = features.hasFeature(Features.PaymentSecret, Some(FeatureSupport.Mandatory))
  lazy val allowTrampoline: Boolean = features.hasFeature(Features.TrampolinePayment)

  def toByteVector: ByteVector = features.toByteVector

//  def areSupported(nodeParams: NodeParams): Boolean =  false//nodeParams.features.areSupported(features)

  override def toString: String = s"Features(${bitmask.toBin})"
}

object LightningPaymentRequest {

  sealed trait TaggedField

  /**
   * Payment Hash
   *
   * @param hash payment hash
   */
  case class PaymentHash(hash: ByteVector32) extends TaggedField

  /**
   * Payment secret. This is currently random bytes used to protect against probing from the next-to-last node.
   *
   * @param secret payment secret
   */
  case class PaymentSecret(secret: ByteVector32) extends TaggedField

  /**
   * Description
   *
   * @param description a free-format string that will be included in the payment request
   */
  case class Description(description: String) extends TaggedField

  /**
   * Hash
   *
   * @param hash hash that will be included in the payment request, and can be checked against the hash of a
   *             long description, an invoice, ...
   */
  case class DescriptionHash(hash: ByteVector32) extends TaggedField

  /**
   * Fallback Payment that specifies a fallback payment address to be used if LN payment cannot be processed
   */
  case class FallbackAddress(version: Byte, data: ByteVector) extends TaggedField

  /**
   * Extra hop contained in RoutingInfoTag
   *
   * @param nodeId                    start of the channel
   * @param shortChannelId            channel id
   * @param feeBase                   node fixed fee
   * @param feeProportionalMillionths node proportional fee
   * @param cltvExpiryDelta           node cltv expiry delta
   */
  case class ExtraHop(
      nodeId: PublicKey,
      shortChannelId: ShortChannelId,
      feeBase: MilliSatoshi,
      feeProportionalMillionths: Long,
      cltvExpiryDelta: CltvExpiryDelta
  )

  /**
   * Routing Info
   *
   * @param path one or more entries containing extra routing information for a private route
   */
  case class RoutingInfo(path: List[ExtraHop]) extends TaggedField

  /**
   * Expiry Date
   */
  case class Expiry(bin: BitVector) extends TaggedField {
    def toLong: Long = bin.toLong(signed = false)
  }

  object Expiry {

    /**
     * @param seconds expiry data for this payment request
     */
    def apply(seconds: Long): Expiry = Expiry(long2bits(seconds))
  }

  /**
   * Min final CLTV expiry
   */
  case class MinFinalCltvExpiry(bin: BitVector) extends TaggedField {
    def toCltvExpiryDelta = CltvExpiryDelta(bin.toInt(signed = false))
  }

  object MinFinalCltvExpiry {

    /**
     * Min final CLTV expiry
     *
     * @param blocks min final cltv expiry, in blocks
     */
    def apply(blocks: Long): MinFinalCltvExpiry = MinFinalCltvExpiry(long2bits(blocks))
  }

  def fixedSizeTrailingCodec[A](codec: Codec[A], size: Int): Codec[A] =
    Codec[A](
      (data: A) => codec.encode(data),
      (wire: BitVector) => {
        val (head, tail) = wire.splitAt(wire.size - size)
        codec.decode(head).map(result => result.copy(remainder = tail))
      }
    )

  val dataLengthCodec: Codec[Long] = uint(10).xmap(_ * 5, s => (s / 5 + (if (s % 5 == 0) 0 else 1)).toInt)

  def dataCodec[A](valueCodec: Codec[A], expectedLength: Option[Long] = None): Codec[A] =
    paddedVarAlignedBits(
      dataLengthCodec.narrow(
        l =>
          if (expectedLength.getOrElse(l) == l) Attempt.successful(l)
          else Attempt.failure(Err(s"invalid length $l")),
        l => l
      ),
      valueCodec,
      multipleForPadding = 5
    )

  case class Bolt11Data(timestamp: Long, taggedFields: List[TaggedField], signature: ByteVector)

  val shortchannelid: Codec[ShortChannelId] = int64.xmap(l => ShortChannelId(l), s => s.toLong)

  val privateKey: Codec[PrivateKey] = Codec[PrivateKey](
    (priv: PrivateKey) => bytes(32).encode(priv.value),
    (wire: BitVector) => bytes(32).decode(wire).map(_.map(b => PrivateKey(b)))
  )

  val publicKey: Codec[PublicKey] = Codec[PublicKey](
    (pub: PublicKey) => bytes(33).encode(pub.value),
    (wire: BitVector) => bytes(33).decode(wire).map(_.map(b => PublicKey(b)))
  )

  val cltvExpiry: Codec[CltvExpiry] = uint32.xmapc(CltvExpiry)((_: CltvExpiry).toLong)
  val cltvExpiryDelta: Codec[CltvExpiryDelta] = uint16.xmapc(CltvExpiryDelta)((_: CltvExpiryDelta).toInt)

  // this is needed because some millisatoshi values are encoded on 32 bits in the BOLTs
  // this codec will fail if the amount does not fit on 32 bits
  val millisatoshi32: Codec[MilliSatoshi] = uint32.xmapc(l => MilliSatoshi(l))(_.toLong)
  val extraHopCodec: Codec[ExtraHop] = (
    ("nodeId" | publicKey) ::
    ("shortChannelId" | shortchannelid) ::
    ("fee_base_msat" | millisatoshi32) ::
    ("fee_proportional_millionth" | uint32) ::
    ("cltv_expiry_delta" | cltvExpiryDelta)
  ).as[ExtraHop]

  /**
   * This returns a bitvector with the minimum size necessary to encode the long, left padded
   * to have a length (in bits) multiples of 5
   */
  def long2bits(l: Long) = {
    val bin = BitVector.fromLong(l)
    var highest = -1
    for (i <- 0 until bin.size.toInt) {
      if (highest == -1 && bin(i)) highest = i
    }
    val nonPadded = if (highest == -1) BitVector.empty else bin.drop(highest)
    nonPadded.size % 5 match {
      case 0 => nonPadded
      case remaining => BitVector.fill(5 - remaining)(high = false) ++ nonPadded
    }
  }

  val extraHopsLengthCodec = Codec[Int](
    (_: Int) => Attempt.successful(BitVector.empty), // we don't encode the length
    (wire: BitVector) =>
      Attempt
        .successful(DecodeResult(wire.size.toInt / 408, wire)) // we infer the number of items by the size of the data
  )
  val bytes32: Codec[ByteVector32] = limitedSizeBytes(32, bytesStrict(32).xmap(d => ByteVector32(d), d => d.bytes))

  def alignedBytesCodec[A](valueCodec: Codec[A]): Codec[A] =
    Codec[A](
      (value: A) => valueCodec.encode(value),
      (wire: BitVector) =>
        (limitedSizeBits(wire.size - wire.size % 8, valueCodec) ~ constant(BitVector.fill(wire.size % 8)(high = false)))
          .map(_._1)
          .decode(wire) // the 'constant' codec ensures that padding is zero
    )

  val taggedFieldCodec: Codec[TaggedField] = discriminated[TaggedField]
    .by(ubyte(5))
    .typecase(0, dataCodec(bits).as[UnknownTag0])
    .\(1) {
      case a: PaymentHash => a: TaggedField
      case a: InvalidTag1 => a: TaggedField
    }(
      choice(
        dataCodec(bytes32, expectedLength = Some(52 * 5)).as[PaymentHash].upcast[TaggedField],
        dataCodec(bits).as[InvalidTag1].upcast[TaggedField]
      )
    )
    .typecase(2, dataCodec(bits).as[UnknownTag2])
    .typecase(3, dataCodec(listOfN(extraHopsLengthCodec, extraHopCodec)).as[RoutingInfo])
    .typecase(4, dataCodec(bits).as[UnknownTag4])
    .typecase(5, dataCodec(bits).as[PaymentRequestFeatures])
    .typecase(6, dataCodec(bits).as[Expiry])
    .typecase(7, dataCodec(bits).as[UnknownTag7])
    .typecase(8, dataCodec(bits).as[UnknownTag8])
    .typecase(9, dataCodec(ubyte(5) :: alignedBytesCodec(bytes)).as[FallbackAddress])
    .typecase(10, dataCodec(bits).as[UnknownTag10])
    .typecase(11, dataCodec(bits).as[UnknownTag11])
    .typecase(12, dataCodec(bits).as[UnknownTag12])
    .typecase(13, dataCodec(alignedBytesCodec(utf8)).as[Description])
    .typecase(14, dataCodec(bits).as[UnknownTag14])
    .typecase(15, dataCodec(bits).as[UnknownTag15])
    .\(16) {
      case a: PaymentSecret => a: TaggedField
      case a: InvalidTag16 => a: TaggedField
    }(
      choice(
        dataCodec(bytes32, expectedLength = Some(52 * 5)).as[PaymentSecret].upcast[TaggedField],
        dataCodec(bits).as[InvalidTag16].upcast[TaggedField]
      )
    )
    .typecase(17, dataCodec(bits).as[UnknownTag17])
    .typecase(18, dataCodec(bits).as[UnknownTag18])
    .typecase(19, dataCodec(bits).as[UnknownTag19])
    .typecase(20, dataCodec(bits).as[UnknownTag20])
    .typecase(21, dataCodec(bits).as[UnknownTag21])
    .typecase(22, dataCodec(bits).as[UnknownTag22])
    .\(23) {
      case a: DescriptionHash => a: TaggedField
      case a: InvalidTag23 => a: TaggedField
    }(
      choice(
        dataCodec(bytes32, expectedLength = Some(52 * 5)).as[DescriptionHash].upcast[TaggedField],
        dataCodec(bits).as[InvalidTag23].upcast[TaggedField]
      )
    )
    .typecase(24, dataCodec(bits).as[MinFinalCltvExpiry])
    .typecase(25, dataCodec(bits).as[UnknownTag25])
    .typecase(26, dataCodec(bits).as[UnknownTag26])
    .typecase(27, dataCodec(bits).as[UnknownTag27])
    .typecase(28, dataCodec(bits).as[UnknownTag28])
    .typecase(29, dataCodec(bits).as[UnknownTag29])
    .typecase(30, dataCodec(bits).as[UnknownTag30])
    .typecase(31, dataCodec(bits).as[UnknownTag31])

  val bolt11DataCodec: Codec[Bolt11Data] = (
    ("timestamp" | ulong(35)) ::
    ("taggedFields" | fixedSizeTrailingCodec(list(taggedFieldCodec), 520)) ::
    ("signature" | bytes(65))
  ).as[Bolt11Data]
  val DEFAULT_EXPIRY_SECONDS = 3600
  val prefix: String = "lnbc"
  def parseAmount(bolt11: String): MilliSatoshi = {
    val idx = bolt11.lastIndexOf("1")
    val regex = s"lnbc"
    val raw = regex.replace(regex, "")
    raw.toLowerCase match {
      case empty if raw.isEmpty => MilliSatoshi(0)
      case a if a.last == 'p' => MilliSatoshi(a.dropRight(1).toLong / 10L) // 1 pico-bitcoin == 10 milli-satoshi
      case a if a.last == 'n' => MilliSatoshi(a.dropRight(1).toLong * 100L)
      case a if a.last == 'u' => MilliSatoshi(a.dropRight(1).toLong * 100000L)
      case a if a.last == 'm' => MilliSatoshi(a.dropRight(1).toLong * 100000000L)
      case a => MilliSatoshi(a.toLong * 100000000000L)
    }
  }
  def trimPrefix(bolt11: String) = if (bolt11.startsWith("lightning:")) bolt11.replace("lightning:", "") else bolt11

  // char -> 5 bits value
  val charToint5: Map[Char, BitVector] = Bech32.alphabet.zipWithIndex.toMap.view
    .mapValues(BitVector.fromInt(_, size = 5, ordering = ByteOrdering.BigEndian))
    .toMap

  // TODO: could be optimized by preallocating the resulting buffer
  def string2Bits(data: String): BitVector = data.map(charToint5).foldLeft(BitVector.empty)(_ ++ _)
  def read(input: String) = {
    // used only for data validation
    Bech32.decode(input)
    val lowercaseInput = input.toLowerCase
    val separatorIndex = lowercaseInput.lastIndexOf('1')
    val hrp = lowercaseInput.take(separatorIndex)
    val data = string2Bits(lowercaseInput.slice(separatorIndex + 1, lowercaseInput.length - 6)) // 6 == checksum size
    val bolt11Data = bolt11DataCodec.decode(data).require.value
    val signature = ByteVector64(bolt11Data.signature.take(64))
    val message: ByteVector = ByteVector.view(hrp.getBytes) ++ data.dropRight(520).toByteVector // we drop the sig bytes
    val recid = bolt11Data.signature.last
    val pub = Crypto.recoverPublicKey(signature, Crypto.sha256(message), recid)
    // README: since we use pubkey recovery to compute the node id from the message and signature, we don't check the signature.
    // If instead we read the node id from the `n` field (which nobody uses afaict) then we would have to check the signature.
    val amount_opt = parseAmount(hrp.drop(prefix.length))
    LightningPaymentRequest(
      prefix = prefix,
      amount = Some(amount_opt),
      timestamp = bolt11Data.timestamp,
      nodeId = pub,
      tags = bolt11Data.taggedFields,
      signature = bolt11Data.signature
    )
  }

  def encode(amount: Option[MilliSatoshi]): String = {
    amount match {
      case None => ""
      case Some(amt) if unit(amt) == 'p' => s"${amt.toLong * 10L}p" // 1 pico-bitcoin == 0.1 milli-satoshis
      case Some(amt) if unit(amt) == 'n' => s"${amt.toLong / 100L}n"
      case Some(amt) if unit(amt) == 'u' => s"${amt.toLong / 100000L}u"
      case Some(amt) if unit(amt) == 'm' => s"${amt.toLong / 100000000L}m"
    }
  }

  /**
   * @return the unit allowing for the shortest representation possible
   */
  def unit(amount: MilliSatoshi): Char =
    amount.toLong * 10 match { // 1 milli-satoshis == 10 pico-bitcoin
      case pico if pico % 1000 > 0 => 'p'
      case pico if pico % 1000000 > 0 => 'n'
      case pico if pico % 1000000000 > 0 => 'u'
      case _ => 'm'
    }

  val eight2fiveCodec: Codec[List[Byte]] = list(ubyte(5))

  /**
   * @param pr payment request
   * @return a bech32-encoded payment request
   */
  def write(pr: LightningPaymentRequest): String = {
    // currency unit is Satoshi, but we compute amounts in Millisatoshis
    val hramount = encode(pr.amount)
    val hrp = s"${pr.prefix}$hramount"
    val data = bolt11DataCodec.encode(Bolt11Data(pr.timestamp, pr.tags, pr.signature)).require
    val int5s = eight2fiveCodec.decode(data).require.value
    Bech32.encode(hrp, int5s.toArray)
  }

  sealed trait UnknownTaggedField extends TaggedField

  sealed trait InvalidTaggedField extends TaggedField

  // @formatter:off
  case class UnknownTag0(data: BitVector) extends UnknownTaggedField
  case class InvalidTag1(data: BitVector) extends InvalidTaggedField
  case class UnknownTag2(data: BitVector) extends UnknownTaggedField
  case class UnknownTag4(data: BitVector) extends UnknownTaggedField
  case class UnknownTag7(data: BitVector) extends UnknownTaggedField
  case class UnknownTag8(data: BitVector) extends UnknownTaggedField
  case class UnknownTag10(data: BitVector) extends UnknownTaggedField
  case class UnknownTag11(data: BitVector) extends UnknownTaggedField
  case class UnknownTag12(data: BitVector) extends UnknownTaggedField
  case class UnknownTag14(data: BitVector) extends UnknownTaggedField
  case class UnknownTag15(data: BitVector) extends UnknownTaggedField
  case class InvalidTag16(data: BitVector) extends InvalidTaggedField
  case class UnknownTag17(data: BitVector) extends UnknownTaggedField
  case class UnknownTag18(data: BitVector) extends UnknownTaggedField
  case class UnknownTag19(data: BitVector) extends UnknownTaggedField
  case class UnknownTag20(data: BitVector) extends UnknownTaggedField
  case class UnknownTag21(data: BitVector) extends UnknownTaggedField
  case class UnknownTag22(data: BitVector) extends UnknownTaggedField
  case class InvalidTag23(data: BitVector) extends InvalidTaggedField
  case class UnknownTag25(data: BitVector) extends UnknownTaggedField
  case class UnknownTag26(data: BitVector) extends UnknownTaggedField
  case class UnknownTag27(data: BitVector) extends UnknownTaggedField
  case class UnknownTag28(data: BitVector) extends UnknownTaggedField
  case class UnknownTag29(data: BitVector) extends UnknownTaggedField
  case class UnknownTag30(data: BitVector) extends UnknownTaggedField
  case class UnknownTag31(data: BitVector) extends UnknownTaggedField
}


/**
 * Created by t-bast on 21/08/2019.
 */

/**
 * Bitcoin scripts (in particular HTLCs) need an absolute block expiry (greater than the current block count) to work
 * with OP_CLTV.
 *
 * @param underlying the absolute cltv expiry value (current block count + some delta).
 */
case class CltvExpiry(private val underlying: Long) extends Ordered[CltvExpiry] {
  // @formatter:off
  def +(d: CltvExpiryDelta): CltvExpiry = CltvExpiry(underlying + d.toInt)
  def -(d: CltvExpiryDelta): CltvExpiry = CltvExpiry(underlying - d.toInt)
  def -(other: CltvExpiry): CltvExpiryDelta = CltvExpiryDelta((underlying - other.underlying).toInt)
  override def compare(other: CltvExpiry): Int = underlying.compareTo(other.underlying)
  def toLong: Long = underlying
  // @formatter:on
}

/**
 * Channels advertise a cltv expiry delta that should be used when routing through them.
 * This value needs to be converted to a  to be used in OP_CLTV.
 *
 * CltvExpiryDelta can also be used when working with OP_CSV which is by design a delta.
 *
 * @param underlying the cltv expiry delta value.
 */
case class CltvExpiryDelta(private val underlying: Int) extends Ordered[CltvExpiryDelta] {

  /**
   * Adds the current block height to the given delta to obtain an absolute expiry.
   */
  def toCltvExpiry(blockHeight: Long) = CltvExpiry(blockHeight + underlying)

  // @formatter:off
  def +(other: Int): CltvExpiryDelta = CltvExpiryDelta(underlying + other)
  def +(other: CltvExpiryDelta): CltvExpiryDelta = CltvExpiryDelta(underlying + other.underlying)
  def -(other: CltvExpiryDelta): CltvExpiryDelta = CltvExpiryDelta(underlying - other.underlying)
  def *(m: Int): CltvExpiryDelta = CltvExpiryDelta(underlying * m)
  def compare(other: CltvExpiryDelta): Int = underlying.compareTo(other.underlying)
  def toInt: Int = underlying
  // @formatter:on

}

/**
 * A short channel id uniquely identifies a channel by the coordinates of its funding tx output in the blockchain.
 *
 * See BOLT 7: https://github.com/lightningnetwork/lightning-rfc/blob/master/07-routing-gossip.md#requirements
 */
case class ShortChannelId(private val id: Long) extends Ordered[ShortChannelId] {

  def toLong: Long = id

  def blockHeight = ShortChannelId.blockHeight(this)

  override def toString: String = {
    val TxCoordinates(blockHeight, txIndex, outputIndex) = ShortChannelId.coordinates(this)
    s"${blockHeight}x${txIndex}x${outputIndex}"
  }

  // we use an unsigned long comparison here
  override def compare(that: ShortChannelId): Int = (this.id + Long.MinValue).compareTo(that.id + Long.MinValue)
}

object ShortChannelId {

  def apply(s: String): ShortChannelId =
    s.split("x").toList match {
      case blockHeight :: txIndex :: outputIndex :: Nil =>
        ShortChannelId(toShortId(blockHeight.toInt, txIndex.toInt, outputIndex.toInt))
      case _ => throw new IllegalArgumentException(s"Invalid short channel id: $s")
    }

  def apply(blockHeight: Int, txIndex: Int, outputIndex: Int): ShortChannelId =
    ShortChannelId(toShortId(blockHeight, txIndex, outputIndex))

  def toShortId(blockHeight: Int, txIndex: Int, outputIndex: Int): Long =
    ((blockHeight & 0xffffffL) << 40) | ((txIndex & 0xffffffL) << 16) | (outputIndex & 0xffffL)

  @inline
  def blockHeight(shortChannelId: ShortChannelId) = ((shortChannelId.id >> 40) & 0xffffff).toInt

  @inline
  def txIndex(shortChannelId: ShortChannelId) = ((shortChannelId.id >> 16) & 0xffffff).toInt

  @inline
  def outputIndex(shortChannelId: ShortChannelId) = (shortChannelId.id & 0xffff).toInt

  def coordinates(shortChannelId: ShortChannelId): TxCoordinates =
    TxCoordinates(blockHeight(shortChannelId), txIndex(shortChannelId), outputIndex(shortChannelId))
}

case class TxCoordinates(blockHeight: Int, txIndex: Int, outputIndex: Int)
