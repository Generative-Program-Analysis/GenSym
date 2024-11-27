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
      case None       => instances.head
    }
  }

  def assertReturn(action: Action, expect: List[Value]): Unit = {
    action match {
      case Invoke(instName, name, args) =>
        val module = getInstance(instName)
        val func = module.exports
          .collectFirst({
            case Export(`name`, ExportFunc(index)) =>
              module.funcs(index)
            case _ => throw new RuntimeException("Not Supported")
          })
          .get
        val instrs = func match {
          case FuncDef(_, FuncBodyDef(ty, _, locals, body)) => body
        }
        val evaluator = EvaluatorFX(module)
        type Cont = evaluator.Cont[evaluator.Stack]
        type MCont = evaluator.MCont[evaluator.Stack]
        type Handler = evaluator.Handler[evaluator.Stack]
        val k: Cont = (retStack, m) => m(retStack)
        val mk: MCont = (retStack) => retStack
        val h0: Handler = stack => throw new Exception(s"Uncaught exception: $stack")
        // TODO: change this back to Evaluator if we are just testing original stuff
        val actual = evaluator.eval(instrs, List(), Frame(ArrayBuffer(args: _*)), k, mk, List(k), h0)
        println(s"expect = $expect")
        println(s"actual = $actual")
        assert(actual == expect)
    }
  }

  def runCmd(cmd: Cmd): Unit = {
    cmd match {
      case CmdModule(module)            => instances += ModuleInstance(module)
      case AssertReturn(action, expect) => assertReturn(action, expect)
      case CMdInstnace()                => ()
      case AssertTrap(action, message)  => ???
    }
  }

  def run(script: Script): Unit = {
    for (cmd <- script.cmds) {
      runCmd(cmd)
    }
  }
}
