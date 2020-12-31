package testdriven

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

class Greeter(listener: Option[ActorRef]) extends Actor with ActorLogging {
  import Greeter._
  def receive: Receive = {
    case Greeting(who) =>
      val message = s"Hello ${who}!"
      log.info(message)
      listener.foreach(_ ! message)
  }
}

object Greeter {
  def props(listener: Option[ActorRef] = None): Props =
    Props(new Greeter(listener))
  case class Greeting(message: String)
}
