package sai.imp

import sai.lang.ImpLang._

import lms.core._
import lms.macros._
import lms.core.stub._
import lms.core.Backend._

import sai.lmsx._

import scala.collection.immutable.{List => SList}

@virtualize
trait StagedImpSemantics extends SAIOps {
  import StateT._

  type Ans = Store
  type Store = Map[String, Value]
  type M[T] = StateT[IdM, Ans, T]
  val M = Monad[M]

  trait Value
  def IntV(i: Rep[Int]): Rep[Value] = "IntV".reflectWith[Value](i)
  def BoolV(b: Rep[Boolean]): Rep[Value] = "BoolV".reflectWith[Value](b)

  implicit def rep_int_proj(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
    case Adapter.g.Def("IntV", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Int](v)
    case _ =>
      Wrap[Int](Adapter.g.reflect("IntV-proj", Unwrap(i)))
  }

  implicit def rep_bool_proj(b: Rep[Value]): Rep[Boolean] = Unwrap(b) match {
    case Adapter.g.Def("BoolV", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Boolean](v)
    case _ =>
      Wrap[Boolean](Adapter.g.reflect("BoolV-proj", Unwrap(b)))
  }

  def eval(e: Expr, σ: Rep[Store]): Rep[Value] = e match {
    case Lit(i: Int) => IntV(i)
    case Lit(b: Boolean) => BoolV(b)
    case Var(x) => σ(x)
    case Op1("-", e) =>
      val i: Rep[Int] = eval(e, σ)
      IntV(-i)
    case Op2(op, e1, e2) =>
      val i1: Rep[Int] = eval(e1, σ)
      val i2: Rep[Int] = eval(e2, σ)
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
    case Input() => ???
  }

  def get_state: M[Store] = MonadState[M, Store].get
  def update_state(x: String, v: Rep[Value]): M[Unit] =
    MonadState[M, Store].mod(s => s + (unit(x) -> v))
  def lift_state(s: Rep[Store]): M[Unit] =
    MonadState[M, Store].put(s)

  def evalM(e: Expr): M[Value] = for {
    σ <- get_state
  } yield eval(e, σ)

  def exec(s: Stmt): M[Unit] = s match {
    case Skip() => M.pure(())
    case Assign(x, e) => for {
      v <- evalM(e)
      _ <- update_state(x, v)
    } yield ()
    case Cond(e, s1, s2) => for {
      cnd <- evalM(e)
      σ <- get_state
      rt <- lift_state(if (cnd) exec(s1)(σ).run._2 else exec(s2)(σ).run._2)
    } yield ()
    case Seq(s1, s2) => for {
      _ <- exec(s1); _ <- exec(s2)
    } yield ()
    case While(e, b) =>
      def loop: Rep[Store => Store] = fun { s =>
        val ans = for {
          cnd <- evalM(e)
          σ <- get_state
          rt <- lift_state(if (cnd) loop(exec(b)(σ).run._2) else σ)
        } yield ()
        ans(s).run._2
      }
      for {
        σ <- get_state
        _ <- lift_state(loop(σ))
      } yield ()
    case Output(e) => ???
    case Assert(e) => ???
  }

  /* 
  //The CPS Monad doesn't work for the While case
  def fix[A, B](f: (Rep[A] => B) => Rep[A] => B): Rep[A] => B = { a =>
    f(fix(f))(a)
  }

  def exec(s: Stmt)(σ: Rep[Store]): M[Store] = s match {
    case Skip() => pure(σ)
    case Assign(x, e) => pure(σ + (x → eval(e, σ)))
    case Cond(e, s1, s2) =>
      val b = rep_bool_proj(eval(e, σ))
      pure(if (b) exec(s1)(σ)(s => s) else exec(s2)(σ)(s => s))
    case Seq(s1, s2) =>
      exec(s1)(σ).flatMap(σ => exec(s2)(σ))
    case While(e, s) =>
      fix((f: Rep[Store] => M[Store]) => (σ: Rep[Store]) => {
        CpsM((k: Rep[Store] => Rep[Ans]) => {
          val b = rep_bool_proj(eval(e, σ))
          if (b) exec(s)(σ)(σ1 => f(σ1)(k)) else k(σ)
        })
      })(σ)
  }
   */
}

trait StagedImpGen extends SAICodeGenBase {
  override def shallow(n: Node): Unit = n match {
    case Node(s, "IntV", List(i), _) =>
      emit("IntV(")
      shallow(i)
      emit(")")
    case Node(s, "IntV-proj", List(i), _) =>
      shallow(i)
      emit(".asInstanceOf[IntV].i")
    case Node(s, "BoolV", List(b), _) =>
      emit("BoolV(")
      shallow(b)
      emit(")")
    case Node(s, "BoolV-proj", List(i), _) =>
      shallow(i)
      emit(".asInstanceOf[BoolV].b")
    case _ => super.shallow(n)
  }
}

trait StagedImpDriver extends SAIDriver[Unit, Unit] with StagedImpSemantics { q =>
  override val codegen = new ScalaGenBase with StagedImpGen {
    val IR: q.type = q
    import IR._
    override def remap(m: Manifest[_]): String = {
      if (m.toString.endsWith("$Value")) "Value"
      else super.remap(m)
    }
  }

  override val prelude =
"""
import sai.lang.ImpLang._
trait Value
case class IntV(i: Int) extends Value
case class BoolV(b: Boolean) extends Value
"""
}

trait CppStagedImpGen extends CppSAICodeGenBase {
  registerHeader("./headers", "<sai_imp_concrete.hpp>")

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, name, _, _) if name.startsWith("IntV") => false
    case Node(_, name, _, _) if name.startsWith("BoolV") => false
    case _ => super.mayInline(n)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "IntV", List(i), _) =>
      emit("(struct IntV){")
      shallow(i)
      emit("}")
    case Node(s, "IntV-proj", List(i), _) =>
      emit("std::get<IntV>(")
      shallow(i)
      emit(").i")
    case Node(s, "BoolV", List(b), _) =>
      emit("(struct BoolV){")
      shallow(b)
      emit("}")
    case Node(s, "BoolV-proj", List(i), _) =>
      emit("std::get<BoolV>(")
      shallow(i)
      emit(").b")
    case _ => super.shallow(n)
  }
}

trait CppStagedImpDriver[A, B] extends CppSAIDriver[A, B] with StagedImpSemantics { q =>
  override val codegen = new CGenBase with CppStagedImpGen {
    val IR: q.type = q
    import IR._
    override def remap(m: Manifest[_]): String = {
      if (m.toString.endsWith("$Value")) "Value"
      else super.remap(m)
    }
  }
}
