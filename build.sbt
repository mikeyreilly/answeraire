name := """play-scala-seed"""
organization := "com.quaxt"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  guice,
  jdbc,
  "org.playframework.anorm" %% "anorm" % "2.6.4",
  "mysql" % "mysql-connector-java" % "5.1.41",
  // "org.joda" % "joda-convert" % "2.2.1",
  "net.logstash.logback" % "logstash-logback-encoder" % "6.2",
  "io.lemonlabs" %% "scala-uri" % "1.5.1",
  "net.codingwell" %% "scala-guice" % "4.2.6",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
)
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
