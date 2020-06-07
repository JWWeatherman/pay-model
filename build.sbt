
lazy val payModel = (project in file("."))
  .settings(
    name := "pay-model",
    version := "0.1",
    scalaVersion := "2.11.11",
    organization := "com.lightningPay",
    libraryDependencies ++= Seq(
      playJson,
      bitcoinj,
      sttpModel
    ) ++ scalatest
  )

lazy val scalaTestVersion = "3.1.1"
lazy val playJsonVersions = "2.7.3"
lazy val playJson  = "com.typesafe.play" %% "play-json" % playJsonVersions
lazy val bitcoinj  = "org.bitcoinj" % "bitcoinj-core" % "0.15.2"
lazy val btcDeps = Seq(
  "fr.acinq" % "bitcoin-lib_2.11" % "0.13"
)

val scalatest = Seq(
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
  "org.scalactic" %% "scalactic" % scalaTestVersion
)

val akkaHttpVersion = "10.1.11"
val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
lazy val sttpModel = "com.softwaremill.sttp.model" %% "core" % "1.1.3"
