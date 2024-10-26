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
    testFile("./benchmarks/wasm/script/script_basic.wabt")
  }
}
