name := "GenSym"

scalaVersion := "2.12.10"

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.8" % "test,bench"
libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value % "compile"
libraryDependencies += "org.scala-lang" % "scala-library" % scalaVersion.value % "compile"
libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value % "compile"
libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0"
libraryDependencies += "org.typelevel" %% "cats-free" % "1.6.0"
libraryDependencies += "com.github.mpilquist" %% "simulacrum" % "0.15.0"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6"
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.27"
libraryDependencies += "org.antlr" % "antlr4-runtime" % "4.13.0"
libraryDependencies += "org.atnos" %% "eff" % "5.7.0"

Compile / unmanagedJars += {
  baseDirectory.value / "third-party" / s"scalaz3_2.12-4.7.1.jar"
}

scalacOptions ++= Seq(
  "-Xcheckinit",
  "-deprecation",
  // "-Xlog-implicit-conversions",
  "-language:implicitConversions",
  "-Ypartial-unification",
  "-P:continuations:enable",
)

autoCompilerPlugins := true

//https://www.scala-sbt.org/release/docs/Running-Project-Code.html
fork := true
val commonJavaOptions = Seq(
  "-Xms4G",
  "-Xmx32G",
  "-Xss1024M",
  "-XX:MaxMetaspaceSize=8G",
  "-XX:ReservedCodeCacheSize=2048M"
)
run / javaOptions ++= commonJavaOptions
Test / javaOptions ++= commonJavaOptions

val paradiseVersion = "2.1.0"
addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full)
addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.3" cross CrossVersion.full)
addCompilerPlugin("com.github.tomasmikula" %% "pascal" % "0.3.5")
addCompilerPlugin("org.scala-lang.plugins" % "scala-continuations-plugin_2.12.0" % "1.0.3")
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0-M4")

lazy val Bench = config("bench").extend(Test)

fork in Bench := false
parallelExecution in Test := false
parallelExecution in Bench := false

lazy val lms = ProjectRef(file("./third-party/lms-clean"), "lms-clean")
  // .settings(fork := true)

lazy val gensym = (project in file(".")).dependsOn(lms % "test->test; compile->compile")
                                        .configs(Bench)
                                        .settings(inConfig(Bench)(Defaults.testSettings))
                                        .settings(assembly / mainClass := Some("gensym.RunGenSym"))

