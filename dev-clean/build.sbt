name := "SAI"

scalaVersion := "2.12.10"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.8"
libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value % "compile"
libraryDependencies += "org.scala-lang" % "scala-library" % scalaVersion.value % "compile"
libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value % "compile"

autoCompilerPlugins := true
val paradiseVersion = "2.1.0"
addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full)

parallelExecution in Test := false

lazy val sai = (project in file(".")).dependsOn(lms % "test->test; compile->compile")

lazy val lms = ProjectRef(file("../lms-clean"), "lms-clean")
  // .settings(fork := true)
