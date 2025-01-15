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
        val actual = invoke(instName, name, args)
        println(s"expect = $expect")
        println(s"actual = $actual")
        assert(actual == expect)
    }
  }

  def invoke(instName: Option[String], name: String, args: List[Value]): Seq[Value] = {
    val module = getInstance(instName)
    val func = module.exports
      .collectFirst({
        case Export(`name`, ExportFunc(index)) =>
          System.err.println(s"Export: $name")
          module.funcs(index)
      })
      .get
    val instrs = func match {
      case FuncDef(_, FuncBodyDef(ty, _, locals, body)) => body
    }
    val evaluator = EvaluatorFX(module)
    type Cont = evaluator.Cont[evaluator.Stack]
    type MCont = evaluator.MCont[evaluator.Stack]
    type Handler = evaluator.Handler[evaluator.Stack]
    val k: Cont = evaluator.initK
    val halt: Cont = (retStack, _, _) => retStack
    // Note: change this back to Evaluator if we are just testing original stuff
    evaluator.evalList(instrs, List(), Frame(ArrayBuffer(args: _*)), k, List((halt, List())), List(k), List())
  }

  def runCmd(cmd: Cmd): Unit = {
    cmd match {
      case CmdModule(module)            => instances += ModuleInstance(module)
      case AssertReturn(action, expect) => assertReturn(action, expect)
      case CMdInstnace()                => ()
      case Invoke(instName, name, args) => invoke(instName, name, args)
      case AssertTrap(action, message)  => ???
    }
  }

  def run(script: Script): Unit = {
    for (cmd <- script.cmds) {
      runCmd(cmd)
    }
  }
}
