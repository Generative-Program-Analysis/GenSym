package sai.direct.large.parser

trait Expr
trait SmallExpr

case class Sym(x: String) extends Expr with SmallExpr {
  override def toString: String = "Sym(\"" + x + "\")"
}

case class Var(x: String) extends Expr with SmallExpr {
  override def toString: String = "Var(\"" + x + "\")"
}
case class App(e1: Expr, param: List[Expr]) extends Expr with SmallExpr
case class Lam(param: List[String], body: Expr) extends Expr with SmallExpr {

  override def toString: String = {
    val list = param map { s => "\"" + s + "\"" } mkString (", ")
    "Lam(List(" + list + "), " + body + ")"
  }
}

case class Bind(x: String, e: Expr) {
  override def toString: String = "Bind(\"" + x + "\" " + e + ")"
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

case class IntLit(x: Int) extends Expr with SmallExpr
case class FloatLit(x: Double) extends Expr with SmallExpr
case class BoolLit(x: Boolean) extends Expr with SmallExpr
case class CharLit(x: Char) extends Expr with SmallExpr {
  override def toString: String = "CharLit('" + x + "')"
}
case class If(cnd: Expr, thn: Expr, els: Expr) extends Expr with SmallExpr

trait CondBrTrait { val cnd: Expr; val thn: Expr }
case class CondBr(cnd: Expr, thn: Expr) extends CondBrTrait
case class CondProcBr(cnd: Expr, thn: Expr) extends CondBrTrait
case class Cond(branches: List[CondBrTrait]) extends Expr

case class CaseBranch(cases: List[Expr], thn: Expr)
case class Case(e: Expr, branches: List[CaseBranch]) extends Expr

case class Void() extends Expr with SmallExpr
case class Set_!(x: String, e: Expr) extends Expr with SmallExpr {
  override def toString: String = "Set_!(\"" + x + "\", " + e + ")"
}
case class Begin(es: List[Expr]) extends Expr
case class Define(x: String, e: Expr) extends Expr

object SExpPrinter {
  def exprToString(e: Expr): String = e match {
    case Var(x) => x
    case Void() => "(void)"
    case Sym(x) => "'" + x
    case CharLit(x)   => "#\\" + x
    case IntLit(x)    => x.toString
    case FloatLit(x)  => x.toString
    case BoolLit(x)   => if (x) "#t" else "#f"
    case Set_!(x, e)  => "(set! " + x + " " + exprToString(e) + ")"
    case Define(x, e) => "(define " + x + " " + exprToString(e) + ")"
    case App(x, l)    => "(" + (x::l).map(exprToString).mkString(" ") + ")"
    case Begin(es)    => "(begin " + es.map(exprToString).mkString(" ") + ")"
    case If(c, t, e)  => "(if " + (c::t::e::Nil).map(exprToString).mkString(" ") + ")"
    case Lam(params, body) => "(lambda (" + params.mkString(" ") + ") " + exprToString(body) + ")"
  }

  def apply(e: Expr): Unit = println(exprToString(e))

  def main(args: Array[String]) = {
    assert(SExpPrinter.exprToString(CharLit('a')) == "#\\a")

    assert(SExpPrinter.exprToString(App(Lam(List("x", "y"),
      App(Var("+"), List(Var("x"), Var("y")))), List(IntLit(1), IntLit(3)))) ==
      "((lambda (x y) (+ x y)) 1 3)")

    assert(SExpPrinter.exprToString(If(BoolLit(false), Var("a"), Lam(List("t"), Var("t")))) ==
      "(if #f a (lambda (t) t))")
  }
}
