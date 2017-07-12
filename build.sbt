import play.sbt.PlayScala

name := """KickAppServer"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += evolutions

libraryDependencies += filters

libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1204-jdbc4"

libraryDependencies ++= Seq("com.typesafe.play" %% "play-slick" % "2.0.0", "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0")

libraryDependencies += "net.logstash.logback" % "logstash-logback-encoder" % "4.6"

libraryDependencies += "com.zivver" %% "web-push" % "0.2.1"

scalastyleConfig := baseDirectory.value / "project/scalastyle_config.xml"