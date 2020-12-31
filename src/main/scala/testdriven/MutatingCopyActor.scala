package testdriven

import akka.actor._

class MutatingCopyActor(receiver: ActorRef) extends Actor {
  import MutatingCopyActor._

  def receive: Receive = {
    case SortEvents(unsorted) =>
      receiver ! SortedEvents(unsorted.sortBy(_.id))
  }
}

object MutatingCopyActor {
  def props(receiver: ActorRef): Props = Props(new MutatingCopyActor(receiver))

  case class Event(id: Long)
  // 貰うメッセージ
  case class SortEvents(unsorted: Vector[Event])
  // 最終的にソート後に返すメッセージ
  case class SortedEvents(sorted: Vector[Event])
}
