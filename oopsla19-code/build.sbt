import Dependencies._

ThisBuild / scalaVersion     := "2.12.7"
ThisBuild / version          := "0.1-OOPSLA"
ThisBuild / organization     := "sai"

lazy val root = (project in file(".")).settings(
  name := "SAI",
  autoCompilerPlugins := true,
  resolvers += Resolver.sonatypeRepo("snapshots"),

  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.9"),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  addCompilerPlugin("org.scala-lang.plugins" % "scala-continuations-plugin_2.12.0" % "1.0.3"),

  libraryDependencies += scalaTest % Test,
  libraryDependencies += "org.scala-lang.lms" %% "lms-core-macrovirt" % "0.9.0-SNAPSHOT",
  libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value % "compile",
  libraryDependencies += "org.scala-lang" % "scala-library" % scalaVersion.value % "compile",
  libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value % "compile",
  libraryDependencies += "org.scala-lang.plugins" % "scala-continuations-library_2.12" % "1.0.3",
  libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0",
  libraryDependencies += "org.typelevel" %% "cats-free" % "1.6.0",
  libraryDependencies += "com.github.mpilquist" %% "simulacrum" % "0.15.0",
  libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6",
  libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.27",

  scalacOptions += "-Xcheckinit",
  scalacOptions += "-deprecation",
  scalacOptions += "-Ypartial-unification",
  scalacOptions += "-P:continuations:enable",

  concurrentRestrictions in Global += Tags.limit(Tags.Test, 1)
)

