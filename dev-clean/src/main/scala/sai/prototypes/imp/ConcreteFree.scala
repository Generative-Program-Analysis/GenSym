package sai.imp

import sai.structure.functor._

import sai.structure.monad.free._
import Free._
import Coproduct.{CoproductFunctor => _, _}
import NondetEff.{NondetFunctor => _, _}
import VoidEff.{VoidFunctor => _, _}
import StateEff.{StateFunctor => _, _}
import NondetVoidEff._
import StateNondetEff._

import sai.lang.ImpLang._

// A concrete semantics of Imp using free monads

object ImpFree {
  trait Value
  case class IntV(i: Int) extends Value
  case class BoolV(b: Boolean) extends Value

  type Store = Map[String, Value]

  def eval(e: Expr, σ: Store): Value = e match {
    case Lit(i: Int) => IntV(i)
    case Lit(b: Boolean) => BoolV(b)
    case Var(x) => σ(x)
    case Op1("-", e) =>
      val IntV(i) = eval(e, σ)
      IntV(-i)
    case Op2(op, e1, e2) =>
      val IntV(i1) = eval(e1, σ)
      val IntV(i2) = eval(e2, σ)
      op match {
        case "+" => IntV(i1 + i2)
        case "-" => IntV(i1 - i2)
        case "*" => IntV(i1 * i2)
        case "==" => BoolV(i1 == i2)
        case "<=" => BoolV(i1 <= i2)
        case "<" => BoolV(i1 < i2)
        case ">=" => BoolV(i1 >= i2)
        case ">" => BoolV(i1 > i2)
      }
  }

  def eval[F[_]: Functor](e: Expr)(implicit I1: Nondet ⊆ F, I2: State[Store, *] ⊆ F): Free[F, Value] =
    for {
      σ <- get
    } yield eval(e, σ)

  def select[F[_]: Functor, A](xs: List[A])(implicit I: Nondet ⊆ F): Free[F, A] =
    xs.map(Return[F, A]).foldRight[Free[F, A]](fail)(choice)

  def ifM[F[_]: Functor](e: Expr, s1: Stmt, s2: Stmt)(implicit I: Nondet ⊆ F, I2: State[Store, *] ⊆ F): Free[F, Stmt] =
    for {
      v <- eval(e)
    } yield {
      val BoolV(b) = v
      if (b) s1 else s2
    }

  def exec[F[_]: Functor](s: Stmt)(implicit I1: Nondet ⊆ F, I2: State[Store, *] ⊆ F): Free[F, Unit] = s match {
    case Skip() => ret(())
    case Assign(x, e) =>
      for {
        σ <- get
        v <- eval(e)
        _ <- put(σ + (x → v))
      } yield ()
    case Cond(e, s1, s2) =>
      for {
        n <- ifM(e, s1, s2)
        _ <- exec(s)
      } yield ()
    case Seq(s1, s2) =>
      for {
        _ <- exec(s1)
        _ <- exec(s2)
      } yield ()
    case While(e, b) =>
      for {
        n <- ifM(e, Seq(b, s), Skip())
        _ <- exec(n)
      } yield ()
  }

  def test: Unit = {
    import Examples._
    import VoidEff.VoidFunctor
    import StateEff.StateFunctor
    import NondetEff.NondetFunctor

    //implicit val F1 = Functor[(State[Store, ?] ⊕ ∅)#t]
    implicit val F2 = Functor[(Nondet ⊕ ∅)#t]

    // concrete execution
    println(VoidEff.run(NondetEff.run(StateEff.run(Map[String, Value](), exec[(State[Store, *] ⊕ (Nondet ⊕ ∅)#t)#t](fact5)))))
  }
}
