name := "github-mqtt-webhooks"

version := "1.0"

lazy val `github-mqtt-webhooks` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.11"

libraryDependencies += guice
libraryDependencies += specs2 % Test

libraryDependencies += "org.fusesource.mqtt-client" % "mqtt-client" % "1.16"

enablePlugins(DockerPlugin)
dockerBaseImage := "openjdk:11-jre"
dockerExposedPorts := Seq(9000)
Universal / mappings += file("conf/logback.xml") -> "conf/logback.xml"
