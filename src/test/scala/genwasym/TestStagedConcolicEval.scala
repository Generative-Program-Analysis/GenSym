package gensym.wasm

import org.scalatest.FunSuite

import lms.core.stub.Adapter

import gensym.wasm.miniwasm.{ModuleInstance}
import gensym.wasm.parser._
import gensym.wasm.stagedconcolicminiwasm._

class TestStagedConcolicEval extends FunSuite {
  def testFileToCpp(filename: String, main: Option[String] = None, expect: Option[List[Float]]=None) = {
    val moduleInst = ModuleInstance(Parser.parseFile(filename))
    val cppFile = s"$filename.cpp"
    val exe = s"$cppFile.exe"
    val exploreTreeFile = s"$filename.tree.dot"
    WasmToCppCompiler.compileToExe(moduleInst, main, cppFile, exe, true, Some(exploreTreeFile))

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

  test("ack-cpp") { testFileToCpp("./benchmarks/wasm/ack.wat", Some("real_main"), expect=Some(List(7))) }

  test("bug-finding") {
    testFileToCpp("./benchmarks/wasm/branch-strip-buggy.wat", Some("real_main"))
  }

  test("brtable-bug-finding") {
    testFileToCpp("./benchmarks/wasm/staged/brtable_concolic.wat")
  }
}
