import sbt._

lazy val scalaTestVersion = "3.1.1"
lazy val playJsonVersions = "2.7.3"
lazy val playJson = "com.typesafe.play" %% "play-json" % playJsonVersions
lazy val bitcoinj = "org.bitcoinj" % "bitcoinj-core" % "0.15.2"
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



lazy val commonSettings = Seq(
  scalaVersion := "2.11.11",
  libraryDependencies ++= scalatest
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .aggregate(nameof, paymodel)
  .dependsOn(nameof, paymodel)

lazy val nameof = (project in file("nameof"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value)
  )

lazy val paymodel = (project in file("./models"))
  .dependsOn(nameof)
  .settings(commonSettings: _*)
  .settings(
    name := "pay-model",
    version := "0.1",
    organization := "com.lightningPay",
    libraryDependencies ++= Seq(
      playJson,
      bitcoinj,
      sttpModel
    ) ++ scalatest
  )
