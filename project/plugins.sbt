addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.10.0")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2")
addSbtPlugin("com.github.cb372" % "sbt-explicit-dependencies" % "0.2.16")

// sbt coverage test coverageReport
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.9.3")
// loads .env file
addSbtPlugin("au.com.onegeek" %% "sbt-dotenv" % "2.1.233")
// dependencyTree, dependencyBrowseGraph, dependencyBrowseTree
addDependencyTreePlugin
// on compile will generate classes from /resources/*.yml
//addSbtPlugin(
//  "io.github.ghostbuster91.sttp-openapi" % "sbt-codegen-plugin" % "0.4.2"
//) // import SttpOpenApiCodegenPlugin._ ; enablePlugins(SttpOpenApiCodegenPlugin)
//addSbtPlugin("com.geirsson" % "sbt-ci-release" % "1.5.5")
//addSbtPlugin("com.github.tkawachi" % "sbt-doctest" % "0.9.8")
//addSbtPlugin("de.heikoseeberger" % "sbt-header" % "5.6.0")
//addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.16")
//addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0")
//addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.13")
//addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.25")
