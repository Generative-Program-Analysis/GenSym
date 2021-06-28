package sai.simpil

import sai.lang.SimpIL
import sai.lang.SimpIL._
import z3.scala._

object ConcolicV {
  trait Value
  type SExp = Z3AST
  case class IntV(x: Int) extends Value
  case class SymV(e: SExp) extends Value
  
  def toNegSExp(e: SExp, z3: Z3Context) = {
    z3.mkNot(e)
  }

  var i: Int = 0
  def fresh: String = {
    val j = i
    i = i + 1
    s"x$j"
  }
}

// Problems so far:
// 1. how should we handle input?

object Concolic {
  import ConcolicV._

  type PC = Int
  type Addr = Int
  type PCond = List[SExp]
  type Store = Map[Addr, Value]
  type Env = Map[String, Value]
  type Prg = Map[PC, Stmt]
  // Input._5 is "bound" defined in the NDSS paper
  type Input = (Env, Store, Set[String], Set[Addr], Int)
  type SInput = (Env, Store, Env, Store, Int)
  type Result = (Env, Store, PCond)

  // Δ concrete env, μ concrete store
  // γ symbolic env, η symbolic store
  def exec(s: Stmt, Δ: Env, μ: Store, Π: PCond, pc: PC, Σ: Prg, γ: Env, η: Store, z3: Z3Context): Result = s match {
    case Assign(x, e) => 
      val v = eval(e, Δ, μ)
      val Δ_* = Δ + (x → v)
      val sv = seval(e, Δ, μ, γ, η, z3)
      val γ_* = sv match {
        case IntV(x) => γ
        case SymV(e) => γ + (x → sv)
      }
      exec(Σ(pc+1), Δ_*, μ, Π, pc+1, Σ, γ_*, η, z3)
    case Store(e1, e2) =>
      val v1 = eval(e1, Δ, μ)
      val v2 = eval(e2, Δ, μ)
      val sv2 = seval(e2, Δ, μ, γ, η, z3)
      val η_* = sv2 match {
        case IntV(x) => η
        case SymV(e) => v1 match {
          case IntV(α) => η + (α → sv2)
        }
      }
      val μ_* = v1 match {
        case IntV(α) => μ + (α → v2)
      }
      exec(Σ(pc+1), Δ, μ_*, Π, pc+1, Σ, γ, η_*, z3)
    case Goto(e) => eval(e, Δ, μ) match {
      case IntV(ℓ) => exec(Σ(ℓ), Δ, μ, Π, ℓ, Σ, γ, η, z3)
    } 
    case Assert(e) => eval(e, Δ, μ) match {
      case IntV(1) => exec(Σ(pc+1), Δ, μ, Π, pc+1, Σ, γ, η, z3)
      case IntV(_) => (Map(), Map(), List(z3.mkFalse()))
    }
    case Cond(cnd, t1, t2) => 
      val scnd = seval(cnd, Δ, μ, γ, η, z3)
      val pcnd = scnd match {
        case IntV(x) => z3.mkTrue
        case SymV(e) => e
      }
      eval(cnd, Δ, μ) match {
        case IntV(1) => eval(t1, Δ, μ) match {
          case IntV(ℓ) => exec(Σ(ℓ), Δ, μ, Π :+ pcnd, ℓ, Σ, γ, η, z3)
        }
        case IntV(0) => eval(t2, Δ, μ) match {
          case IntV(ℓ) => exec(Σ(ℓ), Δ, μ, Π :+ z3.mkNot(pcnd), ℓ, Σ, γ, η, z3)
        }
      }
    case Halt(e) => (Δ, μ, Π)
    case Output(str) => println(str); exec(Σ(pc+1), Δ, μ, Π, pc+1, Σ, γ, η, z3)
  }

  // The reason to have a seperate seval is:
  // Our program can always execute concrete execution
  def seval(e: Exp, Δ: Env, μ: Store, γ: Env, η: Store, z3: Z3Context): Value = e match {
    case Lit(x) => IntV(x)
    case Var(x) =>
      if (γ.contains(x)) γ(x) else Δ(x)
    case Load(e) => eval(e, Δ, μ) match {
      case IntV(α) => 
        if (η.contains(α)) η(α) else μ(α)
    }
    case BinOp(op, e1, e2) => 
      val s1 = seval(e1, Δ, μ, γ, η, z3)
      val s2 = seval(e2, Δ, μ, γ, η, z3)
      (s1, s2) match {
        case (IntV(v1), IntV(v2)) => evalBinOp(op, v1, v2)
        case (IntV(v1), SymV(e1)) => sevalBinOp(op, z3.mkInt(v1, z3.mkIntSort), e1, z3)
        case (SymV(e1), IntV(v1)) => sevalBinOp(op, e1, z3.mkInt(v1, z3.mkIntSort), z3)
        case (SymV(e1), SymV(e2)) => sevalBinOp(op, e1, e2, z3)
      }
    case UnaryOp(op, e) => 
      val s1 = seval(e, Δ, μ, γ, η, z3)
      s1 match {
        case IntV(x) => evalUnaryOp(op, x)
        case SymV(e) => sevalUnaryOp(op, e, z3)
      }
    case GetInput(src) => ???
  }

  def eval(e: Exp, Δ: Env, μ: Store): Value = e match {
    case Lit(i) => IntV(i)
    case Var(x) => Δ(x)
    case Load(e) => eval(e, Δ, μ) match {
      case IntV(α) => μ(α)
    }
    case BinOp(op, e1, e2) => (eval(e1, Δ, μ), eval(e2, Δ, μ)) match {
      case (IntV(v1), IntV(v2)) => evalBinOp(op, v1, v2)
    }
    case UnaryOp(op, e) => eval(e, Δ, μ) match { case IntV(v) => evalUnaryOp(op, v) }
    case GetInput("stdin") => IntV(scala.io.StdIn.readInt)
    case _ => throw new RuntimeException("not an expression")
  }

  def sevalBinOp(op: String, v1: SExp, v2: SExp, z3: Z3Context): Value = op match {
    case "+" => SymV(z3.mkAdd(v1, v2))
    case "-" => SymV(z3.mkSub(v1, v2))
    case "*" => SymV(z3.mkMul(v1, v2))
    case "/" => SymV(z3.mkDiv(v1, v2))
    case "==" => SymV(z3.mkEq(v1, v2))
    case "!=" => SymV(z3.mkNot(z3.mkEq(v1, v2)))
    case ">"  => SymV(z3.mkGT(v1, v2))
    case ">=" => SymV(z3.mkGE(v1, v2))
    case "<"  => SymV(z3.mkLT(v1, v2))
    case "<=" => SymV(z3.mkLE(v1, v2))
  }

  def evalBinOp(op: String, v1: Int, v2: Int): Value = op match {
    case "+" => IntV(v1 + v2)
    case "-" => IntV(v1 - v2)
    case "*" => IntV(v1 * v2)
    case "/" => IntV(v1 / v2)
    case "==" => if (v1 == v2) IntV(1) else IntV(0)
    case "!=" => if (v1 != v2) IntV(1) else IntV(0)
    case ">"  => if (v1 >  v2) IntV(1) else IntV(0)
    case ">=" => if (v1 >= v2) IntV(1) else IntV(0)
    case "<"  => if (v1 <  v2) IntV(1) else IntV(0)
    case "<=" => if (v1 <= v2) IntV(1) else IntV(0)
  }

  def evalUnaryOp(op: String, v: Int): Value = op match {
    case "~" => IntV(-v)
  }

  def sevalUnaryOp(op: String, e: SExp, z3: Z3Context): Value = op match {
    case "~" => SymV(z3.mkSub(z3.mkInt(0, z3.mkIntSort), e))
  }

  // see https://patricegodefroid.github.io/public_psfiles/ndss2008.pdf
  def runConcolic(p: Prog, input: Option[Input]): Set[Input] = {
    val (δ, μ, γ, η, _) = input match {
      case None => gen_initial_state(p)
      case Some(i) => i
    } 
    var tests = Set[Input]((δ, μ, γ, η, 0))
    val workList = scala.collection.mutable.Set[Input]((δ, μ, γ, η, 0))
    var i = 1
    while (workList.nonEmpty) {
      val input = workList.head
      workList.remove(input)
      println(s"Starting Iteration $i, input is $input")
      i += 1
      val childInputs = expandExec(p, input)
      childInputs.foreach(workList.add(_))
      tests = tests ++ childInputs
    }
    tests
  }

  def expandExec(p: Prog, input: Input): Set[Input] = {
    val z3 = new Z3Context()
    var newinputs = Set[Option[Input]]()
    val (δ, μ, γ_i, η_i, bound) = input
    val γ = γ_i.map(s => (s, SymV(z3.mkIntConst(s)))).toMap
    val η = η_i.map(a => (a, SymV(z3.mkIntConst(a.toString)))).toMap
    var (_, _, π) = p match {
      case Prog(stmts) => exec(stmts(0), δ, μ, List(), 0, 
        stmts.zipWithIndex.map(_.swap).toMap, γ, η, z3)
    }
    for (j <- bound until π.length) {
      val π_prime = π.take(j) :+ toNegSExp(π(j), z3)
      println(π_prime)
      newinputs += gen_new_input((δ, μ, γ, η, j + 1), π_prime, z3) 
    }
    newinputs.flatten
  }

  // This seems to be hard in this language
  def gen_initial_state(p: Prog): Input = {
    (Map(), Map(), Set(), Set(), 0)
  }

  def gen_new_input(input: SInput, π: PCond, z3: Z3Context): Option[Input] = {
    val (δ, μ, γ, η, bound) = input
    val solver = z3.mkSolver
    π.foreach(e => solver.assertCnstr(e))
    val result = solver.check
    result match {
      case None => None
      case Some(false) => None
      case Some(true) => {
        val model = solver.getModel
        val γ_concretize = γ.map { case ((s, v)) => (s, model.evalAs[Int](v.asInstanceOf[SymV].e)) }
        val δ_updated = δ.map { case ((s, v)) => {
            if (γ_concretize.contains(s)) γ_concretize(s) match {
              case Some(i) => (s, IntV(i))
              case None => (s, v)
            } else (s, v)
          }
        }
        val η_concretize = η.map { case ((s, v)) => (s, model.evalAs[Int](v.asInstanceOf[SymV].e)) }
        val μ_updated = μ.map { case ((s, v)) => {
            if (η_concretize.contains(s)) η_concretize(s) match {
              case Some(i) => (s, IntV(i))
              case None => (s, v)
            } else (s, v)
          }
        }
        Some((δ_updated, μ_updated, γ.keys.toSet, η.keys.toSet, bound))
      }
    }
  }
}

object TestConcolic extends App {
  import Concolic._, ConcolicV._
  val ex1 = Prog(List(
      Assign("x", Lit(5)),
      Cond(BinOp("<", Var("x"), Var("y")),
        Lit(2),
        Lit(4)),
      Output("x < y"),
      Halt(Var("x")),
      Output("x >= y"),
      Cond(BinOp("<", Var("x"), BinOp("*", Lit(2), Var("y"))),
        Lit(6),
        Lit(8)),
      Output("x < 2* y"),
      Halt(Var("x")),
      Output("x >= 2 * y"),
      Halt(Var("x"))
    )
  )

  runConcolic(ex1, Some((Map(("y" -> IntV(8))), Map(), Set("y"), Set(), 0)))
}

object ScalaZ3Test extends App {
  def main() {
    val z3 = new Z3Context()
    val intSort = z3.mkIntSort
    val i = z3.mkInt(1, intSort)
    val k = z3.mkIntConst("k")
    val solver = z3.mkSolver
    solver.assertCnstr(z3.mkEq(i, k))
    val check = solver.check
    val model = solver.getModel
    Z3NumeralIntAST(Some(1))
    println(model.evalAs[Int](i))
    println(z3.modelToString(model))
    println("finished")
  }

  main()
}