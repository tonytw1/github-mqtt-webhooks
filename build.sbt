name := "github-mqtt-webhooks"

version := "1.0"

lazy val `github-mqtt-webhooks` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.12"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies += specs2 % Test

libraryDependencies += "org.fusesource.mqtt-client" % "mqtt-client" % "1.14"

enablePlugins(DockerPlugin)
dockerBaseImage := "openjdk:11-jre"
dockerExposedPorts in Docker := Seq(9000)
