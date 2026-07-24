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
                          outputDir: Option[String] = None,
                          skipExisting: Boolean = false): Unit = {
    import java.io.File
    import scala.collection.mutable.ArrayBuffer
    import scala.util.control.NonFatal

    val root = new File(dir).getCanonicalFile
    val failures = ArrayBuffer.empty[(String, Throwable)]

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
        val targetCpp = targetDir.map(dir => new File(dir, s"${file.getName}.cpp"))
        if (skipExisting && targetCpp.exists(_.isFile)) {
          println(s"Skipping ${file.getAbsolutePath}; C++ already exists at ${targetCpp.get.getPath}")
        } else {
          try {
            compileToCpp(file.getAbsolutePath, main, targetDir)
          } catch {
            case NonFatal(error) =>
              println(s"Failed to compile ${file.getAbsolutePath}; continuing")
              failures += ((file.getAbsolutePath, error))
          }
        }
      }
    }

    walk(root)

    if (failures.nonEmpty) {
      val summary = failures.map { case (file, error) =>
        s"$file: ${Option(error.getMessage).getOrElse(error.getClass.getName)}"
      }.mkString("\n  ", "\n  ", "")
      throw new Exception(s"${failures.size} WAT file(s) failed to compile:$summary")
    }
  }

  test("compile-btree-benchmarks") {
    compileDirToCpp(
      "./benchmarks/oopsla2026/btree/genwasym-test-input",
      Some("main"),
      Some("./benchmarks/oopsla2026/btree/genwasym-test-artifacts")
    )
  }
  test("compile-evaluator-benchmarks") {
    compileDirToCpp(
      "./benchmarks/oopsla2026/evaluator/genwasym-test-input",
      None,
      Some("./benchmarks/oopsla2026/evaluator/genwasym-test-artifacts")
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
  test("compile-collection-c-normal-benchmarks") {
    compileDirTreeToCpp(
      "./benchmarks/oopsla2026/Collection-C/genwasym-test-input/normal/",
      Some("__original_main"),
      Some("./benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/normal"),
      skipExisting = true
    )
  }
  test("compile-collection-c-array-benchmarks") {
    compileDirTreeToCpp(
      "./benchmarks/oopsla2026/Collection-C/genwasym-test-input/normal/array/",
      Some("__original_main"),
      Some("./benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/normal/array"),
      skipExisting = true
    )
  }
  test("compile-collection-c-buggy-benchmarks") {
    compileDirTreeToCpp(
      "./benchmarks/oopsla2026/Collection-C/genwasym-test-input/bugs/",
      Some("__original_main"),
      Some("./benchmarks/oopsla2026/Collection-C/genwasym-test-artifacts/buggy")
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
