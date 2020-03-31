name := "SAI"

scalaVersion := "2.12.10"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.8"
libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value % "compile"
libraryDependencies += "org.scala-lang" % "scala-library" % scalaVersion.value % "compile"
libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value % "compile"

libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0"
libraryDependencies += "org.typelevel" %% "cats-free" % "1.6.0"
libraryDependencies += "com.github.mpilquist" %% "simulacrum" % "0.15.0"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6"
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.27"

libraryDependencies += "org.antlr" % "antlr4-runtime" % "4.8"
//libraryDependencies += "org.antlr" % "stringtemplate" % "4.3"

libraryDependencies += "org.atnos" %% "eff" % "5.7.0"

scalacOptions ++= Seq(
  "-Xcheckinit",
  "-deprecation",
  "-Xlog-implicit-conversions",
  "-language:implicitConversions",
  "-Ypartial-unification",
  "-P:continuations:enable",
)

autoCompilerPlugins := true

val paradiseVersion = "2.1.0"
addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full)
addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.9")
addCompilerPlugin("org.scala-lang.plugins" % "scala-continuations-plugin_2.12.0" % "1.0.3")

parallelExecution in Test := false

lazy val sai = (project in file(".")).dependsOn(lms % "test->test; compile->compile")

lazy val lms = ProjectRef(file("../lms-clean"), "lms-clean")
  // .settings(fork := true)
