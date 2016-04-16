name := "github-mqtt-webhooks"

version := "1.0"

lazy val `github-mqtt-webhooks` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq("com.sandinh" %% "paho-akka" % "1.2.0")

libraryDependencies += specs2 % Test

maintainer in Linux := "Tony McCrae <tony@eelpieconsulting.co.uk>"

packageSummary in Linux := "Github MQTT webhooks"

packageDescription := "Convert Github webhooks to MQTT events"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.2"

import com.typesafe.sbt.packager.archetypes.ServerLoader

serverLoading in Debian:= ServerLoader.Systemd
