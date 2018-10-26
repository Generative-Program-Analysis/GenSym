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
  case class CloV(λ: Lam, ρ: Env) extends Value with Expr
  case class PrimV(f: List[Value] => Value) extends Value with Expr
  case class VoidV() extends Value with Expr
  case class SymbolV(s: String) extends Value with Expr

  def alloc(σ: Store): Addr = σ.keys.size + 1
}

import CESK._

object BigStepCES {

  // evaluates a list of Exprs in order
  // returns List of evaluated values and an ending state
  def interpListOfExprs(
    l: List[Expr],
    env:Env,
    sigma: Store
  ): (List[Value], Store) = l match {
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

  def interpCondBranches(
    branches: List[CondBrTrait],
    env: Env,
    sigma: Store
  ): (Value, Store) = branches match {
    case Nil => (VoidV(), sigma)
    case e::el =>
      val (cndv, cnds) = interp(e.cnd, env, sigma)
      cndv match {
        case BoolV(false) => interpCondBranches(el, env, cnds)
        case _ =>
          e match {
            case CondBr(_, thn) => interp(thn, env, cnds)
            case CondProcBr(_, thn) =>
              val addr = alloc(cnds)
              val (CloV(Lam(List(param), body), env_*), thns) = interp(thn, env, cnds)
              interp(body, env_* + (param -> addr), thns + (addr -> cndv))
          }
      }
  }

  def interpAndCompare(
    v: Value,
    cases: List[Expr],
    env: Env,
    sigma: Store
  ): (Boolean, Store) = cases match {
    case Nil => (false, sigma)
    case c::cs =>
      val (ev, es) = interp(c, env, sigma)
      if (ev == v) (true, es)
      else interpAndCompare(v, cs, env, es)
  }

  def interpCaseBranches(
    v: Value,
    branches: List[CaseBranch],
    env: Env,
    sigma: Store
  ): (Value, Store) = branches match {
    case Nil => (VoidV(), sigma)
    case e::el =>
      e.cases match {
        case Nil => // Case is always true
          interp(e.thn, env, sigma)
        case _ => // compare v with all exprs in the case
          val (eq, es) = interpAndCompare(v, e.cases, env, sigma)
          if (eq) interp(e.thn, env, es)
          else interpCaseBranches(v, el, env, es)
      }
  }

  def interp(e: Expr, env: Env, sigma: Store): (Value, Store) = e match {
    case Symbol(x) => (SymbolV(x), sigma)
    case IntLit(x) => (NumV(x), sigma)
    case BoolLit(x) => (BoolV(x), sigma)
    case CharLit(x) => (CharV(x), sigma)
    case Var(x) => (sigma(env(x)), sigma)
    case l@Lam(_, _) => (CloV(l, env), sigma)
    case If(cnd, thn, els) =>
      val (ev, es) = interp(cnd, env, sigma)
      ev match {
        case BoolV(false) => interp(els, env, es)
        case _ => interp(thn, env, es)
      }

    case Cond(branches) => interpCondBranches(branches, env, sigma)
    case Case(e, branches) =>
      val (ev, es) = interp(e, env, sigma)
      interpCaseBranches(ev, branches, env, es)

    case App(e, params) =>
      val (ev, es) = interp(e, env, sigma)
      val (appvs, es_) = interpListOfExprs(params, env, es)

      ev match {
        case PrimV(f) => (f(appvs), es_)
        case CloV(Lam(args, lbody), cenv) =>
          val (es__, addrs): (Store, List[Addr]) = appvs.foldRight (es_, List[Addr]()) {
            case (v, (es, addrs)) =>
              val addr = alloc(es)
              (es + (addr -> v), addr::addrs)
          }
          interp(lbody, cenv ++ (args zip addrs), es__)
      }
    case Void() => (VoidV(), sigma)
    case Set_!(x, e) =>
      val (ev, es) = interp(e, env, sigma)
      (VoidV(), es + (env(x) -> ev))
    case Begin(l) => interpSeq(l, env, sigma)
    case Define(x: String, e: Expr) => ??? //TODO (yuxuan): Find a way to implement these imperative features
  }

  def plus = PrimV({ (l: List[Value]) => l.tail.foldRight(l.head) { case (NumV(v1), NumV(v2)) => NumV(v1 + v2) } })
  def minus = PrimV({ (l: List[Value]) => l.tail.foldRight(l.head) { case (NumV(v1), NumV(v2)) => NumV(v1 - v2) } })
  def mul = PrimV({ (l: List[Value]) => l.tail.foldRight(l.head)  { case (NumV(v1), NumV(v2)) => NumV(v1 * v2) } })
  def div = PrimV({ (l: List[Value]) => l.tail.foldRight(l.head)  { case (NumV(v1), NumV(v2)) => NumV(v1 / v2) } })
  def listdec = PrimV({ ListV(_) })
  def vecdec = PrimV({ (l: List[Value]) => VectorV(Vector() ++ l) })
  def car = PrimV({ case List(ListV(l)) => l.head })
  def cdr = PrimV({ case List(ListV(l)) => ListV(l.tail) })

  val initEnv = Map(
    "+" -> 1,
    "-" -> 2,
    "*" -> 3,
    "/" -> 4,
    "list" -> 5,
    "vector" -> 6,
    "car" -> 7,
    "cdr" -> 8
  )

  val initStore = Map(
    1 -> plus,
    2 -> minus,
    3 -> mul,
    4 -> div,
    5 -> listdec,
    6 -> vecdec,
    7 -> car,
    8 -> cdr
  )

  def eval(e: Expr): (Value, Store) = interp(e, initEnv, initStore)
}

object CESKTest {
  def main(args: Array[String]) = {
    assert(BigStepCES.eval(IntLit(1))._1 == NumV(1))

    assert(BigStepCES.eval(
      App(Lam(List("x", "y"), Var("y")), List(IntLit(3), IntLit(4))))._1
      == NumV(4))

    assert(BigStepCES.eval(
      Case(
        IntLit(3),
        List(
          CaseBranch(List(IntLit(1), BoolLit(true)), BoolLit(false)),
          CaseBranch(List(IntLit(3), IntLit(4)), BoolLit(true)))))._1
      == BoolV(true))

    assert(BigStepCES.eval(
      Case(
        IntLit(3),
        List(
          CaseBranch(List(IntLit(1), BoolLit(true)), BoolLit(false)),
          CaseBranch(List(IntLit(7), IntLit(4)), BoolLit(true)),
          CaseBranch(List(), IntLit(42)))))._1
      == NumV(42))

    assert(BigStepCES.eval(
      Cond(List(
        CondBr(BoolLit(false), IntLit(103)),
        CondBr(IntLit(2), IntLit(104)),
        CondProcBr(IntLit(7), Lam(List("x"), Var("x"))))))._1
      == NumV(104))

    assert(BigStepCES.eval(
      Cond(List(
        CondBr(BoolLit(false), IntLit(103)),
        CondProcBr(IntLit(7), Lam(List("x"), Var("x"))))))._1
      == NumV(7))

    assert(BigStepCES.eval(
      App(Var("+"), List(IntLit(2), IntLit(3))))._1 == NumV(5))

    assert(BigStepCES.eval(
      App(Var("car"), List(App(Var("list"), List(IntLit(5), IntLit(6), IntLit(7))))))._1
      == NumV(5))

    assert(BigStepCES.eval(
      App(Var("cdr"), List(App(Var("list"), List(IntLit(5), IntLit(6), IntLit(7))))))._1
      == ListV(List(NumV(6), NumV(7))))
  }
}
