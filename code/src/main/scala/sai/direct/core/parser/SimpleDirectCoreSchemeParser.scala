package sai.direct.core.parser

import scala.util.parsing.combinator._
import sai.common.parser._

trait SimpleDirectCoreSchemeParserTrait extends SchemeTokenParser {
  def variable: Parser[Var] = IDENT ^^ { Var(_) }

  def app: Parser[App] = LPAREN ~> expr ~ expr <~ RPAREN ^^ {
    case e1 ~ e2 => App(e1, e2)
  }

  def lam: Parser[Lam] = LPAREN ~> LAMBDA ~> (LPAREN ~> IDENT <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case arg ~ body => Lam(arg, body)
  }

  def bind: Parser[Bind] = LPAREN ~> IDENT ~ expr <~ RPAREN ^^ {
    case id ~ e => Bind(id, e)
  }

  def let: Parser[Expr] = LPAREN ~> LET ~> (LPAREN ~> (LPAREN ~> IDENT ~ expr <~ RPAREN) <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case id ~ e ~ body => Let(id, e, body)
  }

  def letrec: Parser[Lrc] = LPAREN ~> LETREC ~> (LPAREN ~> bind.+ <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case binds ~ body => Lrc(binds, body)
  }

  def lit: Parser[Lit] = INT10 ^^ { Lit(_) }

  def if0: Parser[If0] = LPAREN ~> IF0 ~> expr ~ expr ~ expr <~ RPAREN ^^ {
    case cond ~ th ~ el => If0(cond, th, el)
  }

  def aop: Parser[AOp] = LPAREN ~> PRIMOP ~ expr ~ expr <~ RPAREN ^^ {
    case op ~ e1 ~ e2 => AOp(Symbol(op), e1, e2)
  }

  def expr: Parser[Expr] = lit | aop | app | if0 | lam | let | letrec | variable
}

object SimpleDirectCoreSchemeParser extends SimpleDirectCoreSchemeParserTrait {
  def apply(input: String): Option[Expr] = apply(expr, input)

  def apply[T](pattern: Parser[T], input: String): Option[T] = parse(pattern, input) match {
    case Success(matched, _) => Some(matched)
    case e => println(e); None
  }
}

object TestSimpleDirectCoreSchemeParser {
  def main(args: Array[String]) = {
    assert(SimpleDirectCoreSchemeParser(SimpleDirectCoreSchemeParser.lit, "2") ==
             Some(Lit(2)))
    assert(SimpleDirectCoreSchemeParser(SimpleDirectCoreSchemeParser.if0, "(if0 a b c)") ==
             Some(If0(Var("a"), Var("b"), Var("c"))))
    assert(SimpleDirectCoreSchemeParser(SimpleDirectCoreSchemeParser.if0, "(if0 1 2 3)") ==
             Some(If0(Lit(1), Lit(2), Lit(3))))
    assert(SimpleDirectCoreSchemeParser("(+ 1 2)") == Some(AOp('+, Lit(1), Lit(2))))

    val fact5 = "(letrec ([fact (lambda (n) (if0 n 1 (* n (fact (- n 1)))))]) (fact 5))"
    assert(SimpleDirectCoreSchemeParser(fact5).get ==
           Lrc(List(Bind("fact",
                         Lam("n",If0(Var("n"),
                                   Lit(1),
                                   AOp('*,
                                       Var("n"),
                                       App(Var("fact"),
                                           AOp('-,Var("n"),Lit(1)))))))),
               App(Var("fact"),Lit(5))))


    //println(SimpleDirectCoreSchemeParser(args(0)))
  }
}
