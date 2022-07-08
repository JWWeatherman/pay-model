package com.mathbot.pay

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.scalalogging.StrictLogging
import org.scalatest.wordspec.AsyncWordSpecLike
import org.scalatest.{BeforeAndAfterAll, EitherValues}
import org.scalatestplus.mockito.MockitoSugar
import org.slf4j.LoggerFactory
import sttp.client3.akkahttp.AkkaHttpBackend

abstract class BaseIntegrationTest
    extends TestKit(ActorSystem("test"))
    with AsyncWordSpecLike
    with ImplicitSender
    with MockitoSugar
    with BeforeAndAfterAll
    with EitherValues
    with StrictLogging {

  override def afterAll(): Unit = TestKit.shutdownActorSystem(system)
  implicit lazy val backend = AkkaHttpBackend.usingActorSystem(system)

}
