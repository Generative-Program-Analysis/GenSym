package sai.direct.large.parser

trait Toplevel
trait Expr extends Toplevel

case class Var(x: String) extends Expr
case class App(e1: Expr, param: List[Expr]) extends Expr
case class Lam(param: List[String], body: Expr) extends Expr

case class Bind(x: String, e: Expr)
case class Let(bds: List[Bind], body: Expr) extends Expr
case class LetStar(bds: List[Bind], body: Expr) extends Expr
case class Lrc(bds: List[Bind], body: Expr) extends Expr

case class IntLit(x: Int) extends Expr
case class BoolLit(x: Boolean) extends Expr
case class If(cnd: Expr, thn: Expr, els: Expr) extends Expr

case class CondBranch(cnd: Expr, thn: Expr)
case class Cond(branches: List[CondBranch]) extends Expr

case class Define(x: String, e: Expr) extends Toplevel
