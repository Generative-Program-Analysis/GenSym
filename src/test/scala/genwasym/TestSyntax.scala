package gensym.wasm

import gensym.wasm.parser.Parser
import org.scalatest.FunSuite

class TestSyntax extends FunSuite {
  def testFile(filename: String) = {
    val script = Parser.parseScriptFile(filename)
    println(s"script = $script")
    assert(script != None, "this syntax is not defined in antlr grammar")
  }

  test("basic script") {
    testFile("./benchmarks/wasm/script/script_basic.wabt")
  }
}

