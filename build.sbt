organization := "com.storecove"

name := "rollbar-scala"

version := "1.1-SNAPSHOT"

description := "Notifier library for integrating Scala apps with the Rollbar service."

scalaVersion := "2.10.4"

val slf4jVersion = "1.7.12"
val dispatchVersion = "0.11.2"
val log4jVersion = "1.2.17"
val logbackVersion = "1.1.3"
val json4sVersion = "3.2.11"
val scalatestVersion = "2.2.4"

assemblyShadeRules in assembly := Seq(
    ShadeRule.rename("org.json4s.**" -> "shaded.json4s.@1").inAll
)

dependencyOverrides ++= Set(
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.4" // upgrade jackson to 2.4.4 to be compatible with spark 1.6.0
)

libraryDependencies ++= Seq(
    "org.slf4j" % "slf4j-api" % slf4jVersion % "provided",
    "net.databinder.dispatch" %% "dispatch-core" % dispatchVersion % "provided",
    "log4j" % "log4j" % log4jVersion % "provided",
    "ch.qos.logback" % "logback-classic" % logbackVersion % "provided",
    "org.json4s" %% "json4s-jackson" % json4sVersion,
    "org.scalatest" %% "scalatest" % scalatestVersion % "test"
)
