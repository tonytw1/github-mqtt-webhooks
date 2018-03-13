name := "github-mqtt-webhooks"

version := "1.0"

lazy val `github-mqtt-webhooks` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.12"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies += specs2 % Test


maintainer in Linux := "Tony McCrae <tony@eelpieconsulting.co.uk>"

packageSummary in Linux := "Github MQTT webhooks"

packageDescription := "Convert Github webhooks to MQTT events"

libraryDependencies += "org.fusesource.mqtt-client" % "mqtt-client" % "1.14"

import com.typesafe.sbt.packager.archetypes.ServerLoader

serverLoading in Debian:= ServerLoader.Systemd

javaOptions in Universal ++= Seq(
  // -J params will be added as jvm parameters
  "-J-Xmx256m"
)
