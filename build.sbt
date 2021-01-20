import Dependencies._
import sbt._

val scala211 = "2.11.11"
val scala212 = "2.12.10"
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
  akkaStream,
  akkaStreamTestkit,
  akkaTestkit,
  scalactic,
  mockito
) ++ sttp ++ macwire

val scala211Deps = nameof :: Nil
val scala212Deps = nameof2 :: Nil
val scala213Deps = nameof2 :: Nil

libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 11)) => commonDeps ++ scala211Deps
    case Some((2, 12)) => commonDeps ++ scala212Deps
    case Some((2, 13)) => commonDeps ++ scala213Deps
    case _ => Seq()
  }
}



lazy val paymodel = (project in file("."))
  .settings(commonSettings: _*)
  .configs(IntegrationTest)
  .settings(
    name := "pay-model",
    version := "0.0.1",
    organization := "com.mathbot",
    scalaVersion := scala213,
    crossScalaVersions := scala211 :: scala212 :: scala213 :: Nil,
    Defaults.itSettings,
  )

def addCommandsAlias(name: String, cmds: Seq[String]) =
  addCommandAlias(name, cmds.mkString(";", ";", ""))

addCommandsAlias("testAll", "compile":: "test:compile" :: "scalafmtCheckAll":: Nil)
