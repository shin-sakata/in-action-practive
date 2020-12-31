package testdriven

import akka.actor._

class FilteringActor(nextActor: ActorRef, bufferSize: Long) extends Actor {
  import FilteringActor._

  var lastMessages: Vector[Event] = Vector[Event]()

  def receive: Receive = {
    case msg: Event =>
      if (!lastMessages.contains(msg)) {
        lastMessages = lastMessages :+ msg
        nextActor ! msg

        // バッファーサイズに達すると、古いものを削除
        if (lastMessages.size > bufferSize) {
          lastMessages = lastMessages.tail
        }
      }
  }
}

object FilteringActor {
  def props(nextActor: ActorRef, bufferSize: Int): Props =
    Props(new FilteringActor(nextActor, bufferSize))

  case class Event(id: Long)
}
