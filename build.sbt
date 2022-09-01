import Dependencies._
import sbt._

//ThisBuild / licenses            += License.Apache2

// maven magic, see https://github.com/makingthematrix/scala-suffix/tree/56270a6b4abbb1cd1008febbd2de6eea29a23b52#but-wait-thats-not-all
Compile / packageBin / packageOptions += Package.ManifestAttributes("Automatic-Module-Name" -> "paymodel")
val scala213 = "2.13.3"
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

val commonDeps = Seq(
    playJson,
    bitcoinj,
    sttpModel,
    scalaTest,
    akkaActor,
    unixSocket,
    requests,
    akkaStream,
    akkaStreamTestkit,
    akkaTestkit,
    scalactic,
    mockito,
    nameOf,
    scodec
  ) ++ sttp ++ macwire ++ loggingDeps

lazy val eclairDeps = Seq(
  "com.google.guava" % "guava" % "31.1-jre", // eclair
  "org.scala-lang.modules" % "scala-parser-combinators_2.13" % "2.1.0", // immortan
  "fr.acinq.secp256k1" % "secp256k1-kmp-jni-jvm" % "0.6.3", // eclair
  "org.scodec" % "scodec-core_2.13" % "1.11.9", // immortan + eclair
  "commons-codec" % "commons-codec" % "1.10", // immortan + eclair
  "io.reactivex" % "rxscala_2.13" % "0.27.0", // immortan
  "org.json4s" % "json4s-native_2.13" % "3.6.7", // electrum,
  "io.spray" % "spray-json_2.13" % "1.3.5", // immortan,
//  "com.typesafe.akka" % "akka-actor_2.13" % "2.6.9", // immortan + eclair
  "io.netty" % "netty-all" % "4.1.42.Final", // electrum
  "com.softwaremill.quicklens" % "quicklens_2.13" % "1.8.4", // immortan
//  "org.bouncycastle" % "bcprov-jdk15to18" % "1.68", // eclair
  "com.sparrowwallet" % "hummingbird" % "1.6.2" // immortan
//  "com.github.alexarchambault" % "case-app_2.13" % "2.1.0-M13", // cliche
//  "com.lihaoyi" % "requests_2.13" % "0.7.0", // cliche
//  "com.iheart" % "ficus_2.13" % "1.5.0", // cliche
//  "org.xerial" % "sqlite-jdbc" % "3.27.2.1" // cliche
)

lazy val paymodel = (project in file("."))
  .settings(commonSettings: _*)
  .configs(IntegrationTest)
  .settings(
    name := "pay-model",
    version := "0.0.1",
    coverageMinimumStmtTotal := 70,
    libraryDependencies ++= commonDeps ++ eclairDeps,
    coverageFailOnMinimum := false,
    coverageHighlighting := true,
    organization := "com.mathbot",
    scalaVersion := scala213,
    Defaults.itSettings
  )

def addCommandsAlias(name: String, cmds: Seq[String]) =
  addCommandAlias(name, cmds.mkString(";", ";", ""))

addCommandsAlias("validate", "compile" :: "test:compile" :: "scalafmtCheckAll" :: Nil)
addCommandsAlias("fmt", Seq("scalafmt", "test:scalafmt", "it:scalafmt"))
addCommandsAlias("generateCoverageReport", "clean" :: "coverage" :: "test" :: "coverageReport" :: Nil)
addCommandsAlias("githubWorkflow", Seq("validate", "coverage", "test", "coverageReport"))

addCommandsAlias("cc", Seq("clean", "compile"))
addCommandAlias("err", "lastGrep error compile")
addCommandAlias("errt", "lastGrep error test:compile")
