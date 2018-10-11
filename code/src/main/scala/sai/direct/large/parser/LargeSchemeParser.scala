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

  def lets: Parser[Expr] = let | letproc | letstar | letrec

  def let: Parser[Let] = LPAREN ~> LET ~> (LPAREN ~> bind.+ <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case binds ~ body => Let(binds, body)
  }

  def letproc: Parser[Lrc] = LPAREN ~> LET ~> IDENT ~ (LPAREN ~> bind.+ <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case ident ~ binds ~ body => 
      val args = binds map { case Bind(id, _) => id }
      val initvals = binds map { case Bind(_, initv) => initv }
      Lrc(List(Bind(ident, Lam(args, body))), App(Var(ident), initvals))
  }

  def letstar: Parser[Let] = LPAREN ~> LETSTAR ~> (LPAREN ~> bind.+ <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case binds ~ body => 
      binds.dropRight(1).foldRight (Let(List(binds.last), body)) { case (bd, e) => Let(List(bd), e) }
  }

  def letrec: Parser[Lrc] = LPAREN ~> LETREC ~> (LPAREN ~> bind.+ <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case binds ~ body => Lrc(binds, body)
  }

  def intlit: Parser[IntLit] = INT10 ^^ { IntLit(_) }

  def boollit: Parser[BoolLit] = (TRUE | FALSE) ^^ { BoolLit(_) }
  
  def stringlit: Parser[App] = STRINGLIT ^^ {
    case str =>
      val elements: List[IntLit] = str.toCharArray map { (c: Char) => IntLit(c.asInstanceOf[Int]) } toList;
      App(Var("vector"), elements)
  }

  def listsugar: Parser[App] = LISTLPAREN ~> expr.* <~ RPAREN ^^ {
    case elements => App(Var("list"), elements)
  }

  def vecsugar: Parser[App] = VECLPAREN ~> expr.* <~ RPAREN ^^ {
    case elements => App(Var("vector"), elements)
  }

  def literals = intlit | boollit | stringlit | listsugar | vecsugar

  def ifthel: Parser[If] = LPAREN ~> IF ~> expr ~ expr ~ expr <~ RPAREN ^^ {
    case cond ~ thn ~ els => If(cond, thn, els)
  }

  def condBranch: Parser[CondBranch] = LPAREN ~> expr ~ expr <~ RPAREN ^^ {
    case cond ~ thn => CondBranch(cond, thn)
  }

  def condProcBranch: Parser[CondBranch] = LPAREN ~> expr ~ (RARROW ~> expr) <~ RPAREN ^^ {
    case cond ~ proc => CondBranch(cond, App(proc, List(cond)))
  }

  def condElseBranch: Parser[CondBranch] = LPAREN ~> ELSE ~> expr <~ RPAREN ^^ {
    case thn => CondBranch(BoolLit(true), thn)
  }

  def condBranches = condElseBranch | condBranch | condProcBranch

  def cond: Parser[Cond] = LPAREN ~> COND ~> condBranches.* <~ RPAREN ^^ {
    case branches => Cond(branches)
  }

  def caseBranch: Parser[CaseBranch] = LPAREN ~> (LPAREN ~> expr.* <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case cases ~ thn => CaseBranch(cases, thn)
  }

  def caseElseBranch: Parser[CaseBranch] = LPAREN ~> ELSE ~> expr <~ RPAREN ^^ {
    case thn => CaseBranch(List(), thn)
  }

  def cas: Parser[Case] = LPAREN ~> CASE ~> expr ~ (caseElseBranch | caseBranch).* <~ RPAREN ^^ {
    case ev ~ branches => Case(ev, branches)
  }

  def dispatch = ifthel | cond | cas

  def expr: Parser[Expr] = literals | variable | lam | lets | dispatch | app

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
