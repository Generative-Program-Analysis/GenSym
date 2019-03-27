package sai.cps.parser

/** Simple CPS Scheme Language
  */

case class Bind(name: String, value: Expr)

sealed abstract class Expr

case class Lit(n: Int) extends Expr {
  override def toString() = { s"sai.cps.parser.Lit($n)" }
}

case class Bool(b: Boolean) extends Expr

case class Op(rator: String) extends Expr {
  override def toString() = { "sai.cps.parser.Op(\"" + rator + "\")"}
}

case class Var(name: String) extends Expr {
  override def toString() = { "sai.cps.parser.Var(\"" + name + "\")" }
}

case class App(f: Expr, args: List[Expr]) extends Expr {
  override def toString() = { s"sai.cps.parser.App($f, $args)" }
}

case class Lam(vars: List[String], body: Expr) extends Expr {
  override def toString() = {
    s"sai.cps.parser.Lam(List(${vars.map((s) => "\""+s+"\"").mkString(",")}), $body)"
  }
}

case class Letrec(binds: List[Bind], body: Expr) extends Expr
