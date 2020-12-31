package testdriven

import Greeter._
import org.scalatest.{MustMatchers, WordSpecLike}
import akka.testkit.{CallingThreadDispatcher, EventFilter, TestKit}
import akka.actor._
import com.typesafe.config.ConfigFactory
import GreeterTest.testSystem

class GreeterTest
    extends TestKit(testSystem)
    with WordSpecLike
    with StopSystemAfterAll {
  "The Greeter" must {
    // Greeting("World")を送ると`Hello World!`と出力
    """say Hello World! when a Greeting("World") is sent to it""" in {
//      val dispatcherId = CallingThreadDispatcher.Id
//      val props = Props[Greeter].withDispatcher(dispatcherId)
//      val greeter = system.actorOf(props)
//      EventFilter
//        .info(message = "Hello World!", occurrences = 1)
//        // ログに出力されたメッセージをインターセプト
//        .intercept({ greeter ! Greeting("World!") })
      val props = Greeter.props(Some(testActor))
      val greeter = system.actorOf(props, "greeter-1")
      greeter ! Greeting("World")
      expectMsg("Hello World!")
    }
    // 何か他のメッセージを送ると何が起こるか
    "say something else and see what happens" in {
      val props = Greeter.props(Some(testActor))
      val greeter = system.actorOf(props, "greeter-2")
      system.eventStream.subscribe(testActor, classOf[UnhandledMessage])
      greeter ! "World"
      expectMsg(UnhandledMessage("World", system.deadLetters, greeter))
    }
  }
}

object GreeterTest {
  val testSystem: ActorSystem = {
    val config = {
      ConfigFactory.parseString("""
         akka.loggers = [akka.testkit.TestEventListener]
      """)
    }
    ActorSystem("testSystem", config)
  }
}
