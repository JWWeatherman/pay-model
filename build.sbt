import Dependencies._
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
      sttpModel,
    ) ++ scalatest
  )
