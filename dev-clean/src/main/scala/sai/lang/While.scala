package sai.lang

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

  def main(args: Array[String]): Unit = {
    import WhileLang.Examples._
    println(exec(fact5)(Map())(σ => σ("fact")))
  }
}
