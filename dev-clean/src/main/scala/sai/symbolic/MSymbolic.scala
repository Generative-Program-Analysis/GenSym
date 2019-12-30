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
  val M = Monad[ReaderT[StateT[IdM, State, ?], Prg, ?]]

  def get_prog: M[Prg] = ???

  def get_pc: M[PC] = ???
  def set_pc(pc: PC): M[Unit] = ???
  def inc_pc: M[Unit] = ???

  def get_env: M[Env] = ???
  def update_env(x: String, v: Value): M[Unit] = ???

  def get_store: M[Store] = ???
  def update_store(a: Addr, v: Value): M[Unit] = ???

  def branch(c: Value, t1: Exp, t2: Exp): M[PC] =
    if (c == 1) (for {
      pc <- eval(t1)
    } yield pc)
    else (for {
      pc <- eval(t2)
    } yield pc)

  def fail(e: Exp): M[Unit] = ???

  def exec(s: Stmt): M[Unit] =  s match {
    case Assign(x, e) => for {
      v <- eval(e)
      _ <- update_env(x, v)
      _ <- inc_pc
    } yield ()
    case Store(e1, e2) => for {
      a <- eval(e1)
      v <- eval(e2)
      _ <- update_store(a, v)
      _ <- inc_pc
    } yield ()
    case Goto(e) => for {
      pc <- eval(e)
      _ <- set_pc(pc)
    } yield ()
    case Assert(e) => for {
      v <- eval(e)
      _ <- if (v == 1) inc_pc else ???
    } yield ()
    case Cond(cnd, t1, t2) => for {
      c <- eval(cnd)
      pc <- branch(c, t1, t2)
      _ <- set_pc(pc)
    } yield ()
    case Output(s) => M.pure(println(s))
    case Halt(e) => for {
      v <- eval(e)

    } yield ()
  }


  def run(s: Stmt): M[Value] = for {
    Σ <- get_prog
    pc <- get_pc
    _ <- exec(Σ(pc))
    next_pc <- get_pc
    r <- run(Σ(next_pc))
  } yield r

  def eval(e: Exp): M[Value] = e match {
    case Lit(i) => M.pure(i)
    case Var(x) => for { ρ <- get_env } yield ρ(x)
    case Load(e) => for {
      σ <- get_store
      a <- eval(e)
    } yield σ(a)
    case BinOp(op, e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
    } yield evalBinOp(op, v1, v2)
    case UnaryOp(op, e) => for { v <- eval(e) } yield evalUnaryOp(op, v)
    case GetInput(_) => M.pure(scala.io.StdIn.readInt)
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
