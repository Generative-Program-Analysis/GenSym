package sai.parser.direct

sealed trait Expr

case class Bind(x: String, e: Expr)

case class Var(x: String) extends Expr
case class Num(i: Int) extends Expr
case class If(cnd: Expr, thn: Expr, els: Expr) extends Expr
case class App(e1: Expr, e2: Expr) extends Expr
case class Lam(x: String, body: Expr) extends Expr
case class PrimOp(op: String, e1: Expr, e2: Expr) extends Expr
case class Let(x: String, e: Expr, body: Expr) extends Expr // TODO: multiple bindings
case class Letrec(bds: List[Bind], body: Expr) extends Expr

sealed trait Value

case class NumV(i: Int) extends Value
case class ClosureV[Env](lam: Lam, env: Env) extends Value
