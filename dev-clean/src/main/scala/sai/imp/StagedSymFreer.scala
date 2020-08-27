package sai.imp

import sai.lang.ImpLang._

import scala.language.{higherKinds, implicitConversions}

import sai.structure.freer3.Eff._
import sai.structure.freer3.Freer._
import sai.structure.freer3.Handlers._
import sai.structure.freer3.OpenUnion._
import sai.structure.freer3.State._
import sai.structure.freer3.IO._
// import sai.structure.freer3.Nondet._

import lms.core._
import lms.core.stub.{While => _, _}
import lms.macros._
import lms.core.Backend._
import sai.lmsx._

import scala.collection.immutable.{List => SList}

object Symbol {
  private val counters = scala.collection.mutable.HashMap[String,Int]()

  def freshName(prefix: String): String = {
    val count = counters.getOrElse(prefix, 1)
    counters.put(prefix, count + 1)
    prefix + "_" + count
  }
}

@virtualize
trait RepBinaryNondet extends SAIOps {
  import sai.structure.freer3.Nondet._
  def run_with_mt[A: Manifest]: Comp[Nondet ⊗ ∅, Rep[A]] => Comp[∅, Rep[List[A]]] =
    handler[Nondet, ∅, Rep[A], Rep[List[A]]] {
      case Return(x) => ret(List(x))
    } (new DeepH[Nondet, ∅, Rep[List[A]]] {
      def apply[X] = (_, _) match {
        case Fail$() => ret(List())
        case Choice$((), k) =>
          val xs: Rep[List[A]] = k(true)
          val ys: Rep[List[A]] = k(false)
          ret(xs ++ ys)
      }
    })
}

@virtualize
trait RepNondet extends SAIOps {
  case class Nondet[A: Manifest](xs: Rep[List[A]])

  def perform[T[_], R <: Eff, X: Manifest](op: T[X])(implicit I: T ∈ R): Comp[R, Rep[X]] =
    Op(I.inj(op)) { x => Return(x) }

  def fail[R <: Eff, A: Manifest](implicit I: Nondet ∈ R): Comp[R, Rep[A]] =
    perform[Nondet, R, A](Nondet(List()))

  def choice[R <: Eff, A: Manifest](x: Rep[A], y: Rep[A])(implicit I: Nondet ∈ R): Comp[R, Rep[A]] =
    perform(Nondet(List(x, y)))

  def select[R <: Eff, A: Manifest](xs: Rep[List[A]])(implicit I: Nondet ∈ R): Comp[R, Rep[A]] =
    perform(Nondet(xs))

  object Nondet$ {
    def unapply[A: Manifest, R](n: (Nondet[A], Rep[A] => R)): Option[(Rep[List[A]], Rep[A] => R)] =
      n match {
        case (Nondet(xs), k) => Some((xs, k))
        case _ => None
      }
  }

  // TODO: how to make it nice?
  // Observation: curried style works really bad when having Manifest
  def runRepNondet[A: Manifest](comp: Comp[Nondet ⊗ ∅, Rep[A]]): Comp[∅, Rep[List[A]]] = {
    val h = handler[Nondet, ∅, Rep[A], Rep[List[A]]] {
      case Return(x) => ret(List(x))
    } (new DeepH[Nondet, ∅, Rep[List[A]]] {
      def apply[X]: (Nondet[X], X => Comp[∅, Rep[List[A]]]) => Comp[∅, Rep[List[A]]] = {
        // Note: writing in ordinary style wont work, because DeepH doesn't know X is Rep[A]
        def f(fx: Nondet[X], k: X => Comp[∅, Rep[List[A]]]): Comp[∅, Rep[List[A]]] = {
          val xs: Rep[List[A]] = fx.asInstanceOf[Nondet[A]].xs
          val kk = k.asInstanceOf[Rep[A] => Comp[∅, Rep[List[A]]]]
          ret(xs.foldLeft(List[A]()) { case (acc, x) =>
            acc ++ kk(x)
          })
        }
        f
      }
    })
    h(comp)
  }
}

@virtualize
trait StagedSymImpEff extends SAIOps with RepNondet {

  // Basic value representation and their constructors/matchers

  abstract class Value
  object IntV {
    def apply (i: Rep[Int]): Rep[Value] =
      Wrap[Value](Adapter.g.reflect("IntV", Unwrap(i)))
    def unapply(v: Rep[Value]): Option[Rep[Int]] =
      Some(Wrap[Int](Adapter.g.reflect("IntV-proj", Unwrap(v))))
  }
  object IntVConst {
    def unapply(v: Rep[Value]): Option[Rep[Int]] =
      v match {
        case Adapter.g.Def("IntV", collection.immutable.List(v: Backend.Exp)) =>
          Some(Wrap[Int](v))
        case _ => None
      }
  }

  object BoolV {
    def apply(b: Rep[Boolean]): Rep[Value] =
      Wrap[Value](Adapter.g.reflect("BoolV", Unwrap(b)))
    def unapply(v: Rep[Value]): Option[Rep[Boolean]] =
      Some(Wrap[Boolean](Adapter.g.reflect("BoolV-proj", Unwrap(v))))
  }
  object BoolVConst {
    def unapply(v: Rep[Value]): Option[Rep[Boolean]] =
      v match {
        case Adapter.g.Def("SymV", SList(v: Backend.Exp)) =>
          Some(Wrap[Boolean](v))
        case _ => None
      }
  }

  object SymV {
    def apply(x: Rep[String]): Rep[Value] =
      Wrap[Value](Adapter.g.reflect("SymV", Unwrap(x)))
    def unapply(v: Rep[Value]): Option[Rep[String]] =
      Some(Wrap[String](Adapter.g.reflect("SymV-proj", Unwrap(v))))
  }
  object SymVConst {
    def unapply(v: Rep[Value]): Option[Rep[String]] =
      v match {
        case Adapter.g.Def("SymV", SList(v: Backend.Exp)) =>
          Some(Wrap[String](v))
        case _ => None
      }
  }

  def op_neg(v: Rep[Value]): Rep[Value] = {
    Unwrap(v) match {
      case IntVConst(i) => IntV(-i)
      case SymVConst(i) => SymV(unit("-"+i))
      case IntV(i) => IntV(-i)
    }
  }

  def op_2(op: String, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = {
    Wrap[Value](Adapter.g.reflect("op", Unwrap(unit(op)), Unwrap(v1), Unwrap(v2)))
  }

  type PC = Set[Expr]
  type Store = Map[String, Value]
  type SS = (Store, PC)

  // type E = IO ⊗ (State[Rep[SS], *] ⊗ (Nondet ⊗ ∅))
  type E = State[Rep[SS], *] ⊗ (Nondet ⊗ ∅)
  type SymEff[T] = Comp[E, T]

  def getStore: SymEff[Rep[Store]] = for { s <- get[Rep[SS], E] } yield s._1

  def updateStore(x: Rep[String], v: Rep[Value]): SymEff[Rep[Unit]] =
    for {
      s <- get[Rep[SS], E]
      _ <- {
        val σ: Rep[Store] = s._1 + (x -> v)
        val ss: Rep[SS] = (σ, s._2)
        put[Rep[SS], E](ss)
      }
    } yield ()

  def getPC: SymEff[Rep[PC]] = for { s <- get[Rep[SS], E] } yield s._2

  def updatePathCond(e: Expr): SymEff[Rep[Unit]] =
    for {
      s <- get[Rep[SS], E]
      _ <- {
        val pc: Rep[PC] = Set(e) ++ s._2
        val ss: Rep[SS] = (s._1, pc)
        put[Rep[SS], E](ss)
      }
    } yield ()

  // The `State.run` doesn't work well for staged S and state A, as it produces Comp[E, (Rep[S], Rep[A])] result.
  // Maybe we can have a handler that transform Comp[E, (Rep[S], Rep[A])] into Comp[E, Rep[(S, A)]].
  // TODO: double check this, and the one in State.scala
  def runRepState[E <: Eff, S: Manifest, A: Manifest](s: Rep[S], comp: Comp[State[Rep[S], *] ⊗ E, Rep[A]]): Comp[E, Rep[(S, A)]] =
    comp match {
      case Return(x) => ret((s, x))
      case Op(u, k) => decomp[State[Rep[S], *], E, Any](u) match {
        case Right(op) => op match {
          case Get() => runRepState(s, k(s))
          case Put(s1) => runRepState(s1, k(()))
        }
        case Left(op) =>
          Op(op) { x => runRepState(s, k(x)) }
      }
    }

  def reify(s: Rep[SS])(comp: Comp[E, Rep[Unit]]): Rep[List[(SS, Unit)]] = {
    val p1: Comp[Nondet ⊗ ∅, Rep[(SS, Unit)]] =
      runRepState[Nondet ⊗ ∅, SS, Unit](s, comp)
    val p2: Comp[∅, Rep[List[(SS, Unit)]]] = runRepNondet(p1)
    p2
  }

  def reflect(res: Rep[List[(SS, Unit)]]): Comp[E, Rep[Unit]] = {
    for {
      ssu <- select[E, (SS, Unit)](res)
      _ <- put[Rep[SS], E](ssu._1)
    } yield ssu._2
  }

  def unfold(w: While, k: Int): Stmt =
    if (k == 0) Skip()
    else w match {
      case While(e, b) =>
        Cond(e, Seq(b, unfold(w, k-1)), Skip())
    }

  def eval(e: Expr, σ: Rep[Store]): Rep[Value] = e match {
    case Lit(i: Int) => IntV(i)
    case Lit(b: Boolean) => BoolV(b)
    case Input() => SymV(Symbol.freshName("x"))
    case Var(x) => σ(x)
    case Op1("-", e) =>
      val v = eval(e, σ)
      op_neg(v)
    case Op2(op, e1, e2) =>
      val v1 = eval(e1, σ)
      val v2 = eval(e2, σ)
      op_2(op, v1, v2)
  }

  def eval(e: Expr): SymEff[Rep[Value]] = for { σ <- getStore } yield eval(e, σ)

  def execWithPC(s: Stmt, pc: Expr): Comp[E, Rep[Unit]] =
    for {
      _ <- updatePathCond(pc)
      _ <- exec(s)
    } yield ()

  def exec(s: Stmt): SymEff[Rep[Unit]] =
    s match {
      case Skip() => ret(())
      case Assign(x, e) =>
        for {
          v <- eval(e)
          _ <- updateStore(x, v)
        } yield ()
      case Cond(e, s1, s2) =>
        /*
        for {
          b <- eval(e)
          v <- choice(execWithPC(s1, e), execWithPC(s2, Op1("-", e)))
        } yield { v }
         */
        ???
      case Seq(s1, s2) =>
        for { _ <- exec(s1); _ <- exec(s2) } yield ()
      case While(e, s) =>
        val k = 4
        exec(unfold(While(e, s), k))
      case Assert(e) =>
        updatePathCond(e)
    }
}

object StagedSymFreer {
  def main(args: Array[String]): Unit = {
    ???
  }
}

