package sai.direct.core.parser

import scala.util.parsing.combinator._
import sai.common.parser._
import sai.cps.parser.Op

trait SimpleDirectCoreSchemeParserTrait extends SchemeTokenParser {
  def variable: Parser[Var] = IDENT ^^ { Var(_) }

  def app: Parser[App] = LPAREN ~> expr ~ expr ~> RPAREN ^^ {
    case e1 ~ e2 => App(e1, e2)
  }

  def lam: Parser[Lam] = LPAREN ~> LAMBDA ~> IDENT ~ expr <~ RPAREN ^^ {
    case arg ~ body => Lam(arg, body)
  }

  def bind : Parser[Bind] = LPAREN ~> IDENT ~ expr <~ RPAREN ^^ {
    case id ~ e => Bind(id, e)
  }

  def let : Parser[Expr] = LPAREN ~> LET ~> IDENT ~ expr ~ expr <~ RPAREN ^^ {
    case id ~ e ~ body => Let(id, e, body)
  }

  def letrec : Parser[Lrc] = LPAREN ~> LETREC ~> (LPAREN ~> bind.+ <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case binds ~ body => Lrc(binds, body)
  }

  def lit: Parser[Lit] = INT10 ^^ { Lit(_) }

  def if0: Parser[If0] = LPAREN ~> IF0 ~> expr ~ expr ~ expr <~ RPAREN ^^ {
    case cond ~ th ~ el => If0(cond, th, el)
  }

  def aop: Parser[AOp] = LPAREN ~> PRIMOP ~ expr ~ expr <~ RPAREN ^^ {
    case op ~ e1 ~ e2 => AOp(op, e1, e2)
  }

  def expr: Parser[Expr] = lit | aop | app | if0 | lam | let | variable
}

object SimpleDirectCoreSchemeParser extends SimpleDirectCoreSchemeParserTrait {
  def apply(input: String): Option[Expr] = apply(expr, input)

  def apply[T](pattern: Parser[T], input: String): Option[T] = parse(pattern, input) match {
    case Success(matched, _) => Some(matched)
    case _ => None
  }
}
