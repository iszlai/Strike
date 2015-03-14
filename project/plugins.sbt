logLevel := Level.Warn

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.6.0")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.2")

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"
