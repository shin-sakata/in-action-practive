package testdriven

import akka.actor._
import Greeter.Greeting

object Main extends App {
  val system = ActorSystem()
  val greeter1 = system.actorOf(Greeter.props(), "1")
  val greeter2 = system.actorOf(Greeter.props(), "2")
  greeter1 ! Greeting("Actor1!")
  greeter2 ! Greeting("Actor2!")
  system.terminate()
}
