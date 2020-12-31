name := "akka-in-action-practice"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies ++= {
  val AkkaVersion = "2.6.10"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % "test",
    "org.scalatest" %% "scalatest" % "3.0.9" % "test"
  )
}
