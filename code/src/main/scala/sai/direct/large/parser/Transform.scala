package sai.direct.large.parser

object LargeSchemeASTDesugar {
  var lastIdent = 0

  def newIdentLet(e: Expr)(f: Expr => Expr): App = {
    val prefix = "__$"
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

  def desugarSequence(seq: List[Expr]): Expr =
    seq match {
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

  def apply(expr: Expr): Expr = expr match {
    case App(e1, param) => App(apply(e1), param map apply)
    case Lam(param, body) => Lam(param, apply(body))
    case If(cnd, thn, els) => If(apply(cnd), apply(thn), apply(els))
    case Cond(branches) => desugarCondBranches(branches)
    case Case(e, branches) =>
      newIdentLet(apply(e)) { v => desugarCaseBranches(v, branches) }
    case Set_!(x, e) => Set_!(x, apply(e))
    case Begin(es) => desugarSequence(es)
    case Define(x, s) => Define(x, apply(s))
    case _ => expr
  }
}

object TestLargeSchemeDesugar {
  def main(args: Array[String]) = {
    assert(LargeSchemeASTDesugar(IntLit(1)) == IntLit(1))
    SExpPrinter(LargeSchemeASTDesugar(
      Begin(List(Define("x", IntLit(2)), Set_!("x", IntLit(3)), Var("x")))))
    SExpPrinter(LargeSchemeASTDesugar(
      Cond(List(
        CondBr(
          App(Var("positive?"),List(IntLit(-5))),
          App(Var("error"),List())),
        CondBr(App(Var("zero?"),List(IntLit(-5))),App(Var("error"),List())),
        CondBr(App(Var("positive?"),List(IntLit(5))),Sym("here"))))))
    SExpPrinter(LargeSchemeASTDesugar(
      Case(IntLit(3), List(
        CaseBranch(List(IntLit(3), IntLit(4), IntLit(5)), BoolLit(true)),
        CaseBranch(List(App(Lam(List(), IntLit(7)), List()), IntLit(6)), BoolLit(false))))))

    val Some(ast) = LargeSchemeParser("(define (pow a b) (if (eq? b 0) 1 (* a (pow a (- b 1))))) (pow 3 5)")
    SExpPrinter(LargeSchemeASTDesugar(ast))
    val Some(begin_in_begin) = LargeSchemeParser("(begin (begin a b) c d)")
    SExpPrinter(LargeSchemeASTDesugar(begin_in_begin))
  }
}
