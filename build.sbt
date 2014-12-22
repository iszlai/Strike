import scala.collection.Seq

name := "Strike"

version := "1.0"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.0.9" ,
  "com.typesafe.akka" %% "akka-actor" % "2.3.4" withSources() withJavadoc() ,
  "com.typesafe.akka" %% "akka-testkit" % "2.3.4" withSources() withJavadoc(),
  "org.scalaj" %% "scalaj-http" % "1.1.0" withSources() withJavadoc(),
  "org.scala-lang.modules" %% "scala-xml" % "1.0.2" withSources() withJavadoc(),
  "com.typesafe" % "config" % "1.2.1",
  "org.apache.commons" % "commons-math3" % "3.3",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
)

