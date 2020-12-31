package testdriven

import akka.actor._
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.WordSpecLike

class EchoActorTest
    extends TestKit(ActorSystem("testSystem"))
    with WordSpecLike
    with ImplicitSender
    with StopSystemAfterAll {
  "A Echo Actor" must {
    // 何もせずに受信したのと同じメッセージを返す
    "Reply with the same message it receives without ask" in {
      val echo = system.actorOf(Props[EchoActor], "echo2")
      echo ! "some message"
      expectMsg("some message")
    }
  }
}
