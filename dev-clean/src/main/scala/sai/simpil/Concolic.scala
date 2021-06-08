package sai.simpil

import sai.lang.SimpIL
import sai.lang.SimpIL._

object Concolic {
  trait Value
  case class IntV(x: Int) extends Value {
    override def toString() = x.toString
  }
  trait SValue
  case class SymV(s: SExp) extends SValue {
    override def toString() = s.toString
  }
  implicit class SValueOps(v: SValue) {
    def neg: SValue = ???
  }

  var i: Int = 0
  def fresh: String = {
    val j = i
    i = i + 1
    s"x$j"
  }

  type SExp = String
  type PC = Int
  type Addr = Int
  type PCond = List[SValue]
  type Store = Map[Addr, Value]
  type Env = Map[String, Value]
  type SStore = Map[Addr, SValue]
  type SEnv = Map[String, SValue]
  type Prg = Map[PC, Stmt]
  type Input = (Env, Store, SEnv, SStore)
  type Result = (Env, Store, PCond)

  def exec(s: Stmt, Δ: Env, μ: Store, Π: PCond, pc: PC, Σ: Prg, γ: SEnv, η: SStore): Result = s match {
    case Assign(x, e) => 
      val v = eval(e, Δ, μ)
      val Δ_* = Δ + (x → v)
      exec(Σ(pc+1), Δ_*, μ, Π, pc+1, Σ, γ, η)
    case Store(e1, e2) =>
      val v1 = eval(e1, Δ, μ)
      val v2 = eval(e2, Δ, μ)
      // have to update symbolic state as well
      val μ_* = v1 match {
        case IntV(α) => μ + (α → v2)
      }
      exec(Σ(pc+1), Δ, μ_*, Π, pc+1, Σ, γ, η)
    case Goto(e) => eval(e, Δ, μ) match {
      case IntV(ℓ) => exec(Σ(ℓ), Δ, μ, Π, ℓ, Σ, γ, η)
    } 
    case Assert(e) => eval(e, Δ, μ) match {
      case IntV(1) => exec(Σ(pc+1), Δ, μ, Π, pc+1, Σ, γ, η)
      case IntV(_) => (Map(), Map(), List(SymV("")))
    }
    case Cond(cnd, t1, t2) => 
      val scnd = seval(cnd, Δ, μ, γ, η)
      eval(cnd, Δ, μ) match {
      case IntV(1) => eval(t1, Δ, μ) match {
        case IntV(ℓ) => exec(Σ(ℓ), Δ, μ, Π :+ scnd, ℓ, Σ, γ, η)
      }
      case IntV(0) => eval(t2, Δ, μ) match {
        case IntV(ℓ) => exec(Σ(ℓ), Δ, μ, Π :+ scnd, ℓ, Σ, γ, η)
      }
    }
    case Halt(e) => (Δ, μ, Π)
  }

  def seval(e: Exp, Δ: Env, μ: Store, γ: SEnv, η: SStore): SValue = e match {
    case Lit(x) => SymV(x.toString)
    case Var(x) =>
      if (γ.contains(x)) γ(x) else SymV(Δ(x).toString())
    case Load(e) => eval(e, Δ, μ) match {
      case IntV(α) => 
        if (η.contains(α)) η(α) else SymV(μ(α).toString())
    }
    case BinOp(op, e1, e2) => 
      val s1 = seval(e1, Δ, μ, γ, η).toString()
      val s2 = seval(e2, Δ, μ, γ, η).toString()
      SymV(s"($s1 $op $s2)")
    case UnaryOp(op, e) => 
      val s1 = seval(e, Δ, μ, γ, η).toString()
      SymV(s"($op $s1)")
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

  // see https://patricegodefroid.github.io/public_psfiles/ndss2008.pdf
  def runConcolic(p: Prog): Set[Input] = {
    val (δ, μ, γ, η) = gen_initial_state(p)
    var tests = Set[Input]((δ, μ, γ, η))
    val workList = scala.collection.mutable.Set[Input]((δ, μ, γ, η))
    while (workList.nonEmpty) {
      val input = workList.head
      workList.remove(input)
      val childInputs = expandExec(p, input)
      tests = tests ++ childInputs
    }
    tests
  }

  def expandExec(p: Prog, i: Input): Set[Input] = {
    var pconds = Set[PCond]()
    val (δ, μ, γ, η) = i
    val (_, _, π) = p match {
      case Prog(stmts) => exec(stmts(0), δ, μ, List(), 0, 
        stmts.zipWithIndex.map(_.swap).toMap, γ, η)
    }
    for (j <- 1 until π.length) {
      val π_prime = π.take(j-1) :+ π(j).neg
      pconds += π_prime
    }
    pconds.flatMap(gen_new_input(_))
  }

  def gen_initial_state(p: Prog): (Env, Store, SEnv, SStore) = {
    (Map(), Map(), Map(), Map())
  }

  def gen_new_input(π: PCond): Option[Input] = {
    // invoke solver on π
    ???
  }
}