package sai.parser.cps

import scala.util.parsing.combinator._

import sai.parser._

/** Syntax reference:
  * TSPL 4th: https://www.scheme.com/tspl4/grammar.html
  * Boucher and Feeley (1996): http://www.iro.umontreal.ca/~feeley/papers/BoucherFeeleyCC96.pdf
  */

object SimpleCPSSchemeParser extends SimpleSchemeTokenParser {
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

  def binding: Parser[Binding] = LPAREN ~> IDENT ~ lam <~ RPAREN ^^ {
    case ident ~ lam => Binding(ident, lam)
  }

  def letrec: Parser[Letrec] = LPAREN ~> LETREC ~> (LPAREN ~> binding.+ <~ RPAREN) ~ call <~ RPAREN ^^ {
    case bindings ~ body => Letrec(bindings, body)
  }

  def let: Parser[App] = LPAREN ~> LET ~> (LPAREN ~> binding.+ <~ RPAREN) ~ call <~ RPAREN ^^ {
    case bindings ~ body => App(Lam(bindings.map(_.name), body), bindings.map(_.value))
  }

  def call: Parser[Expr] = app | letrec | let

  def toplevel: Parser[Expr] = call

  /**********************************/

  def apply(input: String): Option[Expr] = apply(toplevel, input)

  def apply[T](pattern: Parser[T], input: String): Option[T] = parse(pattern, input) match {
    case Success(matched, _) => Some(matched)
    case _ => None
  }
}

object TestSimpleCPSSchemeParser {
  def check(str: String, expr: Expr) {
    SimpleCPSSchemeParser(str) match {
      case Some(e) if e == expr =>
        println(str); println(" ==>"); println(e)
      case Some(e) =>
        println(s"FAILURE: $str,\nEXPECTED: ${expr}\nBUT HAVE: $e")
      case None =>
        println(s"FAILURE: $str,\nEXPECTED: $expr")
    }
  }

  def main(args: Array[String]) = {
    println(SimpleCPSSchemeParser(SimpleCPSSchemeParser.variable, "ab-c"))
    println(SimpleCPSSchemeParser(SimpleCPSSchemeParser.variable, "ab/c"))
    println(SimpleCPSSchemeParser(SimpleCPSSchemeParser.variable, "env*"))
    println(SimpleCPSSchemeParser(SimpleCPSSchemeParser.variable, "env?"))
    println(SimpleCPSSchemeParser(SimpleCPSSchemeParser.variable, "get-env*"))
    println(SimpleCPSSchemeParser(SimpleCPSSchemeParser.variable, "*call-with-current-continuation:!/@:"))
    println(SimpleCPSSchemeParser(SimpleCPSSchemeParser.variable, "x k"))

    println(SimpleCPSSchemeParser(SimpleCPSSchemeParser.lit, "123"))
    println(SimpleCPSSchemeParser(SimpleCPSSchemeParser.lit, "00123"))
    println(SimpleCPSSchemeParser(SimpleCPSSchemeParser.app, "(e1 e2)"))
    println(SimpleCPSSchemeParser(SimpleCPSSchemeParser.lam, "(lambda (x k) (k x))"))

    val example1_str = """((lambda (x k) (k (lambda (a) (halt a))))
                          3
                          (lambda (z) (halt z)))"""
    val example1_expr = App(Lam(List("x", "k"),
                           App(Var("k"), List(Lam(List("a"), App(Var("halt"), List(Var("a"))))))),
                        List(Lit(3), Lam(List("z"), App(Var("halt"), List(Var("z"))))))
    check(example1_str, example1_expr)

    val example2_str = """((lambda (x) (halt x))
                           (lambda (y) (halt y)))"""
    val example2_expr = App(Lam(List("x"), App(Var("halt"), List(Var("x")))),
                            List(Lam(List("y"), App(Var("halt"), List(Var("y"))))))
    check(example2_str, example2_expr)

    check(Examples.example3_str, Examples.example3_expr)
    check(Examples.example4_str, Examples.example4_expr)

    println(SimpleCPSSchemeParser(Examples.example5_str))
  }
}
