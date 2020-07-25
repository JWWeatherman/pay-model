import sbt._

import Dependencies._

// This Dependencies is only used when running sbt from the pay-model root.  Otherwise it will use the Dependencies
// object defined in the /pay/project or /math-bot/project directory.

lazy val paymodel = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "pay-model",
    version := "0.0.1",
    organization := "com.mathbot",
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      playJson,
      bitcoinj,
      sttpModel,
      nameof,
      scalaTest,
      akkaActor
    )
  )
