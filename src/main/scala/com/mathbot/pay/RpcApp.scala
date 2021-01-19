//package com.mathbot.pay
//
//import com.mathbot.pay.bitcoin.{AddressType, BitcoinJsonRpcClient, BitcoinJsonRpcConfig, BtcAddress, Satoshi}
//import com.mathbot.pay.json.PlayJsonSupport
//import com.softwaremill.macwire.wire
//import org.slf4j.LoggerFactory
//import sttp.client.akkahttp.AkkaHttpBackend
//
//import scala.concurrent.Await
//import scala.concurrent.duration.Duration
//
//
//
//object RpcApp extends App with PlayJsonSupport {
//
//  import scala.concurrent.ExecutionContext.Implicits.global
//
//  val user="raspibolt"
//  val host ="http://raspibolt.local:8332"
//
//  val password = "M!tG4MVPW4x&"
//
//  val backend = AkkaHttpBackend()
//  val logger = LoggerFactory.getLogger(this.getClass)
//  val config = BitcoinJsonRpcConfig(baseUrl = host, username = user, password = password)
//  val client: BitcoinJsonRpcClient = wire[BitcoinJsonRpcClient]
//
//  val xp1= "xpub661MyMwAqRbcFtXgS5sYJABqqG9YLmC4Q1Rdap9gSE8NqtwybGhePY2gZ29ESFjqJoCu1Rupje8YtGqsefD265TMg7usUDFdp6W1EGMcet8"
//  val xp2 = "xpub68Gmy5EdvgibQVfPdqkBBCHxA5htiqg55crXYuXoQRKfDBFA1WEjWgP6LHhwBZeNK1VTsfTFUHCdrfp1bgwQ9xv5ski8PX9rL2dZXvgGDnw"
////  val f = client.createmultisig(2, xp1 :: xp2 :: Nil)
//  val d = s"""sh(multi(2,$xp1/*,$xp2/*))"""
//  val f = for {
//    Right(descriptor) <- client.getdescriptorinfo(d)
//    a <- client.deriveaddresses(descriptor.descriptor, 0, 100)
//    b <- client.importmulti(descriptor.descriptor, 100, 200, true, None)
//  } yield {
//b
//  }
//
//
//  val r  = Await.result(f, Duration("30s"))
//
//
//  println(r.merge.toString)
//  sys.exit()
//}
