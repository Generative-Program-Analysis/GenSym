package gensym.wasm

import org.scalatest.FunSuite

import lms.core.stub.Adapter

import gensym.wasm.miniwasm.{ModuleInstance}
import gensym.wasm.parser._
import gensym.wasm.stagedconcolicminiwasm._

class TestStagedConcolicEval extends FunSuite {
  def testFileConcolicCpp(filename: String, main: Option[String] = None) = {
    val moduleInst = ModuleInstance(Parser.parseFile(filename))
    val cppFile = s"$filename.cpp"
    val exe = s"$cppFile.exe"
    val exploreTreeFile = s"$filename.tree.dot"
    WasmToCppCompiler.compileToExe(moduleInst, main, cppFile, exe, true)

    import sys.process._
    val result = Process(s"./$exe", None, "TREE_FILE" -> exploreTreeFile).!!
    println(result)
  }

  // only test concrete execution and its result
  def testFileConcreteCpp(filename: String, main: Option[String] = None, expect: Option[List[Float]] = None) = {
    val moduleInst = ModuleInstance(Parser.parseFile(filename))
    val cppFile = s"$filename.cpp"
    val exe = s"$cppFile.exe"
    WasmToCppCompiler.compileToExe(moduleInst, main, cppFile, exe, true, "NO_INFO")

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

  test("ack-cpp") { testFileConcolicCpp("./benchmarks/wasm/ack.wat", Some("real_main")) }

  test("bug-finding") {
    testFileConcolicCpp("./benchmarks/wasm/branch-strip-buggy.wat", Some("real_main"))
  }

  test("brtable-bug-finding") {
    testFileConcolicCpp("./benchmarks/wasm/staged/brtable_concolic.wat")
  }

  test("return - concrete") {
    testFileConcreteCpp("./benchmarks/wasm/staged/return_poly.wat", Some("$real_main"), expect=Some(List(42)))
  }
}
