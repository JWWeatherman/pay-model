package com.mathbot.pay

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
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
    with EitherValues {

  override def afterAll(): Unit = TestKit.shutdownActorSystem(system)
  implicit lazy val backend = AkkaHttpBackend.usingActorSystem(system)
  val logger = LoggerFactory.getLogger("Spec")

}
