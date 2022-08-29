package com.mathbot.pay

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.scalalogging.StrictLogging
import org.scalatest.wordspec.AsyncWordSpecLike
import org.scalatest.{BeforeAndAfterAll, EitherValues}
import org.scalatestplus.mockito.MockitoSugar
import org.slf4j.LoggerFactory
import sttp.capabilities
import sttp.capabilities.akka.AkkaStreams
import sttp.client3.SttpBackend
import sttp.client3.akkahttp.AkkaHttpBackend

import scala.concurrent.Future

abstract class BaseIntegrationTest
    extends TestKit(ActorSystem("test"))
    with AsyncWordSpecLike
    with ImplicitSender
    with MockitoSugar
    with BeforeAndAfterAll
    with EitherValues
    with StrictLogging {

  override def afterAll(): Unit = TestKit.shutdownActorSystem(system)
  implicit lazy val backend: SttpBackend[Future, AkkaStreams with capabilities.WebSockets] =
    AkkaHttpBackend.usingActorSystem(system)

}
