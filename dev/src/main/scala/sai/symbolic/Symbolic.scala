package sai
package symbolic

import SimpIL._

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
    case Halt() => (Δ, μ)
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

  def main(args: Array[String]): Unit = {
    import SimpIL.Examples._
    println(run(ex1))
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

  trait Value
  case class IntV(x: Int, τ: TaintStatus) extends Value

  type PC = Int
  type Addr = Int
  type Store = Map[Addr, Value]
  type Env = Map[String, Value]
  type Prg = Map[PC, Stmt]

  type TaintEnv = Map[String, TaintStatus]
  type TaintStore = Map[Addr, TaintStatus]
}

trait TaintPolicy {
  import Taint._

  type TS = TaintStatus
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
