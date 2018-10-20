package sai.direct.large.concrete

import sai.direct.large.parser._

object CESK {
  type Addr = Int
  type Env = Map[String, Addr]
  type Store = Map[Addr, Value]

  abstract class Value
  object NotAValue extends Value
  case class NumV(i: Int) extends Value with Expr
  case class CharV(c: Char) extends Value with Expr
  case class BoolV(b: Boolean) extends Value with Expr
  case class ListV(l: List[Value]) extends Value with Expr
  case class VectorV(v: Vector[Value]) extends Value with Expr
  case class CloV(λ: Lam, ρ: Env) extends Value
  case class VoidV() extends Value

  def alloc(σ: Store): Addr = σ.keys.size + 1
}

import CESK._

object BigStepCES {

  // evaluates a list of Exprs in order
  // returns List of evaluated values and an ending state
  def interpListOfExprs(l: List[Expr], env:Env, sigma: Store): (List[Value], Store) = l match {
    case Nil => (Nil, sigma)
    case e::el =>
      val (ev, es) = interp(e, env, sigma)
      val (elv, els) = interpListOfExprs(el, env, es)
      (ev :: elv, els)
  }

  def interpSeq(l: List[Expr], env: Env, sigma: Store): (Value, Store) = l match {
    case Nil => (VoidV(), sigma)
    case e::Nil => interp(e, env, sigma)
    case e::el =>
      val (_, es) = interp(e, env, sigma)
      interpSeq(el, env, es)
  }

  def interp(e: Expr, env: Env, sigma: Store): (Value, Store) = e match {
    case IntLit(x) => (NumV(x), sigma)
    case BoolLit(x) => (BoolV(x), sigma)
    case CharLit(x) => (CharV(x), sigma)
    case Var(x) => (sigma(env(x)), sigma)
    case l@Lam(_, _) => (CloV(l, env), sigma)
    case If(cnd, thn, els) =>
      val (ev, es) = interp(cnd, env, sigma)
      ev match {
        case BoolV(true) => interp(thn, env, es)
        case BoolV(false) => interp(els, env, es)
      }

    case Cond(branches) => ???
    case Case(e, branches) => ???
    case App(e, param) =>
      val (ev, es) = interp(e, env, sigma)
      val (appv, es_) = interpListOfExprs(param, env, es)

      val (es__, addrs): (Store, List[Addr]) = appv.foldRight (es_, List[Addr]()) {
        case (v, (es, addrs)) =>
          val addr = alloc(es)
          (es + (addr -> v), addr::addrs)
      }

      ev match {
        case CloV(Lam(args, lbody), cenv) =>
          interp(lbody, cenv ++ (args zip addrs), es__)
      }

    case Void() => (VoidV(), sigma)
    case Set_!(x, e) =>
      val (ev, es) = interp(e, env, sigma)
      (VoidV(), es + (env(x) -> ev))
    case Begin(l) => interpSeq(l, env, sigma)
    case Define(x: String, e: Expr) => ???
  }
  def eval(e: Expr): (Value, Store) = interp(e, Map(), Map())
}

object CESKTest {
  def main(args: Array[String]) = {
    assert(BigStepCES.eval(IntLit(1)) == (NumV(1), Map()))
    assert(BigStepCES.eval(
      App(Lam(List("x", "y"), Var("y")), List(IntLit(3), IntLit(4))))
      == (NumV(4), Map(1 -> NumV(4), 2 -> NumV(3))))
  }
}
