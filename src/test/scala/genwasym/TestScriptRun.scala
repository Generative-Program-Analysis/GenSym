package gensym.wasm

import gensym.wasm.parser.Parser
import gensym.wasm.miniwasmscript.ScriptRunner

import org.scalatest.FunSuite

class TestScriptRun extends FunSuite {
  def testFile(filename: String): Unit = {
    val script = Parser.parseScriptFile(filename).get
    val runner = new ScriptRunner()
    runner.run(script)
  }

  test("simple script") {
    testFile("./benchmarks/wasm/script/script_basic.wast")
  }

  test("simple bin script") {
    testFile("./benchmarks/wasm/script/script_basic.bin.wast")
  }

  test("f32") {
    testFile("./benchmarks/wasm/script/f32.bin.wast")
  }

}
