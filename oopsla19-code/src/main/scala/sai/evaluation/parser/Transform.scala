package sai.evaluation.parser

/* Author: Yuxuan Chen */

object SchemeASTDesugar {
  var lastIdent = 0

  def newIdentLet(e: Expr)(f: Expr => Expr): App = {
    val prefix = "$"
    val id = lastIdent
    lastIdent += 1
    val newIdent: String = prefix + id
    App(Lam(List(newIdent), f(Var(newIdent))), List(e))
  }

  def desugarCondBranches(branches: List[CondBrTrait]): Expr =
    branches match {
      case Nil => Void()
      case x :: xs =>
        x match {
          case CondBr(cnd, thn) =>
            If(apply(cnd), apply(thn), desugarCondBranches(xs))
          case CondProcBr(cnd, thnl) =>
            newIdentLet(apply(cnd)) {
              τ => If(τ, App(apply(thnl), List(τ)), desugarCondBranches(xs))
            }
        }
    }

  def desugarCaseBranches(comp: Expr, branches: List[CaseBranch]): Expr =
    branches match {
      case Nil => Void()
      case x :: xs =>
        val CaseBranch(cases, thn) = x
        val xsCases = desugarCaseBranches(comp, xs)
        cases.foldRight(xsCases) {
          case (e, xsCases) => If(App(Var("eq?"), List(comp, e)), thn, xsCases)
        }
    }

  def foldDefine(seq: List[Expr]): (List[String], List[Expr]) =
    seq match {
      case Nil => (List(), List())
      case x :: xs =>
        val (ls, le) = foldDefine(xs)
        x match {
          case Define(n, v) => (n :: ls, Set_!(n, v) :: le)
          case _ => (ls, x :: le)
        }
    }

  def desugarSequence(seq: List[Expr]): Expr = {
    val (ls, le) = foldDefine(seq)
    val body = le match {
      case Nil => Void()
      case x :: Nil => apply(x)
      case x :: xs =>
        x match {
          case Define(n, v) =>
            App(Lam(List(n), newIdentLet(Set_!(n, apply(v))) { _ => desugarSequence(xs) }), List(Void()))
          case _ =>
            newIdentLet(apply(x)) { _ => desugarSequence(xs) }
        }
    }
    ls.foldLeft(body) {
      case (b, name) =>
        App(Lam(List(name), b), List(Void()))
    }
  }

  def apply(expr: Expr): Expr = expr match {
    case App(e1, param) => App(apply(e1), param map apply)
    case Lam(param, body) => Lam(param, apply(body))
    case If(cnd, thn, els) => If(apply(cnd), apply(thn), apply(els))
    case Cond(branches) => desugarCondBranches(branches)
    case Case(e, branches) =>
      newIdentLet(apply(e)) { v => desugarCaseBranches(v, branches) }
    case Set_!(x, e) => Set_!(x, apply(e))
    case Begin(es) => 
      Begin(es.map(apply)) 
      //desugarSequence(es)
    case Define(x, s) => Define(x, apply(s))
    case _ => expr
  }
}

