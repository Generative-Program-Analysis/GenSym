package gensym.wasm

import org.scalatest.FunSuite

import lms.core.stub.Adapter

import gensym.wasm.miniwasm.{ModuleInstance}
import gensym.wasm.parser._
import gensym.wasm.stagedconcolicminiwasm._

// This 'test file' is not intended to test functionality, but to generate compiled code for btree benchmarks
class TestBenchmark extends FunSuite {
  def compileToCpp(filename: String,
                   main: Option[String] = None,
                   outputDir: Option[String] = None) = {
    import sys.process._

    println(s"Compiling $filename to C++")
    val moduleInst = ModuleInstance(Parser.parseFile(filename))
    val sourceFile = new java.io.File(filename)
    val cppFile = outputDir match {
      case Some(dir) => new java.io.File(dir, s"${sourceFile.getName}.cpp").getPath
      case None => s"$filename.cpp"
    }
    val generated = WasmToCppCompiler.compile(moduleInst, main, false)

    val code = generated.source
    val writer = new java.io.PrintWriter(new java.io.File(cppFile))
    try {
      writer.write(code)
    } finally {
      writer.close()
    }
  }

  def compileDirToCpp(dir: String,
                      main: Option[String] = None,
                      outputDir: Option[String] = None) = {
    import java.io.File
    val d = new File(dir)
    d.listFiles().filter(_.getName.endsWith(".wat")).foreach { file =>
      compileToCpp(file.getAbsolutePath, main, outputDir)
    }
  }

  def compileDirTreeToCpp(dir: String,
                          main: Option[String] = None,
                          outputDir: Option[String] = None): Unit = {
    import java.io.File

    val root = new File(dir).getCanonicalFile

    def walk(file: File): Unit = {
      if (file.isDirectory) {
        Option(file.listFiles()).getOrElse(Array.empty).foreach(walk)
      } else if (file.getName.endsWith(".wat")) {
        val targetDir = outputDir.map { base =>
          val parent = file.getParentFile.getCanonicalFile
          val relativeParent = root.toPath.relativize(parent.toPath).toString
          val out = new File(base, relativeParent)
          out.mkdirs()
          out.getPath
        }
        compileToCpp(file.getAbsolutePath, main, targetDir)
      }
    }

    walk(root)
  }

  test("compile-btree-benchmarks") {
    compileDirToCpp(
      "./benchmarks/oopsla2026/btree/genwasym-test-input",
      Some("main"),
      Some("./benchmarks/oopsla2026/btree/genwasym-test-artifacts")
    )
  }
  test("compile-crafted-benchmarks") {
    compileDirToCpp(
      "./benchmarks/oopsla2026/crafted/genwasym-test-input",
      None,
      Some("./benchmarks/oopsla2026/crafted/genwasym-test-artifacts")
    )
  }
  test("compile-aws-aws-encryption-sdk") { compileDirToCpp("./benchmarks/oopsla2026/aws-encryption-sdk/tests-original-normalized/", Some("__original_main")) }
  test("compile-collection-c-benchmarks") {
    compileDirTreeToCpp(
      "./benchmarks/oopsla2026/Collection-C/genwasym-test-input/",
      Some("__original_main"),
      Some("./benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts")
    )
  }
  test("compile-quicksort-benchmark") {
    compileDirToCpp(
      "./benchmarks/oopsla2026/quicksort/genwasym-test-input",
      Some("main"),
      Some("./benchmarks/oopsla2026/quicksort/genwasym-test-artifacts")
    )
  }
  test("compile-a-single-file") {
    sys.env.get("INPUT") match {
      case Some(path) =>
        val main = sys.env.get("MAIN")
        compileToCpp(path, main, sys.env.get("OUTPUT_DIR"))
      case None => println("Environment variable INPUT not set; skipping compileToCpp")
    }
  }
}
