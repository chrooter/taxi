name         := "taxi"

organization := "ru.taxi"

version      := "1.0"

scalaVersion := "2.11.5"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  filters
)

libraryDependencies ++= Seq(
  "com.typesafe.slick"  %% "slick"            % "2.1.0",
  "org.postgresql"      %  "postgresql"       % "9.3-1102-jdbc41",
  "com.h2database" % "h2" % "1.4.181" % "test",
  "com.typesafe.play" % "play-slick_2.11" % "0.8.0",
  "org.slf4j" % "slf4j-log4j12" % "1.7.10",
  "com.mohiva" %% "play-silhouette" % "2.0-RC2"
)
