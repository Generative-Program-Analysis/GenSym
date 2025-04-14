package gensym.wasm.concolicdriver

import gensym.wasm.concolicminiwasm._
import gensym.wasm.ast._
import gensym.wasm.parser._
import gensym.wasm.symbolic._

import scala.collection.immutable.Queue
import scala.collection.mutable.{HashMap, HashSet}

import z3.scala._
import scala.tools.nsc.doc.model.Val

object ConcolicDriver {
  def condsToEnv(conds: List[Cond])(implicit z3Ctx: Z3Context): Option[HashMap[Int, Value]] = {
    val intSort = z3Ctx.mkIntSort()
    val boolSort = z3Ctx.mkBoolSort()

    // gotta map locals to names

    def symVToZ3(symV: SymVal): Z3AST = symV match {
      case SymV(name) => z3Ctx.mkConst(name, intSort) // might not be an int?
      case SymBinary(op, lhs, rhs) =>
        op match {
          case Add(_) => z3Ctx.mkAdd(symVToZ3(lhs), symVToZ3(rhs)) // does numtype matter?
          case Sub(_) => z3Ctx.mkSub(symVToZ3(lhs), symVToZ3(rhs))
          case Mul(_) => z3Ctx.mkMul(symVToZ3(lhs), symVToZ3(rhs))
          case Or(_)  => 
            var result = z3Ctx.mkBVOr(
              z3Ctx.mkInt2BV(32, symVToZ3(lhs)), 
              z3Ctx.mkInt2BV(32, symVToZ3(rhs))
            )
            z3Ctx.mkBV2Int(result, false)
          case _      => throw new NotImplementedError(s"Unsupported binary operation: $op")
        }
      case SymUnary(op, v) =>
        op match {
          case _ => ???
        }
      case SymIte(cond, thenV, elseV) => z3Ctx.mkITE(condToZ3(cond), symVToZ3(thenV), symVToZ3(elseV))
      case Concrete(v) => 
        v match {
          // todo: replace with bitvector
          case I32V(i) => z3Ctx.mkInt(i, intSort)
          case I64V(i) => z3Ctx.mkNumeral(i.toString(), intSort)
          // TODO: Float
          case _       => ???
        }
      case RelCond(op, lhs, rhs) => 
        val res = op match {
          case GeS(_) => z3Ctx.mkGE(symVToZ3(lhs), symVToZ3(rhs))
          case GtS(_) => z3Ctx.mkGT(symVToZ3(lhs), symVToZ3(rhs))
          case LtS(_) => z3Ctx.mkLT(symVToZ3(lhs), symVToZ3(rhs))
          case LeS(_) => z3Ctx.mkLE(symVToZ3(lhs), symVToZ3(rhs))
          case GtU(_) => z3Ctx.mkGT(symVToZ3(lhs), symVToZ3(rhs))
          case GeU(_) => z3Ctx.mkGE(symVToZ3(lhs), symVToZ3(rhs))
          case LtU(_) => z3Ctx.mkLT(symVToZ3(lhs), symVToZ3(rhs))
          case LeU(_) => z3Ctx.mkLE(symVToZ3(lhs), symVToZ3(rhs))
          case Eq(_)  => z3Ctx.mkEq(symVToZ3(lhs), symVToZ3(rhs))
          case Ne(_)  => z3Ctx.mkNot(z3Ctx.mkEq(symVToZ3(lhs), symVToZ3(rhs)))
          case Ge(_)  => z3Ctx.mkGE(symVToZ3(lhs), symVToZ3(rhs))
          case Gt(_)  => z3Ctx.mkGT(symVToZ3(lhs), symVToZ3(rhs))
          case Le(_)  => z3Ctx.mkLE(symVToZ3(lhs), symVToZ3(rhs))
          case Lt(_)  => z3Ctx.mkLT(symVToZ3(lhs), symVToZ3(rhs))
        }
        // convert resutl to int
        z3Ctx.mkITE(res, z3Ctx.mkInt(1, intSort), z3Ctx.mkInt(0, intSort))
      case _                          => throw new NotImplementedError(s"Unsupported SymVal: $symV")
    }

    def getIndexOfSym(sym: String): Int = {
      val pattern = ".*_(\\d+)$".r
      sym match {
        case pattern(index) => index.toInt
        case _              => throw new IllegalArgumentException(s"Invalid symbol format: $sym")
      }
    }

    def condToZ3(cond: Cond): Z3AST = cond match {
      case CondEqz(v) => z3Ctx.mkEq(symVToZ3(v), z3Ctx.mkInt(0, intSort))
      case Not(cond)  => z3Ctx.mkNot(condToZ3(cond))
      case RelCond(op, lhs, rhs) =>
        op match {
          case _ => ???
        }
    }

    val solver = z3Ctx.mkSolver()
    for (cond <- conds) {
      solver.assertCnstr(condToZ3(cond))
    }

    // solve for all vars
    println(s"solving constrains: ${solver.toString()}")
    solver.check() match {
      case Some(true) => {
        val model = solver.getModel()
        val vars = model.getConsts
        val env = HashMap[Int, Value]()
        for (v <- vars) {
          val name = v.getName
          val ast = z3Ctx.mkConst(name, intSort)
          val value = model.eval(ast)
          println(s"name: $name")
          println(s"value: $value")
          // TODO: support other types of symbolic values(currently only i32)
          val intValue = if (value.isDefined && value.get.getSort.isIntSort) {
            val negPattern = """\(\-\s*(\d+)\)""".r
            val plainPattern = """(-?\d+)""".r
            val num = value.get.toString match {
              case negPattern(digits) => -digits.toInt
              case plainPattern(number)  => number.toInt
              case _ => throw new IllegalArgumentException("Invalid format")
            }
            I32V(num)
          } else {
            ???
          }
          env += (getIndexOfSym(name.toString) -> intValue)
        }
        println(s"solved env: $env")
        Some(env)
      }
      case _ => None
    }
  }

  def negateCond(conds: List[Cond], i: Int): List[Cond] = {
    conds(i).negated :: conds.drop(i + 1)
  }

  def checkPCToFile(pc: List[Cond]): Unit = {
    // TODO: what this function for?
    ???
  }

  def exec(module: Module, mainFun: String, startEnv: HashMap[Int, Value])(implicit z3Ctx: Z3Context) = {
    val worklist = Queue(startEnv)
    val unreachables = HashSet[ExploreTree]()
    val visited = HashSet[ExploreTree]()
    // the root node of exploration tree
    val root = new ExploreTree()
    def loop(worklist: Queue[HashMap[Int, Value]]): Unit = worklist match {
      case Queue() => ()
      case env +: rest => {
        val moduleInst = ModuleInstance(module)
        Evaluator(moduleInst).execWholeProgram(
          Some(mainFun),
          env,
          root,
          (_endStack, _endSymStack, tree) => {
            tree.fillWithFinished()
            val unexploredTrees = root.unexploredTrees()
            // if a node is already visited or marked as unreachable, don't try to explore it
            val addedNewWork = unexploredTrees.filterNot(unreachables.contains)
                                              .filterNot(visited.contains)
                                              .flatMap { tree =>
              val conds = tree.collectConds()
              val newEnv = condsToEnv(conds)
              // if the path conditions to reach this node are unsatisfiable, mark it as unreachable.
              if (newEnv.isEmpty) unreachables.add(tree)
              newEnv
            }
            for (tree <- unexploredTrees) {
              visited.add(tree)
            }
            loop(rest ++ addedNewWork)
          }
        )
      }
    }

    loop(worklist)
    println(s"unreachable trees number: ${unreachables.size}")
    println(s"number of normal explored paths: ${root.finishedTrees().size}")
    val failedTrees = root.failedTrees()
    println(s"number of failed explored paths: ${failedTrees.size}")
    for (tree <- failedTrees) {
      println(s"find a failed endpoint: ${tree}")
    }
    println(s"exploration tree: ${root.toString}")
  }
}

object DriverSimpleTest {
  def fileTestDriver(file: String, mainFun: String, startEnv: HashMap[Int, Value]) = {
    import gensym.wasm.concolicminiwasm._
    import collection.mutable.ArrayBuffer
    val module = Parser.parseFile(file)
    ConcolicDriver.exec(module, mainFun, startEnv)(new Z3Context())
  }

  def main(args: Array[String]) = {}
}
