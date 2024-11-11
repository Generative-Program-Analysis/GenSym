package gensym.wasm.miniwasmscript

import gensym.wasm.miniwasm._
import gensym.wasm.ast._
import scala.collection.mutable.{ListBuffer, Map, ArrayBuffer}

sealed class ScriptRunner {
  val instances: ListBuffer[ModuleInstance] = ListBuffer()
  val instanceMap: Map[String, ModuleInstance] = Map()

  def getInstance(instName: Option[String]): ModuleInstance = {
    instName match {
	    case Some(name) => instanceMap(name)
      case None => instances.head
    }
  }

  def assertReturn(action: Action, expect: List[Value]): Unit = {
    action match {
      case Invoke(instName, name, args) =>
        val module = getInstance(instName)
        val func = module.exports.collectFirst({
          case Export(`name`, ExportFunc(index)) =>
            module.funcs(index)
          case _ => throw new RuntimeException("Not Supported")
        }).get
        val instrs = func match {
          case FuncDef(_, FuncBodyDef(ty, _, locals, body)) => body
        }
        val k = (retStack: List[Value]) => retStack
        // TODO: change this back to Evaluator if we are just testing original stuff
        val evaluator = EvaluatorFX(module)
        val actual = evaluator.eval(instrs, List(), Frame(ArrayBuffer(args: _*)), k, List(k))
        assert(actual == expect)
    }
  }

  def runCmd(cmd: Cmd): Unit = {
    cmd match {
      case CmdModule(module) => instances += ModuleInstance(module)
      case AssertReturn(action, expect) => assertReturn(action, expect)
      case CMdInstnace() => ()
      case AssertTrap(action, message) => ???
    }
  }

  def run(script: Script): Unit = {
    for (cmd <- script.cmds) {
      runCmd(cmd)
    }
  }
}
