name := """KickAppServer"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1204-jdbc4"

libraryDependencies ++= Seq("com.typesafe.play" %% "play-slick" % "1.1.0", "com.typesafe.play" %% "play-slick-evolutions" % "1.1.0")
