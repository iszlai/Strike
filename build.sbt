import scala.collection.Seq

name := "Strike"

version := "1.0"

scalaVersion := "2.11.4"

mainClass in (Compile, run) := Some("com.strike.webapp.Bootstrap")

libraryDependencies ++={
  val akkaV = "2.3.6"
  val sprayV = "1.3.2"
  Seq(
    "ch.qos.logback" % "logback-classic" % "1.0.9" ,
    "org.scalaj" %% "scalaj-http" % "1.1.0" withSources() withJavadoc(),
    "org.scala-lang.modules" %% "scala-xml" % "1.0.2" withSources() withJavadoc(),
    "com.typesafe" % "config" % "1.2.1",
    "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
    "org.apache.commons" % "commons-math3" % "3.3",
    "net.debasishg" %% "redisclient" % "2.13",
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test"
   // "org.specs2"          %%  "specs2-core"   % "2.3.7" % "test"
  )
}

