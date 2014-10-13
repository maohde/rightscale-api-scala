name := "rightscale-api"

version := ".1"

scalaVersion := "2.11.1"

resolvers ++= Seq("Sonatype Nexus releases" at "o",
                  "Sonatype Nexus snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
                  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")

libraryDependencies +=  "org.scalaz" %% "scalaz-core" % "7.0.6"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.0" % "test"

libraryDependencies += "io.argonaut" %% "argonaut" % "6.1-M3"
