package gensym.wasm

import org.scalatest.FunSuite

import lms.core.stub.Adapter

import gensym.wasm.miniwasm.{ModuleInstance}
import gensym.wasm.parser._
import gensym.wasm.stagedconcolicminiwasm._

// This 'test file' is not intended to test functionality, but to generate compiled code for btree benchmarks
class TestBenchmark extends FunSuite {
  def compileToCpp(filename: String,
                   main: Option[String] = None) = {
    import sys.process._

    println(s"Compiling $filename to C++")
    val moduleInst = ModuleInstance(Parser.parseFile(filename))
    val cppFile = s"$filename.cpp"
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
                      main: Option[String] = None) = {
    import java.io.File
    val d = new File(dir)
    d.listFiles().filter(_.getName.endsWith(".wat")).foreach { file =>
      compileToCpp(file.getAbsolutePath, main)
    }
  }

  def compileDirTreeToCpp(dir: String,
                          main: Option[String] = None): Unit = {
    import java.io.File

    def walk(file: File): Unit = {
      if (file.isDirectory) {
        Option(file.listFiles()).getOrElse(Array.empty).foreach(walk)
      } else if (file.getName.endsWith(".wat")) {
        compileToCpp(file.getAbsolutePath, main)
      }
    }

    walk(new File(dir))
  }

  test("compile-btree-benchmarks") { compileDirToCpp("./benchmarks/pldi2026/btree/tests-normalized", Some("main")) }
  test("compile-crafted-benchmarks") { compileDirToCpp("./benchmarks/pldi2026/crafted/") }
  test("compile-aws-aws-encryption-sdk") { compileDirToCpp("./benchmarks/pldi2026/aws-encryption-sdk/tests-original-normalized/", Some("__original_main")) }
  test("compile-collection-c-benchmarks") { compileDirTreeToCpp("./benchmarks/pldi2026/Collection-C/tests-normalized/", Some("__original_main")) }
  test("compile-quicksort-benchmark") { compileDirToCpp("/home/zdh/WorkSpace/cJSON", Some("main")) }
  test("compile-a-single-file") {
    sys.env.get("INPUT") match {
      case Some(path) =>
        val main = sys.env.get("MAIN")
        compileToCpp(path, main)
      case None => println("Environment variable INPUT not set; skipping compileToCpp")
    }
  }
}
