package sai.direct.large.parser

import scala.util.parsing.combinator._
import sai.common.parser._

trait LargeSchemeParserTrait extends SchemeTokenParser {
  def variable: Parser[Var] = IDENT ^^ { Var(_) }

  def app: Parser[App] = LPAREN ~> expr ~ expr.* <~ RPAREN ^^ {
    case e ~ param => App(e, param)
  }

  def lam: Parser[Lam] = LPAREN ~> LAMBDA ~> (LPAREN ~> IDENT.* <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case args ~ body => Lam(args, body)
  }

  def bind: Parser[Bind] = LPAREN ~> IDENT ~ expr <~ RPAREN ^^ {
    case id ~ e => Bind(id, e)
  }

  def let: Parser[Let] = LPAREN ~> LET ~> (LPAREN ~> bind.+ <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case binds ~ body => Let(binds, body)
  }

  def letproc: Parser[Let] = LPAREN ~> LET ~> IDENT ~ (LPAREN ~> bind.+ <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case ident ~ binds ~ body => 
      val args = binds map { case Bind(id, _) => id }
      val initvals = binds map { case Bind(_, initv) => initv }
      Let(List(Bind(ident, Lam(args, body))), App(Var(ident), initvals))
  }

  def letstar: Parser[LetStar] = LPAREN ~> LETSTAR ~> (LPAREN ~> bind.+ <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case binds ~ body => LetStar(binds, body)
  }

  def letrec: Parser[Lrc] = LPAREN ~> LETREC ~> (LPAREN ~> bind.+ <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case binds ~ body => Lrc(binds, body)
  }

  def intlit: Parser[IntLit] = INT10 ^^ { IntLit(_) }

  def boollit: Parser[BoolLit] = (TRUE | FALSE) ^^ { BoolLit(_) }

  def listsugar: Parser[App] = LISTLPAREN ~> expr.* <~ RPAREN ^^ {
    case elements => App(Var("list"), elements)
  }

  def vecsugar: Parser[App] = VECLPAREN ~> expr.* <~ RPAREN ^^ {
    case elements => App(Var("vector"), elements)
  }

  def ifthel: Parser[If] = LPAREN ~> IF ~> expr ~ expr ~ expr <~ RPAREN ^^ {
    case cond ~ thn ~ els => If(cond, thn, els)
  }

  def condBranch: Parser[CondBranch] = LPAREN ~> expr ~ expr <~ RPAREN ^^ {
    case cond ~ thn => CondBranch(cond, thn)
  }

  def condElseBranch: Parser[CondBranch] = LPAREN ~> ELSE ~> expr <~ RPAREN ^^ {
    case thn => CondBranch(BoolLit(true), thn)
  }

  def cond: Parser[Cond] = LPAREN ~> COND ~> (condElseBranch | condBranch).* <~ RPAREN ^^ {
    case branches => Cond(branches)
  }

  def expr: Parser[Expr] = intlit | boollit | listsugar | vecsugar | variable | lam | let | letproc | letstar | letrec | ifthel | cond | app

  def define: Parser[Define] = LPAREN ~> DEF ~> IDENT ~ expr <~ RPAREN ^^ {
    case id ~ e => Define(id, e)
  }

  def toplevel: Parser[Toplevel] = define | expr 
  def toptoplevel = toplevel.*
}


object LargeSchemeParser extends LargeSchemeParserTrait {
  def apply(input: String): Option[List[Toplevel]] = apply(toptoplevel, input)

  def apply[T](pattern: Parser[T], input: String): Option[T] = parse(pattern, input) match {
    case Success(matched, _) => Some(matched)
    case e => println(e); None
  }
}

object TestSimpleDirectLargeSchemeParser {
  def main(args: Array[String]) = {
    println(LargeSchemeParser(args(0)))
  }
}
