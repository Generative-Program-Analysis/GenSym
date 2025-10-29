package gensym.wasm

import org.scalatest.FunSuite

import lms.core.stub.Adapter

import gensym.wasm.miniwasm.{ModuleInstance}
import gensym.wasm.parser._
import gensym.wasm.stagedconcolicminiwasm._

// This 'test file' is not intended to test functionality, but to generate compiled code for btree benchmarks
class TestBenchmarkBtree extends FunSuite {
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

  // only test concrete execution and its result
  def testFileConcreteCpp(filename: String, main: Option[String] = None, expect: Option[List[Float]] = None) = {
    val moduleInst = ModuleInstance(Parser.parseFile(filename))
    val cppFile = s"$filename.cpp"
    val exe = s"$cppFile.exe"
    WasmToCppCompiler.compileToExe(moduleInst, main, cppFile, exe, true, optimizeLevel=0, "NO_INFO", "RUN_ONCE", "USE_IMM")

    import sys.process._
    val result = s"./$exe".!!
    println(result)

    expect.map(vs => {
      val stackValues = result
        .split("Stack contents: \n")(1)
        .split("\n")
        .map(_.toFloat)
        .toList
      assert(vs == stackValues)
    })
  }

  test("compile-btree-benchmarks") { compileDirToCpp("./benchmarks/pldi2026/btree/", Some("main")) }

}
