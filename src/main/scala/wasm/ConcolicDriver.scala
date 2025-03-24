package gensym.wasm.concolicdriver

import gensym.wasm.concolicminiwasm._
import gensym.wasm.ast._
import gensym.wasm.parser._
import gensym.wasm.symbolic._

import scala.collection.immutable.Queue
import scala.collection.mutable.{HashMap, HashSet}

import z3.scala._

object ConcolicDriver {
  def condsToEnv(conds: List[Cond])(implicit z3Ctx: Z3Context): HashMap[Int, Value] = {
    val intSort = z3Ctx.mkIntSort()
    val boolSort = z3Ctx.mkBoolSort()

    // gotta map locals to names

    def symVToZ3(symV: SymVal): Z3AST = symV match {
      case SymV(name) => z3Ctx.mkConst(name, intSort) // might not be an int?
      case SymBinary(op, lhs, rhs) => op match {
        case Add(_) => z3Ctx.mkAdd(symVToZ3(lhs), symVToZ3(rhs)) // does numtype matter?
        case Sub(_) => z3Ctx.mkSub(symVToZ3(lhs), symVToZ3(rhs))
        case Mul(_) => z3Ctx.mkMul(symVToZ3(lhs), symVToZ3(rhs))
        case _ => ???
      }
      case SymUnary(op, v) => op match {
        case _ => ???
      }
      case SymIte(cond, thenV, elseV) => z3Ctx.mkITE(condToZ3(cond), symVToZ3(thenV), symVToZ3(elseV))
      case Concrete(v) => ???
      case _ => ???
    }
    def condToZ3(cond: Cond): Z3AST = cond match {
      case CondEqz(v) => z3Ctx.mkEq(symVToZ3(v), z3Ctx.mkInt(0, intSort))
      case Not(cond) => z3Ctx.mkNot(condToZ3(cond))
      case RelCond(op, lhs, rhs) => op match {
        case _ => ???
      }
    }

    val solver = z3Ctx.mkSolver()
    for (cond <- conds) {
      solver.assertCnstr(condToZ3(cond))
    }

    // solve for all vars
    solver.check() match {
      case Some(true) => {
        val model = solver.getModel()
        val vars = model.getConsts
        val env = HashMap()
        for (v <- vars) {
          val name = v.getName
          val ast = z3Ctx.mkConst(name, intSort)
          val value = model.eval(ast)
          val intValue = if (value.isDefined && value.get.getSort.isIntSort) {
            I32V(value.toString.toInt)
          } else {
            ???
          }
          // env += (name.toString -> intValue)
          ???
        }
        ???
        // env
      }
      case _ => ???
    }
  }

  def negateCond(conds: List[Cond], i: Int): List[Cond] = {
    ???
  }

  def checkPCToFile(pc: List[Cond]): Unit = {
    ???
  }

  def exec(module: Module, mainFun: String, startEnv: HashMap[Int, Value])(implicit z3Ctx: Z3Context) = {
    val worklist = Queue(startEnv)
    // val visited = ??? // how to avoid re-execution

    def loop(worklist: Queue[HashMap[Int, Value]]): Unit = worklist match {
      case Queue() => ()
      case env +: rest => {
        val moduleInst = ModuleInstance(module)
        Evaluator(moduleInst).execWholeProgram(Some(mainFun), env, (_endStack, _endSymStack, pathConds) => {
          println(s"env: $env")
          val newEnv = condsToEnv(pathConds)
          val newWork = for (i <- 0 until pathConds.length) yield {
            val newConds = negateCond(pathConds, i)
            checkPCToFile(newConds)
            condsToEnv(newConds)
          }
          loop(rest ++ newWork)
        })
      }
    }

    loop(worklist)
  }
}

object DriverSimpleTest {
  def fileTestDriver(file: String, mainFun: String, startEnv: HashMap[Int, Value]) = {
    import gensym.wasm.concolicminiwasm._
    import collection.mutable.ArrayBuffer
    val module = Parser.parseFile(file)
    ConcolicDriver.exec(module, mainFun, startEnv)(new Z3Context())
  }

  def main(args: Array[String]) = {
  }
}
