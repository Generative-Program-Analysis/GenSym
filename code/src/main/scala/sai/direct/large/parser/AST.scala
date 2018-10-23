package sai.direct.large.parser

trait Expr

case class Var(x: String) extends Expr
case class App(e1: Expr, param: List[Expr]) extends Expr
case class Lam(param: List[String], body: Expr) extends Expr

case class Bind(x: String, e: Expr) {
  def toSet: Set_! = Set_!(x, e)
}
case class Let(bds: List[Bind], body: Expr) extends Expr {
  def toApp: App = {
    val args = bds map { case Bind(name, _) => name }
    val vals = bds map { case Bind(_, value) => value }
    App(Lam(args, body), vals)
  }
}
case class Lrc(bds: List[Bind], body: Expr) extends Expr {
  def toApp: App =
    Let(bds.map { case Bind(x, _) => Bind(x, Void()) },
      Begin(bds.map(_.toSet) ++ List(body))).toApp
}

case class IntLit(x: Int) extends Expr
case class BoolLit(x: Boolean) extends Expr
case class CharLit(x: Char) extends Expr
case class If(cnd: Expr, thn: Expr, els: Expr) extends Expr

trait CondBrTrait { val cnd: Expr; val thn: Expr }
case class CondBr(cnd: Expr, thn: Expr) extends CondBrTrait
case class CondProcBr(cnd: Expr, thn: Expr) extends CondBrTrait
case class Cond(branches: List[CondBrTrait]) extends Expr

case class CaseBranch(cases: List[Expr], thn: Expr)
case class Case(e: Expr, branches: List[CaseBranch]) extends Expr

case class Void() extends Expr
case class Set_!(x: String, e: Expr) extends Expr
case class Begin(es: List[Expr]) extends Expr
case class Define(x: String, e: Expr) extends Expr
