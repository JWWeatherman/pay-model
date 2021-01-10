import Dependencies._
import sbt._
val scalaV = "2.11.11"
// This Dependencies is only used when running sbt from the pay-model root.  Otherwise it will use the Dependencies
// object defined in the /pay/project or /math-bot/project directory.
val commonSettings = Seq(
  resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/",
  scalacOptions ++= Seq(
    "-language:postfixOps",
    "-language:implicitConversions",
    "-Ywarn-numeric-widen",
    "-Xlint", //  enables a bunch of compiler warnings https://docs.scala-lang.org/overviews/compiler-options/index.html#Warning_Settings
    "-deprecation",
    "-feature", // Emit warning and location for usages of features that should be imported explicitly.
    "-unchecked" // Generated code depends on assumptions.
    //  "-Xfatal-warnings", // causes the compiler to fail if there are any warnings
  )
)
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
      akkaActor,
      unixSocket,
      akkaStream,
      akkaStreamTestkit,
      akkaTestkit,
      scalactic,
      mockito
    ) ++ sttp ++ macwire
  )
