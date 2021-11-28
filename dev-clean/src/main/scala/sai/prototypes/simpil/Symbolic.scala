package sai.simpil

import sai.lang.SimpIL
import sai.lang.SimpIL._

object Concrete {
  import SimpIL.Values._

  type PC = Int
  type Addr = Int
  type Store = Map[Addr, Value]
  type Env = Map[String, Value]
  type Prg = Map[PC, Stmt]

  def run(p: Prog): (Env, Store) = p match {
    case Prog(stmts) => exec(stmts(0), Map(), Map(), 0, stmts.zipWithIndex.map(_.swap).toMap)
  }

  def exec(s: Stmt, Δ: Env, μ: Store, pc: PC, Σ: Prg): (Env, Store) = s match {
    case Assign(x, e) =>
      val v = eval(e, Δ, μ)
      val Δ_* = Δ + (x → v)
      exec(Σ(pc+1), Δ_*, μ, pc+1, Σ)
    case Store(e1, e2) =>
      val v1 = eval(e1, Δ, μ)
      val v2 = eval(e2, Δ, μ)
      val μ_* = v1 match { case IntV(α) => μ + (α → v2) }
      exec(Σ(pc+1), Δ, μ_*, pc+1, Σ)
    case Goto(e) => eval(e, Δ, μ) match { case IntV(ℓ) => exec(Σ(ℓ), Δ, μ, ℓ, Σ) }
    case Assert(e) => eval(e, Δ, μ) match { case IntV(1) => exec(Σ(pc+1), Δ, μ, pc+1, Σ) }
    case Cond(cnd, t1, t2) =>
      eval(cnd, Δ, μ) match {
        case IntV(1) => eval(t1, Δ, μ) match { case IntV(ℓ) => exec(Σ(ℓ), Δ, μ, ℓ, Σ) }
        case IntV(0) => eval(t2, Δ, μ) match { case IntV(ℓ) => exec(Σ(ℓ), Δ, μ, ℓ, Σ) }
      }
    case Halt(e) => (Δ, μ)
    case _ => throw new RuntimeException("not a statement")
  }

  def eval(e: Exp, Δ: Env, μ: Store): Value = e match {
    case Lit(i) => IntV(i)
    case Var(x) => Δ(x)
    case Load(e) => eval(e, Δ, μ) match { case IntV(α) => μ(α) }
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
}

object Taint {
  trait TaintStatus {
    def ∨(t: TaintStatus): TaintStatus
    def ∧(t: TaintStatus): TaintStatus
    def ¬(): TaintStatus
  }
  case object T extends TaintStatus {
    override def ∨(t: TaintStatus): TaintStatus = T
    override def ∧(t: TaintStatus): TaintStatus = t
    override def ¬(): TaintStatus = F
  }
  case object F extends TaintStatus {
    override def ∨(t: TaintStatus): TaintStatus = t
    override def ∧(t: TaintStatus): TaintStatus = F
    override def ¬(): TaintStatus = T
  }
  type TS = TaintStatus

  import SimpIL.Values._

  type PC = Int
  type Addr = Int
  type Store = Map[Addr, Value]
  type Env = Map[String, Value]
  type Prg = Map[PC, Stmt]

  type TaintEnv = Map[String, TaintStatus]
  type TaintStore = Map[Addr, TaintStatus]
}

trait TaintPolicy {
  import SimpIL.Values._
  import Taint._

  def input(src: String): TS
  def bincheck(t1: TS, t2: TS, v1: Value, v2: Value, op: String): TS
  def memcheck(t1: TS, t2: TS): TS
  def const(): TS
  def unop(t: TS): TS
  def assign(t: TS): TS
  def binop(t1: TS, t2: TS): TS
  def mem(ta: TS, tv: TS): TS
  def condcheck(te: TS, ta: TS): TS
  def gotocheck(ta: TS): TS
}

case object ATypicalTaintPolicy extends TaintPolicy {
  import SimpIL.Values._
  import Taint._

  def input(src: String): TS = T
  def bincheck(t1: TS, t2: TS, v1: Value, v2: Value, op: String): TS = T
  def memcheck(t1: TS, t2: TS): TS = T
  def const(): TS = F
  def unop(t: TS): TS = t
  def assign(t: TS): TS = t
  def binop(t1: TS, t2: TS): TS = t1 ∨ t2
  def mem(ta: TS, tv: TS): TS = ta
  def condcheck(te: TS, ta: TS): TS = ta.¬
  def gotocheck(ta: TS): TS = ta.¬
}

case class TaintAnalysis(p: TaintPolicy = ATypicalTaintPolicy) {
  import SimpIL.Values._
  import Taint._

  type Result = (Env, Store, TaintEnv, TaintStore)

  def run(p: Prog): Result = p match {
    case Prog(stmts) =>
      exec(stmts(0), Map(), Map(), Map(), Map(), 0, stmts.zipWithIndex.map(_.swap).toMap)
  }

  def exec(s: Stmt, Δ: Env, μ: Store, τΔ: TaintEnv, τμ: TaintStore, pc: PC, Σ: Prg): Result = s match {
    case Assign(x, e) =>
      val (v, t) = eval(e, Δ, μ, τΔ, τμ)
      val Δ_* = Δ + (x → v)
      val τΔ_* = τΔ + (x → p.assign(t))
      exec(Σ(pc+1), Δ_*, μ, τΔ_*, τμ, pc+1, Σ)
    case Store(e1, e2) =>
      val (v1, t1) = eval(e1, Δ, μ, τΔ, τμ)
      val (v2, t2) = eval(e2, Δ, μ, τΔ, τμ)
      assert(p.memcheck(t1, t2) == T)
      val (μ_*, τμ_*) = v1 match { case IntV(α) => (μ + (α → v2), τμ + (α → p.mem(t1, t2))) }
      exec(Σ(pc+1), Δ, μ_*, τΔ, τμ_*, pc+1, Σ)
    case Goto(e) =>
      val (IntV(ℓ), t) = eval(e, Δ, μ, τΔ, τμ)
      assert(p.gotocheck(t) == T)
      exec(Σ(ℓ), Δ, μ, τΔ, τμ, ℓ, Σ)
    case Assert(e) =>
      val (IntV(1), t) = eval(e, Δ, μ, τΔ, τμ)
      exec(Σ(pc+1), Δ, μ, τΔ, τμ, pc+1, Σ)
    case Cond(cnd, e1, e2) =>
      eval(cnd, Δ, μ, τΔ, τμ) match {
        case (IntV(1), t1) =>
          val (IntV(ℓ), t2) = eval(e1, Δ, μ, τΔ, τμ)
          assert(p.condcheck(t1, t2) == T)
          exec(Σ(ℓ), Δ, μ, τΔ, τμ, ℓ, Σ)
        case (IntV(0), t1) =>
          val (IntV(ℓ), t2) = eval(e1, Δ, μ, τΔ, τμ)
          assert(p.condcheck(t1, t2) == T)
          exec(Σ(ℓ), Δ, μ, τΔ, τμ, ℓ, Σ)
      }
    case Halt(e) => (Δ, μ, τΔ, τμ)
    case _ => throw new RuntimeException("not a statement")
  }

  def eval(e: Exp, Δ: Env, μ: Store, τΔ: TaintEnv, τμ: TaintStore): (Value, TS) = {
    def rec(e: Exp) = eval(e, Δ, μ, τΔ, τμ)
    e match {
      case Lit(i) => (IntV(i), p.const)
      case Var(x) => (Δ(x), τΔ(x))
      case Load(e) => rec(e) match { case (IntV(α), t) => (μ(α), p.mem(t, τμ(α))) }
      case BinOp(op, e1, e2) =>
        val (i1@IntV(v1), t1) = rec(e1)
        val (i2@IntV(v2), t2) = rec(e2)
        assert(p.bincheck(t1, t2, i1, i2, op) == T)
        (Concrete.evalBinOp(op, v1, v2), p.binop(t1, t2))
      case UnaryOp(op, e) =>
        val (i@IntV(v), t) = rec(e)
        (Concrete.evalUnaryOp(op, v), p.unop(t))
      case GetInput("stdin") => (IntV(scala.io.StdIn.readInt), p.input("stdin"))
      case _ => throw new RuntimeException("not an expression")
    }
  }
}

object SymExec {
  trait BExp {
    def ∧(e: BExp): BExp
    def ∨(e: BExp): BExp
  }
  case object True extends BExp {
    def ∧(e: BExp): BExp = e
    def ∨(e: BExp): BExp = True
    override def toString = "⊤"
  }
  case object False extends BExp {
    def ∧(e: BExp): BExp = False
    def ∨(e: BExp): BExp = e
    override def toString = "⊥"
  }
  case class BEq(e1: Exp, e2: Exp) extends BExp {
    def ∧(e: BExp): BExp = BConj(e :: this :: Nil)
    def ∨(e: BExp): BExp = BDisj(e :: this :: Nil)
    override def toString = s"($e1 == $e2)"
  }
  case class BConj(es: List[BExp]) extends BExp {
    def ∧(e: BExp): BExp = BConj(e :: es)
    def ∨(e: BExp): BExp = BDisj(e :: this :: Nil)
    override def toString = es.mkString("∧")
  }
  case class BDisj(es: List[BExp]) extends BExp {
    def ∧(e: BExp): BExp = BConj(e :: this :: Nil)
    def ∨(e: BExp): BExp = BDisj(e :: es)
    override def toString = es.mkString("∨")
  }
  case class BNeg(b: BExp) extends BExp {
    def ∧(e: BExp): BExp = BConj(e :: this :: Nil)
    def ∨(e: BExp): BExp = BDisj(e :: this :: Nil)
    override def toString = s"¬ $b"
  }

  def assertTrue(e: Exp): BExp = BEq(e, Lit(1)) // TODO: convert to boolean?
  def assertFalse(e: Exp): BExp = BEq(e, Lit(0))

  trait Value
  case class IntV(x: Int) extends Value
  case class SymV(x: Exp) extends Value

  var i: Int = 0
  def fresh: String = {
    val j = i
    i = i + 1
    s"x$j"
  }

  type PC = Int
  type Addr = Int
  type PCond = BExp
  type Store = Map[Addr, Value]
  type Env = Map[String, Value]
  type Prg = Map[PC, Stmt]
  type Result = Set[(Env, Store, BExp)]

  def error(msg: String) = throw new RuntimeException(msg)
  def symerror = throw new RuntimeException("Use a symbolic value as address!")

  def exec(s: Stmt, Δ: Env, μ: Store, Π: PCond, pc: PC, Σ: Prg): Result = s match {
    case Assign(x, e) =>
      val v = eval(e, Δ, μ)
      val Δ_* = Δ + (x → v)
      exec(Σ(pc+1), Δ_*, μ, Π, pc+1, Σ)
    case Store(e1, e2) =>
      val v1 = eval(e1, Δ, μ)
      val v2 = eval(e2, Δ, μ)
      val μ_* = v1 match {
        case IntV(α) => μ + (α → v2)
        case SymV(x) => symerror
      }
      exec(Σ(pc+1), Δ, μ_*, Π, pc+1, Σ)
    case Goto(e) => eval(e, Δ, μ) match {
      case IntV(ℓ) => exec(Σ(ℓ), Δ, μ, Π, ℓ, Σ)
      case SymV(x) => symerror
    } 
    case Assert(e) =>
      eval(e, Δ, μ) match {
        case IntV(1) => exec(Σ(pc+1), Δ, μ, Π, pc+1, Σ)
        case IntV(_) => Set((Map(), Map(), False))
        case SymV(x) => exec(Σ(pc+1), Δ, μ, Π ∧ assertTrue(x), pc+1, Σ)
      }
    case Cond(cnd, t1, t2) =>
      eval(cnd, Δ, μ) match {
        case IntV(1) => eval(t1, Δ, μ) match {
          case IntV(ℓ) => exec(Σ(ℓ), Δ, μ, Π, ℓ, Σ)
          case SymV(x) => symerror
        }
        case IntV(0) => eval(t2, Δ, μ) match {
          case IntV(ℓ) => exec(Σ(ℓ), Δ, μ, Π, ℓ, Σ)
          case SymV(x) => symerror
        }
        case SymV(x) => (eval(t1, Δ, μ), eval(t2, Δ, μ)) match {
          case (IntV(ℓ1), IntV(ℓ2)) =>
            exec(Σ(ℓ1), Δ, μ, Π ∧ assertTrue(x), ℓ1, Σ) ++
            exec(Σ(ℓ2), Δ, μ, Π ∧ assertFalse(x), ℓ2, Σ)
          case _ => symerror
        }
      }
    case Halt(e) => Set((Δ, μ, Π))
    case _ => throw new RuntimeException("not a statement")
  }

  def eval(e: Exp, Δ: Env, μ: Store): Value = e match {
    case Lit(i) => IntV(i)
    case Var(x) => Δ(x)
    case Load(e) => eval(e, Δ, μ) match {
      case IntV(α) => μ(α)
      case SymV(x) => symerror
    }
    case BinOp(op, e1, e2) => (eval(e1, Δ, μ), eval(e2, Δ, μ)) match {
      case (IntV(v1), IntV(v2)) => evalBinOp(op, v1, v2)
      case (IntV(v1), SymV(x)) => SymV(BinOp(op, Lit(v1), x))
      case (SymV(x), IntV(v2)) => SymV(BinOp(op, x, Lit(v2)))
    }
    case UnaryOp(op, e) => eval(e, Δ, μ) match { case IntV(v) => evalUnaryOp(op, v) }
    case GetInput("stdin") => SymV(Var(fresh))
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

  def run(p: Prog): Result = p match {
    case Prog(stmts) => exec(stmts(0), Map(), Map(), True, 0, stmts.zipWithIndex.map(_.swap).toMap)
  }
}
