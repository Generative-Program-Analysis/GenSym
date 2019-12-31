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

  import ReaderT._
  import EitherT._
  import StateT._

  type StateM[T] = StateT[IdM, State, T]
  type EitherStateM[T] = EitherT[StateM, Value, T]
  type M[T] = ReaderT[EitherStateM, Prg, T]

  val EitherStateM = Monad[EitherStateM]
  val M = Monad[M]

  def get_prog: M[Prg] = ReaderTMonad[EitherStateM, Prg].ask

  def get_pc: M[PC] = ReaderT.liftM[EitherStateM, Prg, PC](
    EitherT.liftM[StateM, Value, PC](
      StateTMonad[IdM, State].get.map(s => s._1)
    ))
  def set_pc(pc: PC): M[Unit] = ReaderT.liftM[EitherStateM, Prg, Unit](
    EitherT.liftM[StateM, Value, Unit](
      StateTMonad[IdM, State].mod(s => (pc, s._2, s._3))
    ))
  def inc_pc: M[Unit] = ReaderT.liftM[EitherStateM, Prg, Unit](
    EitherT.liftM[StateM, Value, Unit](
      StateTMonad[IdM, State].mod(s => (s._1 + 1, s._2, s._3))
    ))

  def get_env: M[Env] = ReaderT.liftM[EitherStateM, Prg, Env](
    EitherT.liftM[StateM, Value, Env](
      StateTMonad[IdM, State].get.map(s => s._2)
    ))
  def update_env(x: String, v: Value): M[Unit] = ReaderT.liftM[EitherStateM, Prg, Unit](
    EitherT.liftM[StateM, Value, Unit](
      StateTMonad[IdM, State].mod(s => (s._1, s._2 + (x -> v), s._3))
    ))

  def get_store: M[Store] = ReaderT.liftM[EitherStateM, Prg, Store](
    EitherT.liftM[StateM, Value, Store](
      StateTMonad[IdM, State].get.map(s => s._3)
    ))
  def update_store(a: Addr, v: Value): M[Unit] = ReaderT.liftM[EitherStateM, Prg, Unit](
    EitherT.liftM[StateM, Value, Unit](
      StateTMonad[IdM, State].mod(s => (s._1, s._2, s._3 + (a -> v)))
    ))

  def branch(c: Value, t1: Exp, t2: Exp): M[PC] =
    if (c == 1) eval(t1) else eval(t2)
  def assert(v: Value): M[Unit] =
    if (v == 1) inc_pc else halt(-1)

  def halt(v: Value): M[Unit] =
    ReaderT.liftM[EitherStateM, Prg, Unit](EitherT.left[StateM, Value, Unit](v))

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
      _ <- assert(v)
    } yield ()
    case Cond(cnd, t1, t2) => for {
      c <- eval(cnd)
      pc <- branch(c, t1, t2)
      _ <- set_pc(pc)
    } yield ()
    case Output(s) => for {
      _ <- M.pure(println(s))
      _ <- inc_pc
    } yield ()
    case Halt(e) => for {
      v <- eval(e)
      _ <- halt(v)
    } yield ()
  }

  def drive: M[Unit] = for {
    Σ <- get_prog
    i <- get_pc
    _ <- exec(Σ(i))
    _ <- drive
  } yield ()

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

  def run(p: Prog): (Either[Value, Unit], State) = {
    def toMap(p: Prog): Prg = p match {
      case Prog(stmts) => stmts.zipWithIndex.map(_.swap).toMap
    }
    drive(toMap(p)).run((0, Map(), Map())).run
  }

  def main(args: Array[String]): Unit = {
    import SimpIL.Examples._
    //println(run(ex1))

    val h = Prog(List(
      Halt(Lit(1))
    ))
    //println(run(h))

    println(run(ex3))
  }
}
