import Dependencies._
import sbt._


addCommandsAlias("validate", "clean" :: "compile":: "test:compile" :: "scalafmtCheckAll" :: Nil)
addCommandsAlias("fmt", Seq("scalafmt", "test:scalafmt", "it:scalafmt"))
addCommandsAlias("generateCoverageReport", "clean" :: "coverage" :: "test" :: "coverageReport" :: Nil)
addCommandsAlias("githubWorkflow", Seq("validate", "coverage", "test", "coverageReport"))


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
  "com.github.dwickern" %% "scala-nameof" % "3.0.0" % "provided",
  bitcoinLib,
  scodec
) ++ sttp ++ macwire





lazy val paymodel = (project in file("."))
  .settings(commonSettings: _*)
  .configs(IntegrationTest)
  .settings(
    name := "pay-model",
    version := "0.0.1",
    coverageMinimum := 70,
    libraryDependencies := commonDeps,
    coverageFailOnMinimum := false,
    coverageHighlighting := true,
    organization := "com.mathbot",
    scalaVersion := scala213,
    Defaults.itSettings,
  )

def addCommandsAlias(name: String, cmds: Seq[String]) =
  addCommandAlias(name, cmds.mkString(";", ";", ""))
