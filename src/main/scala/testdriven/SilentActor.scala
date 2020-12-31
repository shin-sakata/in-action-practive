package testdriven

import akka.actor._

class SilentActor extends Actor {
  import SilentActor._

  var initialState: Vector[String] = Vector[String]()

  def receive: Receive = {
    case SilentMessage(data) =>
      initialState = initialState :+ data
    case GetState(receiver) =>
      receiver ! initialState
  }

  def state: Vector[String] = initialState
}

object SilentActor {
  def props: Props = Props(new SilentActor)

  case class SilentMessage(data: String)
  case class GetState(receiver: ActorRef)
}
