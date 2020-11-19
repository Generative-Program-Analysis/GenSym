package sai.imp

import sai.lang.ImpLang._

import scala.language.{higherKinds, implicitConversions, existentials}

import sai.structure.freer._
import sai.structure.freer.Eff._
import sai.structure.freer.Freer._
import sai.structure.freer.Handlers._
import sai.structure.freer.OpenUnion._
import sai.structure.freer.State._
import sai.structure.freer.IO._

import lms.core._
import lms.core.stub.{While => _, _}
import lms.macros._
import lms.core.Backend._
import sai.lmsx._

import scala.collection.immutable.{List => SList}
import sai.utils.symbol._

// Basic value representation and their constructors/matchers

@virtualize
trait Vals extends SAIOps {
  abstract class Value
  implicit class ValueOps(v: Rep[Value]) {
    def isBool: Rep[Boolean] = Wrap[Boolean](Adapter.g.reflect("BoolV-pred", Unwrap(v)))
    def getBool: Rep[Boolean] = BoolV.unapply(v).get
  }

  object IntV {
    def apply (i: Rep[Int]): Rep[Value] =
      Wrap[Value](Adapter.g.reflect("IntV", Unwrap(i)))
    def unapply(v: Rep[Value]): Option[Rep[Int]] =
      Some(Wrap[Int](Adapter.g.reflect("IntV-proj", Unwrap(v))))
  }
  object IntVConst {
    def unapply(v: Rep[Value]): Option[Rep[Int]] =
      v match {
        case Adapter.g.Def("IntV", SList(v: Backend.Exp)) =>
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
}

@virtualize
trait StagedSMT extends Vals with SAIOps {
  sealed trait SMT[K]
  case class IsSAT(pc: Rep[Set[Expr]]) extends SMT[Rep[Boolean]]
  case class Concretize(pc: Rep[Set[Expr]], x: Rep[Value]) extends SMT[Rep[Value]]

  def isSAT[R <: Eff](pc: Rep[Set[Expr]])(implicit I: SMT ∈ R): Comp[R, Rep[Boolean]] =
    perform[SMT, R, Rep[Boolean]](IsSAT(pc))

  def concretize[R <: Eff](pc: Rep[Set[Expr]], x: Rep[Value])(implicit I: SMT ∈ R): Comp[R, Rep[Value]] =
    perform[SMT, R, Rep[Value]](Concretize(pc, x))

  object IsSAT$ {
    def unapply[K, R](n: (SMT[K], K => R)): Option[(Rep[Set[Expr]], Rep[Boolean] => R)] = n match {
      case (IsSAT(pc), k) => Some((pc, k))
      case _ => None
    }
  }

  object Concretize$ {
    def unapply[K, R](n: (SMT[K], K => R)): Option[(Rep[Set[Expr]], Rep[Value], Rep[Value] => R)] = n match {
      case (Concretize(pc, x), k) => Some((pc, x, k))
      case _ => None
    }
  }

  def runFakeSMT[E <: Eff, A]: Comp[SMT ⊗ E, A] => Comp[E, A] =
    handler[SMT, E, A, A] {
      case Return(x) => ret(x)
    } (ν[DeepH[SMT, E, A]] {
      case IsSAT$(pc, k) =>
        // Always returns sat
        k(true)
      case Concretize$(pc, x, k) =>
        // Always returns 0
        k(IntV(0))
    })
}

@virtualize
trait StagedIO extends Vals with SAIOps {
  sealed trait IO[K]
  case class ReadInt() extends IO[Rep[Value]]
  case class WriteInt(x: Rep[Value]) extends IO[Rep[Unit]]

  def readInt[R <: Eff](implicit I: IO ∈ R): Comp[R, Rep[Value]] =
    perform[IO, R, Rep[Value]](ReadInt())
  def writeInt[R <: Eff](n: Rep[Value])(implicit I: IO ∈ R): Comp[R, Rep[Unit]] =
    perform[IO, R, Rep[Unit]](WriteInt(n))

  object ReadInt$ {
    def unapply[K, R](n: (IO[K], K => R)): Option[(Unit, Rep[Value] => R)] = n match {
      case (ReadInt(), k) => Some(((), k))
      case _ => None
    }
  }

  object WriteInt$ {
    def unapply[K, R](n: (IO[K], K => R)): Option[(Rep[Value], Rep[Unit] => R)] = n match {
      case (WriteInt(x), k) => Some((x, k))
      case _ => None
    }
  }

  def runIO[E <: Eff, A]: Comp[IO ⊗ E, A] => Comp[E, A] =
    handler[IO, E, A, A] {
      case Return(x) => ret(x)
    } (ν[DeepH[IO, E, A]] {
      case ReadInt$((), k) =>
        val n = SymV(Symbol.freshName("x"))
        k(n)
      case WriteInt$(x, k) =>
        val u = Wrap[Unit](Adapter.g.reflectWrite("write_value", Unwrap(x))(Adapter.CTRL))
        k(u)
    })
}

@virtualize
trait StagedSymImpEff extends Vals with SAIOps with StagedNondet with StagedIO with StagedSMT {
  def evalOp2(op: String, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = {
    Wrap[Value](Adapter.g.reflect("op", Unwrap(unit(op)), Unwrap(v1), Unwrap(v2)))
  }

  def opNeg(v: Rep[Value]): Rep[Value] = {
    Unwrap(v) match {
      case IntVConst(i) => IntV(-i)
      case SymVConst(i) => SymV(unit("-"+i))
      case IntV(i) => IntV(-i)
    }
  }

  type PC = Set[Expr]
  type Store = Map[String, Value]
  type SS = (Store, PC)
  type E = State[Rep[SS], *] ⊗ (SMT ⊗ (IO ⊗ (Nondet ⊗ ∅)))
  type SymEff[T] = Comp[E, T]

  def reify(s: Rep[SS])(comp: Comp[E, Rep[Unit]]): Rep[List[(SS, Unit)]] = {
    val c1: Comp[SMT ⊗ (IO ⊗ (Nondet ⊗ ∅)), (Rep[SS], Rep[Unit])] =
      State.runState[SMT ⊗ (IO ⊗ (Nondet ⊗ ∅)), Rep[SS], Rep[Unit]](s)(comp)
    val c2: Comp[SMT ⊗ (IO ⊗ (Nondet ⊗ ∅)), Rep[(SS, Unit)]] =
      for { x <- c1 } yield x
    val c3: Comp[IO ⊗ (Nondet ⊗ ∅), Rep[(SS, Unit)]] = runFakeSMT(c2)
    val c4: Comp[Nondet ⊗ ∅, Rep[(SS, Unit)]] = runIO(c3)
    val c5: Comp[∅, Rep[List[(SS, Unit)]]] = runRepNondet(c4)
    c5
  }

  def reflect(res: Rep[List[(SS, Unit)]]): SymEff[Rep[Unit]] =
    for {
      ssu <- select[E, (SS, Unit)](res)
      _ <- put[Rep[SS], E](ssu._1)
    } yield ssu._2

  def getStore: SymEff[Rep[Store]] = for { s <- get[Rep[SS], E] } yield s._1

  def getPC: SymEff[Rep[PC]] = for { s <- get[Rep[SS], E] } yield s._2

  def putStore(x: Rep[String], v: Rep[Value]): SymEff[Rep[Unit]] =
    for {
      s <- get[Rep[SS], E]
      _ <- put[Rep[SS], E]((s._1 + (x -> v), s._2))
    } yield ()

  def putPathCond(e: Expr): SymEff[Rep[Unit]] =
    for {
      s <- get[Rep[SS], E]
      _ <- put[Rep[SS], E]((s._1, Set(e) ++ s._2))
    } yield ()

  def unfold(w: While, k: Int): Stmt =
    if (k == 0) Skip()
    else w match {
      case While(e, b) =>
        Cond(e, Seq(b, unfold(w, k-1)), Skip())
    }

  def eval(e: Expr): SymEff[Rep[Value]] = e match {
    case Lit(i: Int) => ret(IntV(i))
    case Lit(b: Boolean) => ret(BoolV(b))
    case Input() => readInt
    case Var(x) =>
      for {
        σ <- getStore
      } yield σ(x)
    case Op1("-", e) =>
      for {
        v <- eval(e)
      } yield opNeg(v)
    case Op2(op, e1, e2) =>
      for {
        v1 <- eval(e1)
        v2 <- eval(e2)
      } yield evalOp2(op, v1, v2)
  }

  def execWithPC(s: Stmt, pc: Expr): Comp[E, Rep[Unit]] =
    for {
      _ <- putPathCond(pc)
      _ <- exec(s)
    } yield ()

  def if_updown(s: Rep[SS], cnd: => Rep[Boolean],
                e1: => SymEff[Rep[Unit]],
                e2: => SymEff[Rep[Unit]]): SymEff[Rep[Unit]] = {
    reflect(if (cnd) reify(s)(e1) else reify(s)(e2))
  }

  def probChoice(s: Rep[SS], e1: => SymEff[Rep[Unit]], e2: => SymEff[Rep[Unit]]): SymEff[Rep[Unit]] = {
    val r: Rep[Int] = Wrap[Int](Adapter.g.reflectWrite("randInt", Unwrap(unit(100)))(Adapter.CTRL))
    if_updown(s, r <= 50, e1, e2)
  }

  // Statically unfold the While to Cond for k times
  def execWhileUnfold(exec: Stmt => SymEff[Rep[Unit]])(k: Int, w: While): SymEff[Rep[Unit]] = {
    exec(unfold(w, k))
  }

  def continue(c: Rep[Value]): SymEff[Rep[Value]] = {
    ret(Wrap[Value](Adapter.g.reflectWrite("continue_loop", Unwrap(c))(Adapter.CTRL)))
  }

  // TODO: write
  def exec(s: Stmt): SymEff[Rep[Unit]] =
    s match {
      case Skip() => ret(())
      case Assign(x, e) =>
        for {
          v <- eval(e)
          _ <- putStore(x, v)
        } yield ()
      case Seq(s1, s2) =>
        for {
          _ <- exec(s1)
          _ <- exec(s2)
        } yield ()
      case Cond(e, s1, s2) =>
        for {
          b <- eval(e)
          s <- get[Rep[SS], E]
          u <- if_updown(s, b.isBool,
            if_updown(s, b.getBool,
              execWithPC(s1, e),
              execWithPC(s2, Op1("-", e))),
            choice(execWithPC(s1, e), execWithPC(s2, Op1("-", e)))
            //probChoice(s, execWithPC(s1, e), execWithPC(s2, Op1("-", e)))
          )
        } yield u
      case While(e, s) =>
        def loop: Rep[SS => List[(SS, Unit)]] = fun { ss =>
          reify(ss)(for {
            c <- eval(e)
            v <- continue(c)
            u <- if_updown(ss, v.getBool,
              for {
                _ <- execWithPC(s, e)
                ss_* <- get[Rep[SS], E]
                r <- reflect(loop(ss_*))
              } yield r,
              for {
                r <- execWithPC(Skip(), Op1("-", e))
              } yield r)
          } yield u)
        }
        for {
          s <- get[Rep[SS], E]
          r <- reflect(loop(s))
        } yield r
      case Assert(e) =>
        putPathCond(e)
      case Output(e) =>
        for {
          v <- eval(e)
          _ <- writeInt[E](v)
        } yield ()
    }
}

trait StagedSymImpEffDriver[A, B] extends GenericSymStagedImpDriver[A, B] with StagedSymImpEff

trait StagedCppSymImpEffDriver[A, B] extends GenericCppSymStagedImpDriver[A, B] with StagedSymImpEff

object StagedSymFreer {
  @virtualize
  def specSym(s: Stmt): SAIDriver[Unit, Unit] =
    new StagedSymImpEffDriver[Unit, Unit] {
      def snippet(u: Rep[Unit]) = {
        // make two conditions symbolic
        val init: Rep[SS] = (Map("x" -> IntV(3), "z" -> IntV(4), "y" -> SymV("y")), Set[Expr]())
        // all concrete values
        // val init: Rep[SS] = (Map("x" -> IntV(3), "z" -> IntV(4), "y" -> IntV(5)), Set[Expr]())
        val v = reify(init)(exec(s))
        println(v)
        println("path number: ")
        println(v.size)
      }
    }

  @virtualize
  def specSymCpp(s: Stmt): CppSAIDriver[Int, Unit] =
    new StagedCppSymImpEffDriver[Int, Unit] {
      def snippet(u: Rep[Int]) = {
        // make two conditions symbolic
        val init: Rep[SS] = (Map("x" -> IntV(3), "z" -> IntV(4), "y" -> SymV("y")), Set[Expr]())
        // all concrete values
        // val init: Rep[SS] = (Map("x" -> IntV(3), "z" -> IntV(4), "y" -> IntV(5)), Set[Expr]())
        val v = reify(init)(exec(s))
        // println(v)
        println("path number: ")
        println(v.size)
        println(v(0)._1._1)
      }
    }

  def main(args: Array[String]): Unit = {
    import Examples._
    {
      val code = specSymCpp(unboundLoop)
      code.save("uloop.cpp")
      //println(code.code)
      code.eval(1)
    }
    {
      val code = specSymCpp(cond3)
      code.save("cond3.cpp")
      //code.eval(1)
    }
  }
}
