name := "genwasym-library-demo"

version := "0.1.0"

scalaVersion := "2.12.10"

Compile / run / fork := true

Compile / run / javaOptions ++= Seq("-Xmx4G", "-Xss64M")
