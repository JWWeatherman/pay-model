package com.mathbot.pay.ws

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AsyncWordSpecLike
import org.scalatestplus.mockito.MockitoSugar

abstract class BaseActorTest
    extends TestKit(ActorSystem("test"))
    with AsyncWordSpecLike
    with ImplicitSender
    with MockitoSugar
    with BeforeAndAfterAll {
  override def afterAll(): Unit = TestKit.shutdownActorSystem(system)

}
