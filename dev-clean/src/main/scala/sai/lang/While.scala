package sai.lang

import lms.core._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

import sai.lmsx._

object WhileLang {
  sealed trait Stmt
  case class Skip() extends Stmt
  case class Assign(x: String, e: Expr) extends Stmt
  case class Cond(e: Expr, thn: Stmt, els: Stmt) extends Stmt
  case class Seq(s1: Stmt, s2: Stmt) extends Stmt
  case class While(b: Expr, s: Stmt) extends Stmt

  sealed trait Expr
  case class Lit(x: Any) extends Expr
  case class Var(x: String) extends Expr
  case class Op1(op: String, e: Expr) extends Expr
  case class Op2(op: String, e1: Expr, e2: Expr) extends Expr

  object Examples {
    val fact5 =
      Seq(Assign("i", Lit(1)),
          Seq(Assign("fact", Lit(1)),
              While(Op2("<=", Var("i"), Lit(5)),
                    Seq(Assign("fact", Op2("*", Var("fact"), Var("i"))),
                        Assign("i", Op2("+", Var("i"), Lit(1)))))))
  }
}

import sai.structure.monad._

object WhileSemantics {
  import WhileLang._
  import CpsM._

  trait Value
  case class IntV(i: Int) extends Value
  case class BoolV(b: Boolean) extends Value

  type Ans = Value
  type Store = Map[String, Value]
  type M[T] = CpsM[Ans, T]

  val M = Monad[M]
  import M._

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

  def fix[A, B](f: (A => B) => A => B): A => B = a => f(fix(f))(a)

  def exec(s: Stmt)(σ: Store): M[Store] = s match {
    case Skip() => pure(σ)
    case Assign(x, e) => pure(σ + (x → eval(e, σ)))
    case Cond(e, s1, s2) =>
      val BoolV(b) = eval(e, σ)
      if (b) exec(s1)(σ) else exec(s2)(σ)
    case Seq(s1, s2) =>
      exec(s1)(σ).flatMap(σ => exec(s2)(σ))
    case While(e, s) => fix((f: Store => M[Store]) => (σ: Store) => {
                              CpsM((k: Store => Ans) => {
                                     val BoolV(b) = eval(e, σ)
                                     if (b) exec(s)(σ)(σ1 => f(σ1)(k)) else k(σ)
                                   })
                            })(σ)
  }
}

@virtualize
trait StagedWhileSemantics extends SAIOps {
  import WhileLang._
  import CpsM._

  type Ans = Store 
  type Store = Map[String, Value]
  type M[T] = CpsM[Ans, T]

  val M = Monad[M]
  import M._

  trait Value
  def IntV(i: Rep[Int]): Rep[Value] =
    Wrap[Value](Adapter.g.reflect("IntV", Unwrap(i)))
  def BoolV(b: Rep[Boolean]): Rep[Value] =
    Wrap[Value](Adapter.g.reflect("BoolV", Unwrap(b)))
  def rep_int_proj(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
    case Adapter.g.Def("IntV", scala.collection.immutable.List(v: Backend.Exp)) => Wrap[Int](v)
    case _ => Wrap[Int](Adapter.g.reflect("IntV-proj", Unwrap(i)))
  }
  def rep_bool_proj(b: Rep[Value]): Rep[Boolean] = Unwrap(b) match {
    case Adapter.g.Def("BoolV", scala.collection.immutable.List(v: Backend.Exp)) => Wrap[Boolean](v)
    case _ => Wrap[Boolean](Adapter.g.reflect("BoolV-proj", Unwrap(b)))
  }

  def eval(e: Expr, σ: Rep[Store]): Rep[Value] = e match {
    case Lit(i: Int) => IntV(i)
    case Lit(b: Boolean) => BoolV(b)
    case Var(x) => σ(x)
    case Op1("-", e) =>
      val i = rep_int_proj(eval(e, σ))
      IntV(-i)
    case Op2(op, e1, e2) =>
      val i1 = rep_int_proj(eval(e1, σ))
      val i2 = rep_int_proj(eval(e2, σ))
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
}

trait StagedWhileGen extends SAICodeGenBase {
  override def shallow(n: Node): Unit = n match {
    case Node(s, "IntV", List(i), _) =>
      emit("IntV(")
      shallow(i)
      emit(")")
    case Node(s, "IntV-proj", List(i), _) =>
      shallow(i)
      emit(".i")
    case Node(s, "BoolV", List(b), _) =>
      emit("BoolV(")
      shallow(b)
      emit(")")
    case Node(s, "BoolV-proj", List(i), _) =>
      shallow(i)
      emit(".b")
    case _ => super.shallow(n)
  }
}

trait StagedWhileDriver extends SAIDriver[Unit, Unit] with StagedWhileSemantics { q =>
  override val codegen = new ScalaGenBase with StagedWhileGen {
    val IR: q.type = q
    import IR._
    override def remap(m: Manifest[_]): String = {
      if (m.toString.endsWith("$Value")) "Value"
      else super.remap(m)
    }
  }

  override val prelude =
"""
import sai.lang.WhileLang._
trait Value
case class IntV(i: Int) extends Value
case class BoolV(b: Boolean) extends Value
"""
}

object TestWhile {
  import WhileLang._
  import WhileLang.Examples._

  @virtualize
  def specialize(e: Expr): SAIDriver[Unit, Unit] = new StagedWhileDriver {
    def snippet(u: Rep[Unit]) = {
      val v = eval(e, Map())
      println(v)
    }
  }

  @virtualize
  def specialize(s: Stmt): SAIDriver[Unit, Unit] = new StagedWhileDriver {
    def snippet(u: Rep[Unit]) = {
      val v = exec(s)(Map())(s => s)
      println(v)
    }
  }

  def main(args: Array[String]): Unit = {
    //println(exec(fact5)(Map())(σ => σ("fact")))
    //val code = specialize(Op2("+", Lit(1), Lit(2)))
    val code = specialize(fact5)
    println(code.code)
    code.eval(())
  }
}
