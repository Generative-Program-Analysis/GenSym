package sai.imp

import sai.structure.freer._
import Eff._
import Freer._
import Handlers._
import OpenUnion._
import State._
import IO._

import sai.lang.ImpLang._
import scala.language.{higherKinds, implicitConversions}

// A symbolic execution semantics of Imp using *freer* monads

object FreerUnstagedImp {
  import Nondet._
  import Knapsack._
  import sai.lang.ImpLang._

  trait Value
  case class IntV(i: Int) extends Value
  case class BoolV(b: Boolean) extends Value
  case class SymV(e: Expr) extends Value

  type PC = Set[Expr]
  type Store = Map[String, Value]
  type SS = (Store, PC)

  def getStore[R <: Eff](implicit I: State[SS, *] ∈ R): Comp[R, Store] =
    for {s <- get} yield s._1
  def getPC[R <: Eff](implicit I: State[SS, *] ∈ R): Comp[R, PC] =
    for {s <- get} yield s._2
  def updateStore[R <: Eff](x: String, v: Value)(implicit I: State[SS, *] ∈ R): Comp[R, Unit] =
    for {s <- get; _ <- put((s._1 + (x -> v), s._2))} yield ()
  def updatePathCond[R <: Eff](e: Expr)(implicit I: State[SS, *] ∈ R): Comp[R, Unit] =
    for {s <- get; _ <- put((s._1, Set(e) ++ s._2))} yield ()

  def eval(e: Expr, σ: Store): Value =
    e match {
      case Lit(i: Int) => IntV(i)
      case Lit(b: Boolean) => BoolV(b)
      case Var(x) => σ(x)
      case Op1("-", e) =>
        eval(e, σ) match {
          case IntV(i) => IntV(-i)
          case SymV(e) => SymV(Op1("-", e))
        }
      case Op2(op, e1, e2) =>
        val v1 = eval(e1, σ)
        val v2 = eval(e2, σ)
          (op, v1, v2) match {
          case ("+", IntV(i1), IntV(i2)) => IntV(i1 + i2)
          case ("-", IntV(i1), IntV(i2)) => IntV(i1 - i2)
          case ("*", IntV(i1), IntV(i2)) => IntV(i1 * i2)
          case ("/", IntV(i1), IntV(i2)) => IntV(i1 / i2)
          case ("==", IntV(i1), IntV(i2)) => BoolV(i1 == i2)
          case ("<=", IntV(i1), IntV(i2)) => BoolV(i1 <= i2)
          case (">=", IntV(i1), IntV(i2)) => BoolV(i1 >= i2)
          case ("<", IntV(i1), IntV(i2)) => BoolV(i1 < i2)
          case (">", IntV(i1), IntV(i2)) => BoolV(i1 > i2)
          case (op, SymV(e1), SymV(e2)) => SymV(Op2(op, e1, e2))
        }
    }

  def eval[R <: Eff](e: Expr)(implicit I1: State[SS, *] ∈ R, I2: IO ∈ R): Comp[R, Value] =
    e match {
      case Input() => for { i <- readInt } yield IntV(i)
      case _ => for { σ <- getStore } yield eval(e, σ)
    }

  def exec[R <: Eff](s: Stmt)(implicit I1: State[SS, *] ∈ R, I2: IO ∈ R, I3: Nondet ∈ R): Comp[R, Unit] =
    s match {
      case Skip() => ret(())
      case Assign(x, e) => for { v <- eval(e); σ <- getStore; _ <- updateStore(x, v) } yield ()
      case Cond(e, s1, s2) =>
        for {
          v <- eval(e)
          _ <- if (v == BoolV(true)) exec(s1)
          else if (v == BoolV(false)) exec(s2)
          else choice(exec(s1), exec(s2))
        } yield ()
      case Seq(s1, s2) => for { _ <- exec(s1); _ <- exec(s2) } yield ()
      case While(e, s) =>
        def loop: Comp[R, Unit] = for {
          v <- eval(e)
          _ <- if (v == BoolV(true)) for { _ <- exec(s); _ <- loop } yield ()
          else if (v == BoolV(false)) exec(Skip())
          else choice(for { _ <- exec(s); _ <- loop } yield (), exec(Skip()))
        } yield ()
        loop
      case Output(e) => for {
        v <- eval(e)
        _ <- writeInt(v.asInstanceOf[IntV].i)
      } yield ()
    }

  def run(s: Stmt) = {
    val ss0: SS = (Map(), Set())
    val p: Comp[IO ⊗ (State[SS, *] ⊗ (Nondet ⊗ ∅)), Unit] =
      exec[IO ⊗ (State[SS, *] ⊗ (Nondet ⊗ ∅))](s)
    val p1: Comp[State[SS, *] ⊗ (Nondet ⊗ ∅), Unit] = IO.run(p)
    val p2: Comp[∅, List[Comp[∅, List[(SS, Unit)]]]] = runLocalState(ss0, p1)
    println(extract(p2).map(extract))
  }
}
