package sai.direct.core.parser

trait Control {
  val path = "sai.direct.core.parser"
}

trait Expr extends Control

case class Var(x: String) extends Expr {
  override def toString: String = path + ".Var(\"" + x + "\")"
}
case class App(e1: Expr, e2: Expr) extends Expr
case class Lam(x: String, body: Expr) extends Expr {
  override def toString: String = path + "Lam(\"" + x + "\", $body)"
}

case class Bind(x: String, e: Expr) {
  def toSet: Set_! = Set_!(x, e)
}
case class Let(x: String, e: Expr, body: Expr) extends Expr {
  def toApp: App = App(Lam(x, body), e)
}
case class Lrc(bds: List[Bind], body: Expr) extends Expr {
  require(bds.nonEmpty)
  def toLet: Expr = bds.foldRight[Expr](Begin(bds.map(_.toSet) ++ List(body))) {
    case (Bind(x, e), body) ⇒ Let(x, Void(), body)
  }
  def toApp: App = toLet.asInstanceOf[Let].toApp
}
case class Rec(x: String, e: Expr, body: Expr) extends Expr

case class Lit(i: Int) extends Expr
case class If0(cnd: Expr, thn: Expr, els: Expr) extends Expr
case class AOp(op: Symbol, e1: Expr, e2: Expr) extends Expr

case class Void() extends Expr
case class Set_!(x: String, e: Expr) extends Expr
case class Begin(es: List[Expr]) extends Expr
