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
    // val visited = ??? // how to avoid re-execution
    var pathCount = 0 // TODO: replace this with accurate path exploration
    val visited = new java.util.IdentityHashMap[ExploreTree, Unit]()
    val root = new ExploreTree()
    def collectUnexploredTrees(tree: ExploreTree): List[ExploreTree] = {
      tree.node match {
        case UnExplored() => List(tree)
        case IfElse(_, thenNode, elseNode) =>
          collectUnexploredTrees(thenNode) ++ collectUnexploredTrees(elseNode)
        case _ => Nil
      }
    }
    def loop(worklist: Queue[HashMap[Int, Value]]): Unit = worklist match {
      case Queue() => ()
      case env +: rest => {
        val moduleInst = ModuleInstance(module)
        Evaluator(moduleInst).execWholeProgram(
          Some(mainFun),
          env,
          root,
          (_endStack, _endSymStack, tree) => {
            println(s"env: $env")
            println(s"visited: $visited")
            // TODO: use a clever way to avoid re-iteration of the whole tree
            val unexploredTrees = collectUnexploredTrees(root)
            val addedNewWork = unexploredTrees.filterNot(visited.keySet().contains).flatMap { tree =>
              val conds = tree.collectConds()
              val newEnv = condsToEnv(conds)
              newEnv match {
                case Some(env) => {
                  visited.put(tree, ())
                  Some(env)
                }
                case None => None
              }
            }
            loop(rest ++ addedNewWork)
          }
        )
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

  def main(args: Array[String]) = {}
}
