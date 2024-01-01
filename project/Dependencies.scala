import Dependencies.scalaLogging
import sbt._
// This object is only used when running sbt from the pay-model directory, a project like pay or mathbot will use
// the Dependencies object defined in its ./project directory instead

//noinspection SpellCheckingInspection
object Dependencies {
  lazy val alpakkaSocketV = "5.0.0"
  lazy val mockitoV = "3.2.10.0"
  lazy val bitcoinjV = "0.16.2"
  val playJsonV = "2.9.4"
  val macwireV = "2.5.8"
  val scalaTestV = "3.2.15"
  val akkaHttpV = "10.5.0"
  val akkaV = "2.8.0"
  val sttpV = "3.8.13"
  lazy val sttpModelV = "1.5.5"
  val mongoV = "4.9.0"
  val mockitoScalaV = "1.17.12"
  val guavaV = "28.1-jre"
  val slf4jV = "2.0.5"
  val akkaHttpCorsV = "1.0.0"
  val CatsV = "2.9.0"
  private val scalaLoggingV = "3.9.5"
  val nameOfV = "4.0.0"
  val requestsV = "0.8.0"
  val scodecCoreV = "1.11.10"

  lazy val bitcoinj = "org.bitcoinj" % "bitcoinj-core" % bitcoinjV
  lazy val bouncy = "org.bouncycastle" % "bcprov-jdk15on" % "1.70"

  private val jwtPlayJsonV = "5.0.0"
  val jwtPlayJson = "com.pauldijou" %% "jwt-play-json" % jwtPlayJsonV
  lazy val sttp = Seq(
    "com.softwaremill.sttp.client3" %% "core" % sttpV,
    "com.softwaremill.sttp.client3" %% "akka-http-backend" % sttpV,
    "com.softwaremill.sttp.client3" %% "play-json" % sttpV
  )
  val requests = "com.lihaoyi" %% "requests" % requestsV
  val scodec = "org.scodec" %% "scodec-core" % scodecCoreV
  val nameOf = "com.github.dwickern" %% "scala-nameof" % nameOfV % "provided"
  lazy val okhttp = "com.softwaremill.sttp.client3" %% "okhttp-backend" % sttpV
  lazy val akkaTestkit =
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "it,test"
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaV
  lazy val unixSocket = "com.lightbend.akka" %% "akka-stream-alpakka-unix-domain-socket" % alpakkaSocketV
  lazy val sttpModel = "com.softwaremill.sttp.model" %% "core" % sttpModelV

  lazy val akkaStreamTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % akkaV % "it,test"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % scalaTestV % "it,test"
  lazy val scalactic = "org.scalactic" %% "scalactic" % scalaTestV % "it,test"
  lazy val mockito = "org.scalatestplus" %% "mockito-3-4" % mockitoV % "it,test"

  lazy val playJson = "com.typesafe.play" %% "play-json" % playJsonV
  lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaV
  lazy val macwire = Seq(
    "com.softwaremill.macwire" %% "macros" % macwireV % "provided",
    "com.softwaremill.macwire" %% "macrosakka" % macwireV % "provided",
    "com.softwaremill.macwire" %% "util" % macwireV,
    "com.softwaremill.macwire" %% "proxy" % macwireV
  )
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingV

  // https://mvnrepository.com/artifact/com.typesafe.akka/akka-slf4j
  lazy val loggingDeps = Seq(
    scalaLogging,
    "org.slf4j" % "slf4j-api" % slf4jV
  )

  val cats = "org.typelevel" %% "cats-core" % CatsV

  val commonDeps = Seq(
      playJson,
      cats,
      jwtPlayJson,
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
      bitcoinj,
      bouncy,
      scodec
    ) ++ sttp ++ macwire ++ loggingDeps
}
