package testdriven

import akka.actor._

class EchoActor extends Actor {
  def receive: Receive = sender() ! _
}
