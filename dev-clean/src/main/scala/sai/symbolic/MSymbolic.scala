package sai.msymbolic

import sai.lang.SimpIL
import sai.lang.SimpIL._

import sai.structure.monad._

object Concrete {
  //import SimpIL.Values._

  type PC = Int
  type Addr = Int
  type Value = Int
  type Env = Map[String, Value]
  type Store = Map[Addr, Value]
  type Prg = Map[PC, Stmt]

  type State = (PC, Env, Store)
  type M[T] = ReaderT[StateT[IdM, State, ?], Prg, T]

  def get_prog: M[Prg] = ???
  def get_pc: M[PC] = ???
  def set_pc(pc: PC): M[Unit] = ???
  def inc_pc: M[Unit] = ???
  def update_env(x: String, v: Value): M[Unit] = ???
  def update_store(a: Addr, v: Value): M[Unit] = ???

  def evalM(e: Exp): M[Value] = ???

  def branch(c: Value, t1: Exp, t2: Exp): M[PC] =
    if (c == 1) (for {
      pc <- evalM(t1)
    } yield pc)
    else (for {
      pc <- evalM(t2)
    } yield pc)

  def exec(s: Stmt): M[Unit] =  s match {
    case Assign(x, e) => for {
      v <- evalM(e)
      _ <- update_env(x, v)
      _ <- inc_pc
    } yield ()
    case Store(e1, e2) => for {
      a <- evalM(e1)
      v <- evalM(e2)
      _ <- update_store(a, v)
      _ <- inc_pc
    } yield ()
    case Goto(e) => for {
      pc <- evalM(e)
      _ <- set_pc(pc)
    } yield ()
    case Assert(e) => for {
      v <- evalM(e)
      _ <- if (v == 1) inc_pc else ???
    } yield ()
    case Cond(cnd, t1, t2) => for {
      c <- evalM(cnd)
      pc <- branch(c, t1, t2)
      _ <- set_pc(pc)
    } yield ()
    case Halt(e) => for {
      v <- evalM(e)

    } yield ()
  }

  def run(s: Stmt): M[Value] = for {
    Σ <- get_prog
    pc <- get_pc
    _ <- exec(Σ(pc))
    next_pc <- get_pc
    r <- run(Σ(next_pc))
  } yield r

  def eval(e: Exp, Δ: Env, μ: Store): Value = e match {
    case Lit(i) => i
    case Var(x) => Δ(x)
    case Load(e) => eval(e, Δ, μ) match { case α => μ(α) }
    case BinOp(op, e1, e2) => (eval(e1, Δ, μ), eval(e2, Δ, μ)) match {
      case (v1, v2) => evalBinOp(op, v1, v2)
    }
    case UnaryOp(op, e) => eval(e, Δ, μ) match { case v => evalUnaryOp(op, v) }
    case GetInput("stdin") => scala.io.StdIn.readInt
    case _ => throw new RuntimeException("not an expression")
  }

  def evalBinOp(op: String, v1: Int, v2: Int): Value = op match {
    case "+" => v1 + v2
    case "-" => v1 - v2
    case "*" => v1 * v2
    case "/" => v1 / v2
    case "==" => if (v1 == v2) 1 else 0
    case "!=" => if (v1 != v2) 1 else 0
    case ">"  => if (v1 >  v2) 1 else 0
    case ">=" => if (v1 >= v2) 1 else 0
    case "<"  => if (v1 <  v2) 1 else 0
    case "<=" => if (v1 <= v2) 1 else 0
  }

  def evalUnaryOp(op: String, v: Int): Value = op match {
    case "~" => -v
  }
}
