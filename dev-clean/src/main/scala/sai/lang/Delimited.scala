package sai.lang.delimited

trait Expr
case class Lit(n: Int) extends Expr
case class Var(x: String) extends Expr
case class Plus(e1: Expr, e2: Expr) extends Expr
case class Minus(e1: Expr, e2: Expr) extends Expr
case class Mult(e1: Expr, e2: Expr) extends Expr
case class Nil() extends Expr
case class Cons(hd: Expr, tl: Expr) extends Expr
case class Head(e: Expr) extends Expr
case class Tail(e: Expr) extends Expr
case class IsNil(e: Expr) extends Expr
case class If0(cnd: Expr, thn: Expr, els: Expr) extends Expr
case class App(e1: Expr, e2: Expr) extends Expr
case class Lam(x: String, body: Expr) extends Expr
case class CallCC(k: String, body: Expr) extends Expr
case class Shift(k: String, body: Expr) extends Expr
case class Reset(e: Expr) extends Expr

case class Control(k: String, body: Expr) extends Expr
case class Prompt(e: Expr) extends Expr

// TODO: mutation, letrec

/** shift/reset of Danvy & Filinski:
  *   M[reset(v)] => M[v]
  *   M[reset(E[shift(k, e)])] => M[reset(e[k ↦ λx.reset(E[x])])]
  *     where E has no reset
  * 
  * control/prompt of Felleisen:
  *   M[control(v)] => M[v]
  *   M[prompt(E[control(k, e)])] => M[prompt(e[k ↦ λx.E[x]])]
  *     where E has no prompt
  */

object Eval {
  trait Value
  case class IntV(n: Int) extends Value
  case class CloV(l: Lam, env: Env) extends Value
  case class ContV(k: Value => Cont => MCont => Value) extends Value
  case class ListV(xs: List[Value]) extends Value

  implicit class ValueOps(v: Value) {
    def +(w: Value): Value = (v, w) match {
      case (IntV(x), IntV(y)) => IntV(x + y)
    }
    def -(w: Value): Value = (v, w) match {
      case (IntV(x), IntV(y)) => IntV(x - y)
    }
    def *(w: Value): Value = (v, w) match {
      case (IntV(x), IntV(y)) => IntV(x * y)
    }
    def ∷(w: Value): Value = w match {
      case ListV(xs) => ListV(v :: xs)
    }
    def head: Value = v match { case ListV(x::xs) => x }
    def tail: Value = v match { case ListV(x::xs) => ListV(xs) }
  }

  type Env = Map[String, Value]
  type Cont = MCont => Value => Value
  type MCont = Value => Value

  def eval(e: Expr, ρ: Env, κ: Cont, γ: MCont): Value =
    e match {
      case Lit(n) => κ(γ)(IntV(n))
      case Var(x) => κ(γ)(ρ(x))
      case Plus(e1, e2) =>
        eval(e1, ρ, γ1 => v1 => eval(e2, ρ, γ2 => v2 => κ(γ2)(v1 + v2), γ1), γ)
      case Minus(e1, e2) =>
        eval(e1, ρ, γ1 => v1 => eval(e2, ρ, γ2 => v2 => κ(γ2)(v1 - v2), γ1), γ)
      case Mult(e1, e2) =>
        eval(e1, ρ, γ1 => v1 => eval(e2, ρ, γ2 => v2 => κ(γ2)(v1 * v2), γ1), γ)
      case Nil() => κ(γ)(ListV(List()))
      case Cons(hd, tl) =>
        eval(hd, ρ, γ1 => v1 => eval(tl, ρ, γ2 => v2 => κ(γ2)(v1 ∷ v2), γ1), γ)
      case Head(e) => eval(e, ρ, γ1 => v => κ(γ1)(v.head), γ)
      case Tail(e) => eval(e, ρ, γ1 => v => κ(γ1)(v.tail), γ)
      case IsNil(e) =>
        eval(e, ρ, γ1 => {
          case ListV(List()) => κ(γ1)(IntV(0))
          case _ => κ(γ1)(IntV(1))
        }, γ)
      case If0(cnd, thn, els) =>
        eval(cnd, ρ, γ1 => {
          case IntV(0) => eval(thn, ρ, κ, γ1)
          case _ => eval(els, ρ, κ, γ1)
        }, γ)
      case App(e1, e2) =>
        eval(e1, ρ, γ1 => v1 => eval(e2, ρ, γ2 => v2 =>
          v1 match {
            case CloV(Lam(x, body), λρ) => eval(body, λρ + (x → v2), κ, γ2)
            case ContV(k) => k(v2)(κ)(γ)
          }, γ1), γ)
      case Lam(x, body) => κ(γ)(CloV(Lam(x, body), ρ))
      case CallCC(k, body) =>
        eval(body, ρ + (k → ContV(v => κ_* => γ_* => κ(γ_*)(v))), κ, γ)
      case Shift(k, body) =>
        eval(body, ρ + (k → ContV(v => κ_* => γ_* => κ(κ_*(γ_*))(v))), γ => γ, γ)
      case Reset(e) => eval(e, ρ, γ => γ, κ(γ))
      // Note: a HO evaluator for control/prompt must use a trail of contexts (Danvy & Millikin 2009)
      case Control(k, body) => ???
      case Prompt(e) => ???
    }

  def eval(e: Expr): Value = eval(e, Map(), γ => γ, v => v)

  def main(args: Array[String]): Unit = {
    println(eval(Cons(Lit(1), Cons(Lit(2), Cons(Lit(3), Nil())))))
    println(eval(IsNil(Cons(Lit(1), Cons(Lit(2), Cons(Lit(3), Nil()))))))
    println(eval(If0(IsNil(Cons(Lit(1), Cons(Lit(2), Cons(Lit(3), Nil())))), Lit(1), Lit(2))))

    // Y ≡ (λf.λF.F1 (λx.f1 f1 F1 x)) (λf.λF.F1 (λx.f1 f1 F1 x))
    val Y = App(Lam("f1", Lam("F1", App(Var("F1"), Lam("x", App(App(App(Var("f1"), Var("f1")), Var("F1")), Var("x")))))),
                Lam("f2", Lam("F2", App(Var("F2"), Lam("y", App(App(App(Var("f2"), Var("f2")), Var("F2")), Var("y")))))))
    // Y2 = λt.(λf.t(λz.f f z)) (λf.t(λz.f f z))
    val Y2 = Lam("t", App(
      Lam("f", App(Var("t"), Lam("z", App(App(Var("f"), Var("f")), Var("z"))))),
      Lam("f", App(Var("t"), Lam("z", App(App(Var("f"), Var("f")), Var("z")))))))

    val fact = Lam("g", Lam("n", If0(Var("n"), Lit(1), Mult(Var("n"), App(Var("g"), Minus(Var("n"), Lit(1)))))))
    println(eval(App(App(Y, fact), Lit(5))))

    val foldPlus = Lam("f", Lam("xs", If0(IsNil(Var("xs")), Lit(0), Plus(Head(Var("xs")), App(Var("f"), Tail(Var("xs")))))))
    println(eval(App(App(Y, foldPlus), Cons(Lit(1), Cons(Lit(2), Cons(Lit(3), Nil()))))))

    /** Tests for shift/reset **/

    val visitS = Lam("f", Lam("xs",
      If0(IsNil(Var("xs")),
        Nil(),
        App(Var("f"), Shift("k", Cons(Head(Var("xs")), App(Var("k"), Tail(Var("xs")))))))))
    println(eval(Reset(App(App(Y, visitS), Cons(Lit(1), Cons(Lit(2), Cons(Lit(3), Nil()))))))) // expecting List(1, 2, 3)

    // 1 + reset { 2 + reset { 4 + shift k { shift k2 8 } } } ≡ 11
    assert(eval(Plus(Lit(1), Reset(Plus(Lit(2), Reset(Plus(Lit(4), Shift("k", Shift("k2", Lit(8))))))))) == IntV(11))

    //(reset (+ 2 (shift k (k 5)))) ≡ 7
    assert(eval(Reset(Plus(Lit(2), Shift("k", App(Var("k"), Lit(5)))))) == IntV(7))

    //(reset (+ 2 (shift k 5))) ≡ 5
    assert(eval(Reset(Plus(Lit(2), Shift("k", Lit(5))))) == IntV(5))

    //(reset (+ 2 (shift k (+ 1 (shift k1 (k 6))))))
    assert(eval(Reset(Plus(Lit(2), Shift("k", Plus(Lit(1), Shift("k1", App(Var("k"), Lit(6)))))))) == IntV(8))

    //(reset (+ 2 (shift k (shift k1 (shift k2 (k2 6))))))
    assert(eval(Reset(Plus(Lit(2), Shift("k", Shift("k1", Shift("k2", App(Var("k2"), Lit(6)))))))) == IntV(6))

    /*
    /** Tests for control/prompt **/
    val visitF = Lam("f", Lam("xs",
      If0(IsNil(Var("xs")),
        Nil(),
        App(Var("f"), Control("k", Cons(Head(Var("xs")), App(Var("k"), Tail(Var("xs")))))))))

    println(eval(Prompt(App(App(Y, visitS), Cons(Lit(1), Cons(Lit(2), Cons(Lit(3), Nil()))))))) // expecting List(3, 2, 1)

    //(prompt (+ 2 (control k (k 5)))) ≡ 7
    println(eval(Prompt(Plus(Lit(2), Control("k", App(Var("k"), Lit(5)))))))
    assert(eval(Prompt(Plus(Lit(2), Control("k", App(Var("k"), Lit(5)))))) == IntV(7))

    //(prompt (+ 2 (control k 5))) ≡ 5
    //println(eval(Prompt(Plus(Lit(2), Control("k", Lit(5))))))
    assert(eval(Prompt(Plus(Lit(2), Control("k", Lit(5))))) == IntV(5))

    //(prompt (+ 2 (control k (+ 1 (control k1 (k 6))))))
    assert(eval(Prompt(Plus(Lit(2), Control("k", Plus(Lit(1), Control("k1", App(Var("k"), Lit(6)))))))) == IntV(8))

    //(prompt (+ 2 (control k (control k1 (control k2 (k2 6))))))
    assert(eval(Prompt(Plus(Lit(2), Control("k", Control("k1", Control("k2", App(Var("k2"), Lit(6)))))))) == IntV(6))
     */
  }
}
