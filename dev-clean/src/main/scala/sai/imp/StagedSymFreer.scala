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
}

// Staged symbolic IO effect

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

  def run[E <: Eff, A]: Comp[IO ⊗ E, A] => Comp[E, A] =
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

// Staged SMT effect (using STP)

@virtualize
trait StagedSymImpEff extends Vals with SAIOps with StagedNondet {
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

  // type E = IO ⊗ (State[Rep[SS], *] ⊗ (Nondet ⊗ ∅))
  type E = State[Rep[SS], *] ⊗ (Nondet ⊗ ∅)
  type SymEff[T] = Comp[E, T]

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

  def reify(s: Rep[SS])(comp: Comp[E, Rep[Unit]]): Rep[List[(SS, Unit)]] = {
    val p1: Comp[Nondet ⊗ ∅, (Rep[SS], Rep[Unit])] =
      State.run2[Nondet ⊗ ∅, Rep[SS], Rep[Unit]](s)(comp)
    val p2: Comp[Nondet ⊗ ∅, Rep[(SS, Unit)]] = p1.map(a => a)
    val p3: Comp[∅, Rep[List[(SS, Unit)]]] = runRepNondet(p2)
    p3
  }

  def reflect(res: Rep[List[(SS, Unit)]]): SymEff[Rep[Unit]] =
    for {
      ssu <- select[E, (SS, Unit)](res)
      _ <- put[Rep[SS], E](ssu._1)
    } yield ssu._2

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
      opNeg(v)
    case Op2(op, e1, e2) =>
      val v1 = eval(e1, σ)
      val v2 = eval(e2, σ)
      evalOp2(op, v1, v2)
  }

  def eval(e: Expr): SymEff[Rep[Value]] = for { σ <- getStore } yield eval(e, σ)

  def execWithPC(s: Stmt, pc: Expr): Comp[E, Rep[Unit]] =
    for {
      _ <- putPathCond(pc)
      _ <- exec(s)
    } yield ()

  def if_updown(s: Rep[SS], cnd: => Rep[Boolean], e1: => SymEff[Rep[Unit]], e2: => SymEff[Rep[Unit]]): SymEff[Rep[Unit]] = {
    reflect(if (cnd) reify(s)(e1) else reify(s)(e2))
  }

  def probChoice(s: Rep[SS], e1: => SymEff[Rep[Unit]], e2: => SymEff[Rep[Unit]]): SymEff[Rep[Unit]] = {
    val r: Rep[Int] = Wrap[Int](Adapter.g.reflectWrite("randInt", Unwrap(unit(100)))(Adapter.CTRL))
    if_updown(s, r <= 50, e1, e2)
  }

  def genSelect[A: Manifest, B: Manifest](i: Rep[Int], xs: List[Rep[A]], k: Rep[A] => Rep[B]): Rep[Int => Rep[B]] = {
    def select(xs: List[(Rep[A], Int)]): Rep[B] = {
      if (xs.isEmpty) unchecked[B]("???")
      else {
        if (i == xs.head._2) k(xs.head._1)
        else select(xs.tail)
      }
    }
    ???
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
