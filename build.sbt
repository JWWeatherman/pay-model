import sbt._

import Dependencies._

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
      scalaTest
    )
  )
