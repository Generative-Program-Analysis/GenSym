package sai.parser.cps

import scala.util.parsing.combinator._

import sai.parser._

/** Syntax reference:
  * TSPL 4th: https://www.scheme.com/tspl4/grammar.html
  * Boucher and Feeley (1996): http://www.iro.umontreal.ca/~feeley/papers/BoucherFeeleyCC96.pdf
  */

trait SimpleCPSSchemeParserTrait extends SchemeTokenParser {
  def op: Parser[Op] = PRIMOP ^^ { Op(_) }

  def lit: Parser[Lit] = INT10 ^^ { Lit(_) }
  
  def bool: Parser[Bool] = (TRUE | FALSE) ^^ { Bool(_) }

  def variable: Parser[Var] = IDENT ^^ { Var(_) }

  def app: Parser[App] = LPAREN ~> (variable | op | lam) ~ (variable | lit | lam).* <~ RPAREN ^^ {
    case e1 ~ e2 => App(e1, e2)
  }

  def lam: Parser[Lam] = LPAREN ~> LAMBDA ~> (LPAREN ~> IDENT.* <~ RPAREN) ~ call <~ RPAREN ^^ {
    case vars ~ expr => Lam(vars, expr)
  }

  def bind: Parser[Bind] = LPAREN ~> IDENT ~ lam <~ RPAREN ^^ {
    case ident ~ lam => Bind(ident, lam)
  }

  def letrec: Parser[Letrec] = LPAREN ~> LETREC ~> (LPAREN ~> bind.+ <~ RPAREN) ~ call <~ RPAREN ^^ {
    case binds ~ body => Letrec(binds, body)
  }

  def let: Parser[App] = LPAREN ~> LET ~> (LPAREN ~> bind.+ <~ RPAREN) ~ call <~ RPAREN ^^ {
    case binds ~ body => App(Lam(binds.map(_.name), body), binds.map(_.value))
  }

  def call: Parser[Expr] = app | letrec | let

  def toplevel: Parser[Expr] = call
}

object SimpleCPSSchemeParser extends SimpleCPSSchemeParserTrait {
  def apply(input: String): Option[Expr] = apply(toplevel, input)

  def apply[T](pattern: Parser[T], input: String): Option[T] = parse(pattern, input) match {
    case Success(matched, _) => Some(matched)
    case _ => None
  }
}

object TestSimpleCPSSchemeParser extends SimpleCPSSchemeParserTrait {
  def check[T](parser: SimpleCPSSchemeParser.Parser[T], str: String, expr: Expr) {
    SimpleCPSSchemeParser(parser, str) match {
      case Some(e) if e == expr =>
        println(str); println(" ==>"); println(e)
      case Some(e) =>
        println(s"FAILURE: $str,\nEXPECTED: ${expr}\nBUT HAVE: $e")
      case None =>
        println(s"FAILURE: $str,\nEXPECTED: $expr")
    }
  }

  def checkTop(str: String, expr: Expr) { check(SimpleCPSSchemeParser.toplevel, str, expr) }
  def checkVar(str: String, expr: Var) { check(SimpleCPSSchemeParser.variable, str, expr) }
  def checkLit(str: String, expr: Lit) { check(SimpleCPSSchemeParser.lit, str, expr) }
  def checkApp(str: String, expr: App) { check(SimpleCPSSchemeParser.app, str, expr) }
  def checkLam(str: String, expr: Lam) { check(SimpleCPSSchemeParser.lam, str, expr) }

  def main(args: Array[String]) = {
    checkVar("ab-c", Var("ab-c"))
    checkVar("ab/c", Var("ab/c"))
    checkVar("env*", Var("env*"))
    checkVar("env?", Var("env?"))
    checkVar("get-env*", Var("get-env*"))
    checkVar("*call-with-current-continuation:!/@:", Var("*call-with-current-continuation:!/@:"))
    checkVar("x k", Var("x"))
    checkVar("ab-c ; this is a comment", Var("ab-c"))

    checkLit("123", Lit(123))
    checkLit("00123", Lit(123))
    checkLit("00123 ; comment", Lit(123))
    checkLit("00123 ;; comment", Lit(123))

    checkApp("(e1 e2)", App(Var("e1"), List(Var("e2"))))
    checkApp("(e1 e2 e3", App(Var("e1"), List(Var("e2"), Var("e3"))))
    checkLam("(lambda (x k) (k x))", Lam(List("x","k"), App(Var("k"), List(Var("x")))))
    checkLam("""(lambda (x k) 
                ;; comment
                        (k x))""", 
             Lam(List("x","k"), App(Var("k"), List(Var("x")))))

    val example1_str = """((lambda (x k) (k (lambda (a) (halt a))))
                          3
                          (lambda (z) (halt z)))"""
    val example1_expr = App(Lam(List("x", "k"),
                           App(Var("k"), List(Lam(List("a"), App(Var("halt"), List(Var("a"))))))),
                        List(Lit(3), Lam(List("z"), App(Var("halt"), List(Var("z"))))))
    checkTop(example1_str, example1_expr)

    val example2_str = """((lambda (x) (halt x))
                           (lambda (y) (halt y)))"""
    val example2_expr = App(Lam(List("x"), App(Var("halt"), List(Var("x")))),
                            List(Lam(List("y"), App(Var("halt"), List(Var("y"))))))
    checkTop(example2_str, example2_expr)

    checkTop(Examples.example3_str, Examples.example3_expr)

    checkTop(Examples.example4_str, Examples.example4_expr)

    checkTop(Examples.example5_str, App(Lam(List("f1","k1"), App(Lam(List("f2","k2"), App(Lam(List("f3","k3"), App(Lam(List("f4","k4"), App(Lam(List("f5","k5"), App(Lam(List("f6","k6"), App(Lam(List("f7","k7"), App(Lam(List("f8","k8"), App(Lam(List("f9","k9"), App(Lam(List("f10","k10"), App(Lam(List("f11","k11"), App(Lam(List("f12","k12"), App(Var("k12"), List(Var("f12")))), List(Var("f11"), Var("k11")))), List(Var("f10"), Var("k10")))), List(Var("f9"), Var("k9")))), List(Var("f8"), Var("k8")))), List(Var("f7"), Var("k7")))), List(Var("f6"), Var("k6")))), List(Var("f5"), Var("k5")))), List(Var("f4"), Var("k4")))), List(Var("f3"), Var("k3")))), List(Var("f2"), Var("k2")))), List(Var("f1"), Var("k1")))), List(Lam(List("y"), App(Var("halt"), List(Var("y")))), Lam(List("x"), App(Var("halt"), List(Var("x"))))))) }
}
