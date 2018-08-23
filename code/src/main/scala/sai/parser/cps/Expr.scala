package sai.parser.cps

/** Simple CPS Scheme Language
  */

case class Binding(name: String, value: Expr)

sealed abstract class Expr

case class Lit(n: Int) extends Expr {
  override def toString() = { s"sai.parser.cps.Lit($n)" }
}

case class Bool(b: Boolean) extends Expr

case class Op(rator: String) extends Expr {
  override def toString() = { "sai.parser.cps.Op(\"" + rator + "\")"}
}

case class Var(name: String) extends Expr {
  override def toString() = { "sai.parser.cps.Var(\"" + name + "\")" }
}

case class App(f: Expr, args: List[Expr]) extends Expr {
  override def toString() = { s"sai.parser.cps.App($f, $args)" }
}

case class Lam(vars: List[String], body: Expr) extends Expr {
  override def toString() = {
    s"sai.parser.cps.Lam(List(${vars.map((s) => "\""+s+"\"").mkString(",")}), $body)"
  }
}

case class Letrec(bindings: List[Binding], body: Expr) extends Expr
