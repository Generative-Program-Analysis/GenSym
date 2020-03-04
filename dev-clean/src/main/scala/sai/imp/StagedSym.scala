package sai.imp

import sai.lang.ImpLang._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

import scala.collection.immutable.{List => SList}

@virtualize
trait SymStagedImp extends SAIOps {
  import StateT._
  import ListT._

  trait Value
  def IntV(i: Rep[Int]): Rep[Value] =
    Wrap[Value](Adapter.g.reflect("IntV", Unwrap(i)))
  def BoolV(b: Rep[Boolean]): Rep[Value] =
    Wrap[Value](Adapter.g.reflect("BoolV", Unwrap(b)))
  def SymV(x: Rep[String]): Rep[Value] =
    Wrap[Value](Adapter.g.reflect("SymV", Unwrap(x)))

  /*
  def rep_int_proj(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
    case Adapter.g.Def("IntV", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Int](v)
    case _ =>
      Wrap[Int](Adapter.g.reflect("IntV-proj", Unwrap(i)))
  }

  def rep_bool_proj(b: Rep[Value]): Rep[Boolean] = Unwrap(b) match {
    case Adapter.g.Def("BoolV", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Boolean](v)
    case _ =>
      Wrap[Boolean](Adapter.g.reflect("BoolV-proj", Unwrap(b)))
  }
   */

  def op_neg(v: Rep[Value]): Rep[Value] = {
    Unwrap(v) match {
      case Adapter.g.Def("IntV", scala.collection.immutable.List(v: Backend.Exp)) =>
        val v1: Rep[Int] = Wrap[Int](v)
        IntV(-v1)
      case Adapter.g.Def("SymV", scala.collection.immutable.List(v: Backend.Exp)) =>
        val v1: Rep[String] = Wrap[String](v)
        SymV(unit("-" + v1))
      case i =>
        val v1: Rep[Int] = Wrap[Int](Adapter.g.reflect("IntV-proj", i))
        IntV(-v1)
    }
  }

  def op_2(op: String, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = {
    Wrap[Value](Adapter.g.reflect("op", Unwrap(unit(op)), Unwrap(v1), Unwrap(v2)))
  }

  type PC = Set[Expr]
  type Store = Map[String, Value]
  type Ans = (Store, PC)
  type M[T] = StateT[ListM, Ans, T] // List[(Unit, (Store, PC))]
  val M = Monad[M]

  def eval(e: Expr, σ: Rep[Store]): Rep[Value] = e match {
    case Lit(i: Int) => IntV(i)
    case Lit(b: Boolean) => BoolV(b)
    case Var(x) => σ(x)
    case Op1("-", e) =>
      val v = eval(e, σ)
      op_neg(v)
    case Op2(op, e1, e2) =>
      val v1 = eval(e1, σ)
      val v2 = eval(e2, σ)
      op_2(op, v1, v2)
  }

  def evalM(e: Expr): M[Value] = for {
    σ <- get_store
  } yield eval(e, σ)

  def get_store: M[Store] = for {
    ans <- MonadState[M, Ans].get
  } yield ans._1

  def update_store(x: String, v: Rep[Value]): M[Unit] = for {
    ans <- MonadState[M, Ans].get
    _ <- MonadState[M, Ans].put((ans._1 + (x -> v), ans._2))
  } yield ()

  def update_pc(e: Expr): M[Unit] = for {
    ans <- MonadState[M, Ans].get
    _ <- MonadState[M, Ans].put((ans._1, ans._2 ++ Set(e)))
  } yield ()

  def br(cnd: Expr, m1: M[Unit], m2: M[Unit]): M[Unit] = {
    val b1 = for {
      _ <- update_pc(cnd)
      _ <- m1
    } yield ()
    val b2 = for {
      _ <- update_pc(Op1("-", cnd))
      _ <- m2
    } yield ()
    StateT[ListM, Ans, Unit]((s: Rep[Ans]) => {
      b1.run(s) ++ b2.run(s)
    })
  }

  def fix[A: Manifest, B: Manifest](f: Rep[A] => Rep[B]): Rep[A => B] = fun(f)

  def exec(s: Stmt): M[Unit] = s match {
    case Skip() => M.pure(())
    case Assign(x, e) => for {
      v <- evalM(e)
      _ <- update_store(x, v)
    } yield ()
    case Cond(e, s1, s2) => for {
      cnd <- evalM(e)
      σ <- get_store
      _ <- br(e, exec(s1), exec(s2))
    } yield ()
    case Seq(s1, s2) => for {
      _ <- exec(s1)
      _ <- exec(s2)
    } yield ()
    case While(e, b) =>
      // TODO: add break
      /*
      def f: Rep[Ans => List[(Unit, Ans)]] = fix { s =>
        val ans = for {
          cnd <- evalM(e)
          σ <- get_store
          _ <- br(e, exec(b), exec(Skip()))
        } yield ()
        ans.run(s).run
      }
      */
      val k = 4
      exec(unfold(While(e, b), k))
  }

  def unfold(w: While, k: Int): Stmt =
    if (k == 0) Skip()
    else w match {
      case While(e, b) =>
        Cond(e, Seq(b, unfold(w, k-1)), Skip())
    }
}

trait SymStagedImpDriver extends SAIDriver[Unit, Unit] with SymStagedImp { q =>
  override val codegen = new ScalaGenBase with StagedImpGen {
    val IR: q.type = q
    import IR._
    override def remap(m: Manifest[_]): String = {
      if (m.toString == "java.lang.String") "String"
      else if (m.toString.endsWith("$Value")) "Value"
      else if (m.toString.endsWith("$Expr")) "Expr"
      else super.remap(m)
    }
  }

  override val prelude =
    """
import sai.lang.ImpLang._
import sai.imp.SymRuntime._
"""
}

object SymRuntime {
  trait Value
  case class IntV(i: Int) extends Value
  case class BoolV(b: Boolean) extends Value
  case class SymV(s: String) extends Value
  case class SymE(op: String, args: List[Value]) extends Value

  type PC = Set[Expr]
  type Store = Map[String, Value]

  def op_2(op: String, v1: Value, v2: Value): Value =
    (op, v1, v2) match {
      case ("+", IntV(i1), IntV(i2)) => IntV(i1 + i2)
      case ("-", IntV(i1), IntV(i2)) => IntV(i1 - i2)
      case ("*", IntV(i1), IntV(i2)) => IntV(i1 * i2)
      case ("/", IntV(i1), IntV(i2)) => IntV(i1 / i2)
      case ("==", IntV(i1), IntV(i2)) => BoolV(i1 == i2)
      case (">=", IntV(i1), IntV(i2)) => BoolV(i1 >= i2)
      case (">", IntV(i1), IntV(i2)) => BoolV(i1 > i2)
      case ("<=", IntV(i1), IntV(i2)) => BoolV(i1 <= i2)
      case ("<", IntV(i1), IntV(i2)) => BoolV(i1 < i2)
      case (op, x1, x2) => SymE(op, List(x1, x2))
    }
}
