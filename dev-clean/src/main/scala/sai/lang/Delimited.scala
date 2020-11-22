package sai.lang.delimited

trait Expr
case class Lit(n: Int) extends Expr
case class Var(x: String) extends Expr
case class Plus(e1: Expr, e2: Expr) extends Expr
case class App(e1: Expr, e2: Expr) extends Expr
case class Lam(x: String, body: Expr) extends Expr
case class CallCC(k: String, body: Expr) extends Expr
case class Shift(k: String, body: Expr) extends Expr
case class Reset(e: Expr) extends Expr

// TODO: add mutation and let

object Eval {
  trait Value
  case class IntV(n: Int) extends Value
  case class CloV(l: Lam, env: Env) extends Value
  case class ContV(k: Value => Cont => MCont => Value) extends Value

  implicit class ValueOps(v: Value) {
    def +(w: Value): Value = (v, w) match {
      case (IntV(x), IntV(y)) => IntV(x + y)
    }
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
    }

  def eval(e: Expr): Value = eval(e, Map(), γ => γ, v => v)

  def main(args: Array[String]): Unit = {
    // 1 + reset { 2 + reset { 4 + shift k { shift k2 8 } } }
    println(eval(
      Plus(Lit(1),
        Reset(Plus(Lit(2),
          Reset(Plus(Lit(4),
            Shift("k",
              Shift("k2", Lit(8))))))))))
  }
}
