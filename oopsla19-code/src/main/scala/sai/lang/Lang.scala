package sai

object PCFLang {
  sealed trait Expr
  sealed trait AExpr extends Expr
  sealed trait CExpr extends Expr

  case class Lit(i: Int) extends AExpr
  case class Var(x: String) extends AExpr {
    override def toString: String = "Var(\"" + x + "\")"
  }
  case class Lam(x: String, e: Expr) extends AExpr {
    override def toString: String = "Lam(\"" + x + "\"," + e + ")"
  }
  case class Amb(a: AExpr, b: AExpr) extends AExpr

  case class App(e1: Expr, e2: Expr) extends CExpr
  case class Let(x: String, rhs: Expr, body: Expr) extends CExpr {
    override def toString = "Let(\"" + x + "\"," + rhs + "," + body + ")"
  }
  case class Rec(x: String, rhs: Expr, body: Expr) extends CExpr {
    override def toString: String = "Rec(\"" + x + "\"," + rhs + "," + body + ")"
  }
  case class If0(e1: Expr, e2: Expr, e3: Expr) extends CExpr
  case class Aop(op: Symbol, e1: Expr, e2: Expr) extends CExpr

  object Values {
    trait Value
    case class IntV(i: Int) extends Value
    case class CloV[Env](lam: Lam, e: Env) extends Value
  }

  object Examples {
    val p1 = Let("x", Lit(1),
      Let("y", Lit(2),
        Let("f", Lam("z", Aop('+, Var("z"), Var("y"))),
          App(Var("f"), Var("x")))))

    val fact = Lam("n",
      If0(Var("n"),
        Lit(1),
        Aop('*, Var("n"), App(Var("fact"), Aop('-, Var("n"), Lit(1))))))
    val fact5 = Rec("fact", fact, App(Var("fact"), Lit(5)))

    val id3 = App(Lam("x",
      App(Var("x"), Var("x"))),
      Lam("y", Var("y")))

    // ((λ (x) ((x x) x)) (λ (y) y))
    val id4 = App(Lam("x",
      App(App(Var("x"), Var("x")),
        Var("x"))),
      Lam("y", Var("y")))

    val ifif = Let("x",
      If0(Lit(-1),
        If0(Lit(-1), Lit(1), Lit(2)),
        If0(Lit(-1), Lit(3), Lit(4))),
      Let("y",
        If0(Lit(-1), Lit(5), Lit(6)),
        Lit(100)))
    val simpleif = If0(Lit(-1), Lit(5), Lit(6))
  }
}
