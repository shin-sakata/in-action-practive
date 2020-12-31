package testdriven

import org.scalatest.{MustMatchers, WordSpecLike}
import akka.testkit.TestKit
import akka.actor._

class SendingActorTest
    extends TestKit(ActorSystem("testsystem"))
    with WordSpecLike
    with MustMatchers
    with StopSystemAfterAll {
  "A Mutating Copy Actor" must {
    // 処理終了時に別アクターへメッセージを送信する
    "send a message to another actor when it has finished processing" in {
      import MutatingCopyActor._
      import scala.util.Random
      // 受信者となるアクターを渡してPropsを作成する。
      // このテストではtestActorを渡す
      val props = MutatingCopyActor.props(this.testActor)
      val sendingActor = system.actorOf(props, "sendingActor")

      val size = 1000
      val macInclusive = 100000

      def randomEvents() =
        (0 until size).map(_ => Event(Random.nextInt(macInclusive))).toVector

      val unsorted = randomEvents()
      sendingActor ! SortEvents(unsorted)

      expectMsgPF() {
        case SortedEvents(events) =>
          events.size must be(size)
          unsorted.sortBy(_.id) must be(events)
      }
    }
  }

  "A Filtering Actor" must {
    // 特定のメッセージをフィルターにかける
    "filter out particular messages" in {
      import FilteringActor._
      val props = FilteringActor.props(testActor, 100)
      val filterActor = system.actorOf(props, "filter-1")
      filterActor ! Event(1)
      filterActor ! Event(2)
      filterActor ! Event(1)
      filterActor ! Event(3)
      filterActor ! Event(4)
      filterActor ! Event(5)
      filterActor ! Event(5)
      filterActor ! Event(6)
      val eventIds = receiveWhile() {
        case Event(id) if id <= 5 => id
      }
      eventIds must be(List(1, 2, 3, 4, 5))
      expectMsg(Event(6))
    }
    // expectNoMsgを使って特定のメッセージをフィルターにかける
    "filter out particular messages using expectNoMsg" in {
      import FilteringActor._

      val props = FilteringActor.props(testActor, 5)
      val filterActor = system.actorOf(props, "filter-2")
      filterActor ! Event(1)
      filterActor ! Event(2)
      expectMsg(Event(1))
      expectMsg(Event(2))
      filterActor ! Event(1)
      expectNoMessage
      filterActor ! Event(3)
      expectMsg(Event(3))
      filterActor ! Event(3)
      expectNoMessage
      filterActor ! Event(4)
      filterActor ! Event(5)
      filterActor ! Event(3)
      expectMsg(Event(4))
      expectMsg(Event(5))
      expectNoMessage
      expectNoMessage
    }
  }
}
