package sai.evaluation.parser

/* Author: Yuxuan Chen, Guannan Wei */

trait Expr
trait AtomExpr

case class Sym(x: String) extends Expr with AtomExpr {
  override def toString: String = "Sym(\"" + x + "\")"
}

case class Var(x: String) extends Expr with AtomExpr {
  override def toString: String = "Var(\"" + x + "\")"
}
case class App(e1: Expr, param: List[Expr]) extends Expr with AtomExpr
case class Lam(param: List[String], body: Expr) extends Expr with AtomExpr {
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

case class IntLit(x: Int) extends Expr with AtomExpr
case class FloatLit(x: Double) extends Expr with AtomExpr
case class BoolLit(x: Boolean) extends Expr with AtomExpr
case class CharLit(x: Char) extends Expr with AtomExpr {
  override def toString: String = "CharLit('" + x + "')"
}
case class If(cnd: Expr, thn: Expr, els: Expr) extends Expr with AtomExpr

trait CondBrTrait { val cnd: Expr; val thn: Expr }
case class CondBr(cnd: Expr, thn: Expr) extends CondBrTrait
case class CondProcBr(cnd: Expr, thn: Expr) extends CondBrTrait
case class Cond(branches: List[CondBrTrait]) extends Expr

case class CaseBranch(cases: List[Expr], thn: Expr)
case class Case(e: Expr, branches: List[CaseBranch]) extends Expr

case class Void() extends Expr with AtomExpr
case class Set_!(x: String, e: Expr) extends Expr with AtomExpr {
  override def toString: String = "Set_!(\"" + x + "\", " + e + ")"
}
case class Begin(es: List[Expr]) extends Expr
case class Define(x: String, e: Expr) extends Expr

object ASTUtils {
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

  def prettyPrint(e: Expr): Unit = println(exprToString(e))

  def size(e: Expr): Int = e match {
    case Var(x) => 1
    case Void() => 1
    case Sym(x) => 1
    case CharLit(x)   => 1
    case IntLit(x)    => 1
    case FloatLit(x)  => 1
    case BoolLit(x)   => 1
    case Set_!(x, e)  => 1 + size(e)
    case Define(x, e) => 1 + size(e)
    case App(x, l)    => 1 + size(x) + l.foldLeft(0)(_ + size(_))
    case Begin(es)    => 1 + es.foldLeft(0)(_ + size(_))
    case If(c, t, e)  => 1 + size(c) + size(t) + size(e)
    case Lam(params, body) => 1 + size(body)
  }

  def defVars(e: Expr): Set[String] = e match {
    case Define(x, e) => Set(x) ++ defVars(e)
    case Set_!(x, e) => defVars(e)
    case App(x, l)    => defVars(x) ++ l.map(defVars).foldLeft(Set[String]())(_ ++ _)
    case Begin(es)    => es.map(defVars).foldLeft(Set[String]())(_ ++ _)
    case If(c, t, e)  => defVars(c) ++ defVars(t) ++ defVars(e)
    case Lam(params, body) => defVars(body)
    case _ => Set()
  }

  def free(e: Expr): Set[String] = {
    val fv: Set[String] = e match {
      case Var(x) => Set(x)
      case Void() => Set()
      case Sym(x) => Set()
      case CharLit(x)   => Set()
      case IntLit(x)    => Set()
      case FloatLit(x)  => Set()
      case BoolLit(x)   => Set()
      case Set_!(x, e)  => free(e)
      case Define(x, e) => free(e) - x
      case App(x, l)    => free(x) ++ l.map(free).foldLeft(Set[String]())(_ ++ _)
      case Begin(es)    => es.map(free).foldLeft(Set[String]())(_ ++ _)
      case If(c, t, e)  => free(c) ++ free(t) ++ free(e)
      case Lam(params, body) => free(body) -- params.toSet
    }
    fv -- defVars(e)
  }

  var count = 1
  def fresh(pre: String = "var"): String = {
    val name = pre + count.toString
    count = count + 1
    name
  }
  def alpha(e: Expr, map: Map[String, String]): (Expr, Map[String, String]) = {
    e match {
      case Var(x) =>
        (if (map.contains(x)) Var(map(x)) else {
           //println(s"$x not exist!")
           e
         }, map)
      case Void() => (e, map)
      case Sym(x) => (e, map)
      case CharLit(x)   => (e, map)
      case IntLit(x)    => (e, map)
      case FloatLit(x)  => (e, map)
      case BoolLit(x)   => (e, map)
      case Set_!(x, e)  =>
        val (ne, nm) = alpha(e, map)
        (Set_!(map(x), ne), map)
      case Define(x, e) =>
        val name = if (map.contains(x)) map(x) else fresh()
        val nm = map + (x -> name)
        val (ne, nnm) = alpha(e, nm)
        (Define(name, ne), nm)
      case App(x, l)    => (App(alpha(x, map)._1, l.map(alpha(_, map)._1)), map)
      case Begin(es)    =>
        val defmap = (es.filter(_.isInstanceOf[Define]).map { case Define(x, _) => (x, fresh()) }).toMap
        val (res, mm) = es.foldLeft((List[Expr](), defmap ++ map)) {
          case ((nes, m), e) =>
            val (ne, nm) = alpha(e, m)
            (nes ++ List(ne), nm)
        }
        (Begin(res), map)
      case If(c, t, e)  => (If(alpha(c, map)._1, alpha(t, map)._1, alpha(e, map)._1), map)
      case Lam(params, body) =>
        val nparams = params.map(x => (x, fresh("var")))
        val nmap = map ++ nparams.toMap
        val (nbody, _) = alpha(body, nmap)
        (Lam(nparams.map(_._2), nbody), map)
    }
  }
}
