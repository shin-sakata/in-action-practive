package testdriven

import org.scalatest.{MustMatchers, WordSpecLike}
import akka.testkit.{TestActorRef, TestKit}
import akka.actor._

class SilentActorTest
    extends TestKit(ActorSystem("testsystem"))
    with WordSpecLike
    with MustMatchers
    with StopSystemAfterAll {
  "A Silent Actor" must {
    "change state when it receives a message, single threaded" in {
      import SilentActor._

      val silentActor = TestActorRef[SilentActor](props)

      silentActor ! SilentMessage("whisper")
      silentActor.underlyingActor.state must (contain("whisper"))
    }
    "change state when it receives a message, multi-threaded" in {
      import SilentActor._

      val silentActor = system.actorOf(props, "s3")
      silentActor ! SilentMessage("whisper1")
      silentActor ! SilentMessage("whisper2")

      silentActor ! GetState(testActor)
      expectMsg(Vector("whisper1", "whisper2"))
    }
  }
}
