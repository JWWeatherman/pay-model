import sbt.Keys.{libraryDependencies, resolvers, scalaVersion, scalacOptions}
import sbt._

// This object is only used when running sbt from the pay-model directory, a project like pay or mathbot will use
// the Dependencies object defined in its ./project directory instead

object Dependencies {
  val scalaV = "2.11.11"
  lazy val playJsonV = "2.7.3"
  lazy val scalaTestV = "3.0.8"
  lazy val akkaVersion = "2.5.26"

  lazy val nameof = "com.github.dwickern" %% "scala-nameof" % "1.0.3"
  lazy val sttpModel = "com.softwaremill.sttp.model" %% "core" % "1.1.3"
  lazy val bitcoinj = "org.bitcoinj" % "bitcoinj-core" % "0.15.2"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % scalaTestV % "test"
  lazy val playJson = "com.typesafe.play" %% "play-json" % playJsonV
  lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVersion


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

}


