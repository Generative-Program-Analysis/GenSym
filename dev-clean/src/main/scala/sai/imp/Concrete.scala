package sai.imp

import sai.lang.ImpLang._
import sai.structure.monad._
import sai.structure.monad.CpsM._

// A concrete semantics of Imp using monad transformers.

object ImpSemantics {
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
    case Input() =>
      ???
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
    case While(e, s) =>
      fix((f: Store => M[Store]) => (σ: Store) => {
        CpsM((k: Store => Ans) => {
          val BoolV(b) = eval(e, σ)
          if (b) exec(s)(σ)(σ1 => f(σ1)(k)) else k(σ)
        })
      })(σ)
    case Output(e) =>
      val v = eval(e, σ)
      System.out.println(v)
      pure(σ)
    case Assert(e) =>
      val IntV(v) = eval(e, σ)
      assert(v == 1)
      pure(σ)
  }
}

