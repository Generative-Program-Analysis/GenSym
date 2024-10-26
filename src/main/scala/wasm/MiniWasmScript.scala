package gensym.wasm.miniwasmscript

import gensym.wasm.miniwasm._
import gensym.wasm.ast.{Script, Cmd}
import gensym.wasm.ast.AssertReturn
import gensym.wasm.ast.AssertTrap

sealed class ScriptRunner {
  val instances: List[ModuleInstance] = List()

  def runCmd(cmd: Cmd): Unit = {
    cmd match {
      case AssertReturn(action, results) =>
      case AssertTrap(action, message) =>
    }
  }

  def run(script: Script): Unit = {
    for (cmd <- script.cmds) {
      runCmd(cmd)
    }
  }
}
