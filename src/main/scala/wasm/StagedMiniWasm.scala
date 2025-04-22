package gensym.wasm.miniwasm

import scala.collection.mutable.ArrayBuffer

import lms.core.stub.Adapter
import lms.core.virtualize
import lms.core.stub.Base
import lms.core.Backend.{Block => LMSBlock}

import gensym.wasm.ast._
import gensym.wasm.ast.{Const => ConstInstr}

case class StagedEvaluator(module: ModuleInstance) extends Base {
  // reset and initialize the internal state of Adapter
  Adapter.resetState
  Adapter.g = Adapter.mkGraphBuilder

  type Stack = Rep[List[Value]]
  type Cont[A] = Stack => Rep[A]
  type Trail[A] = List[Cont[A]]

  // Ans should be instantiated to something like Int, Unit, etc, which is the result type of staged program
  def eval[Ans](insts: List[Instr],
                stack: Stack,
                frame: Rep[Frame],
                kont: Cont[Ans],
                trail: Trail[Ans]): Rep[Ans] = {
      if (insts.isEmpty) return kont(stack)
      val (inst, rest) = (insts.head, insts.tail)
      inst match {
        case Drop => eval(rest, stack.tail, frame, kont, trail)
        // Why this cons operation compiled? does anything could be casted to Rep?
        case ConstInstr(num) => eval(rest, num :: stack, frame, kont, trail)
        case _ => "todo-op".reflectWith()
      }
  }

  def evalTop[Ans](kont: Cont[Ans], main: Option[String]): Rep[Ans] = {
    val funBody: FuncBodyDef = main match {
      case Some(func_name) =>
        module.defs.flatMap({
          case Export(`func_name`, ExportFunc(fid)) =>
            println(s"Entering function $main")
            module.funcs(fid) match {
              case FuncDef(_, body@FuncBodyDef(_,_,_,_)) => Some(body)
              case _ => throw new Exception("Entry function has no concrete body")
            }
          case _ => None
        }).head
      case None => 
        val startIds = module.defs.flatMap {
            case Start(id) => Some(id)
            case _ => None
        }
        val startId = startIds.headOption.getOrElse { throw new Exception("No start function") }
        module.funcs(startId) match {
          case FuncDef(_, body@FuncBodyDef(_,_,_,_)) => body
          case _ =>
            throw new Exception("Entry function has no concrete body")
        }
    }
    val (instrs, localSize) = (funBody.body, funBody.locals.size)
    val frame = Frame(ArrayBuffer.fill(localSize)(I32V(0)))
    eval(instrs, List(), frame, kont, List(kont))
  }

  def evalTop(main: Option[String]): Rep[Unit] = {
    val haltK: Cont[Unit] = stack => {
      "no-op".reflectWith()
    }
    evalTop(haltK, main)
  }

  def codegen(main: Option[String]): LMSBlock = {
    Adapter.g.reify( { Unwrap(evalTop(main)) } )
  }

  // The stack should be allocated on the stack to get optimal performance
  implicit class StackOps(stack: Stack) {
    def tail(): Stack = {
      "value-stack-tail".reflectWith(stack)
    }

    def ::[A](v: Rep[A]): Stack = {
      "value-stack-cons".reflectWith(v, stack)
    }
  }

  // directly specify the translated operation
  implicit class StringOps(op: String) {
    def reflectWith[T: Manifest](rs: Rep[_]*): Rep[T] = {
      val result = rs.map(Unwrap)
      Predef.println(s"reflectWith: $op, $result")
      val result1 = Adapter.g.reflect(op, result:_*)
      Wrap[T](result1)
    }
  }
}
