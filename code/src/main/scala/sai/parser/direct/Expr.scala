package sai.parser.direct

sealed trait Expr
sealed trait Value extends Expr

case class B(x: String, e: Expr)

case class Var(x: String) extends Expr
case class Num(i: Int) extends Expr with Value
case class If0(cnd: Expr, thn: Expr, els: Expr) extends Expr
case class App(e1: Expr, e2: Expr) extends Expr
case class Lam(x: String, body: Expr) extends Expr with Value
case class PrimOp(op: String, e1: Expr, e2: Expr) extends Expr
case class Let(x: String, e: Expr, body: Expr) extends Expr
case class Letrec(bds: List[B], body: Expr) extends Expr
