import sbt._

lazy val scalaTestVersion = "3.1.1"
lazy val playJsonVersions = "2.7.3"
lazy val playJson = "com.typesafe.play" %% "play-json" % playJsonVersions
lazy val bitcoinj = "org.bitcoinj" % "bitcoinj-core" % "0.15.2"
lazy val btcDeps = Seq(
  "fr.acinq" %% "bitcoin-lib" % "0.13"
)

val scalatest = Seq(
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
  "org.scalactic" %% "scalactic" % scalaTestVersion
)

val akkaHttpVersion = "10.1.11"
val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
lazy val sttpModel = "com.softwaremill.sttp.model" %% "core" % "1.1.3"
lazy val nameof = "com.github.dwickern" %% "scala-nameof" % "1.0.3"


lazy val paymodel = (project in file("."))
  .settings(
    scalaVersion := "2.11.11",
    name := "pay-model",
    version := "0.0.1",
    organization := "com.lightningPay",
    libraryDependencies ++= Seq(
      playJson,
      bitcoinj,
      sttpModel,
      nameof
    ) ++ scalatest
  )
