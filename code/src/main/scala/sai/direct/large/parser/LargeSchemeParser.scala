package sai.direct.large.parser

import scala.util.parsing.combinator._
import sai.common.parser._

trait LargeSchemeParserTrait extends SchemeTokenParser {
  implicit def variable: Parser[Var] = IDENT ^^ { Var(_) }

  implicit def app: Parser[App] = LPAREN ~> expr ~ expr.* <~ RPAREN ^^ {
    case e ~ param => App(e, param)
  }

  implicit def lam: Parser[Lam] = LPAREN ~> LAMBDA ~> (LPAREN ~> IDENT.* <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case args ~ body => Lam(args, body)
  }

  implicit def bind: Parser[Bind] = LPAREN ~> IDENT ~ expr <~ RPAREN ^^ {
    case id ~ e => Bind(id, e)
  }

  implicit def lets: Parser[Expr] = let | letstar | letrec | letproc

  implicit def let: Parser[App] = LPAREN ~> LET ~> (LPAREN ~> bind.+ <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case binds ~ body => Let(binds, body).toApp
  }

  implicit def letproc: Parser[App] = LPAREN ~> LET ~> IDENT ~ (LPAREN ~> bind.+ <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case ident ~ binds ~ body =>
      Lrc(List(Bind(ident, Lam(binds.map(_.x), body))), App(Var(ident), binds.map(_.e))).toApp
  }

  implicit def letstar: Parser[App] = LPAREN ~> LETSTAR ~> (LPAREN ~> bind.+ <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case binds ~ body =>
      binds.dropRight(1).foldRight (Let(List(binds.last), body).toApp) { case (bd, e) => Let(List(bd), e).toApp }
  }

  implicit def letrec: Parser[App] = LPAREN ~> LETREC ~> (LPAREN ~> bind.+ <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case binds ~ body => Lrc(binds, body).toApp
  }

  implicit def intlit: Parser[IntLit] = INT10 ^^ { IntLit(_) }
  implicit def boollit: Parser[BoolLit] = (TRUE | FALSE) ^^ { BoolLit(_) }
  implicit def charlit: Parser[CharLit] = CHARLIT ^^ {
    case s => CharLit(s.charAt(2))
  }
  implicit def stringlit: Parser[App] = STRINGLIT ^^ {
    case str =>
      val elements: List[CharLit] = str.toCharArray map { CharLit(_) } toList;
      App(Var("vector"), elements.drop(1).dropRight(1))
  }
  implicit def listsugar: Parser[App] = LISTLPAREN ~> expr.* <~ RPAREN ^^ {
    case elements => App(Var("list"), elements)
  }
  implicit def vecsugar: Parser[App] = VECLPAREN ~> expr.* <~ RPAREN ^^ {
    case elements => App(Var("vector"), elements)
  }
  implicit def literals = intlit | charlit | boollit | stringlit | listsugar | vecsugar


  implicit def ifthel: Parser[If] = LPAREN ~> IF ~> expr ~ expr ~ expr <~ RPAREN ^^ {
    case cond ~ thn ~ els => If(cond, thn, els)
  }

  implicit def condBranch: Parser[CondBr] = LPAREN ~> expr ~ expr <~ RPAREN ^^ {
    case cond ~ thn => CondBr(cond, thn)
  }
  implicit def condElseBranch: Parser[CondBr] = LPAREN ~> ELSE ~> expr <~ RPAREN ^^ {
    case thn => CondBr(BoolLit(true), thn)
  }
  implicit def condProcBranch: Parser[CondProcBr] = LPAREN ~> expr ~ (RARROW ~> lam) <~ RPAREN ^^ {
    case cond ~ proc => CondProcBr(cond, proc)
  }
  implicit def condBranches = condElseBranch | condBranch | condProcBranch
  implicit def cond: Parser[Cond] = LPAREN ~> COND ~> condBranches.* <~ RPAREN ^^ {
    case branches => Cond(branches)
  }

  implicit def caseBranch: Parser[CaseBranch] = LPAREN ~> (LPAREN ~> expr.* <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case cases ~ thn => CaseBranch(cases, thn)
  }
  implicit def caseElseBranch: Parser[CaseBranch] = LPAREN ~> ELSE ~> expr <~ RPAREN ^^ {
    case thn => CaseBranch(List(), thn)
  }
  implicit def cas: Parser[Case] = LPAREN ~> CASE ~> expr ~ (caseElseBranch | caseBranch).* <~ RPAREN ^^ {
    case ev ~ branches => Case(ev, branches)
  }

  implicit def dispatch = ifthel | cond | cas

  implicit def void: Parser[Void] = LPAREN ~> VOID <~ RPAREN ^^ { _ => Void() }

  implicit def define: Parser[Define] = LPAREN ~> DEF ~> IDENT ~ expr <~ RPAREN ^^ {
    case id ~ e => Define(id, e)
  }

  implicit def definefunc: Parser[Define] = LPAREN ~> DEF ~> (LPAREN ~> IDENT.+ <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case idents ~ e => Define(idents.head, Lam(idents.tail, e))
  }

  implicit def set: Parser[Set_!] = LPAREN ~> SET ~> IDENT ~ expr <~ RPAREN ^^ {
    case id ~ e => Set_!(id, e)
  }

  implicit def begin: Parser[Begin] = LPAREN ~> BEGIN ~> expr.* <~ RPAREN ^^ {
    case exps => Begin(exps)
  }

  // rule is causing infinite recursion
  implicit def implicit_begin: Parser[Begin] = expr ~ expr.+ ^^ {
    case exp ~ exps => Begin(exp :: exps)
  }

  implicit def imp_structure: Parser[Expr] = void | define | definefunc | set | begin

  def expr: Parser[Expr] = literals | variable | lam | lets | dispatch | imp_structure | app
}


object LargeSchemeParser extends LargeSchemeParserTrait {
  def apply(input: String): Option[Expr] = apply(expr, input)

  def apply[T](pattern: Parser[T], input: String): Option[T] = parse(pattern, input) match {
    case Success(matched, _) => Some(matched)
    case e => println(e); None
  }
}

object TestSimpleDirectLargeSchemeParser {
  def main(args: Array[String]) = {
    if (!args.isEmpty) {
      println(LargeSchemeParser(args(0)))
    } else {
      testall()
    }
  }

  def testall() = {
    val tests: List[() => Unit] = List(test1, test3, test4)
    tests foreach { _() }
  }

  // Test 1: Letrec to set!
  def test1() = {
    val actual = LargeSchemeParser("(letrec ([a 3] [b a]) (add a b))")
    val expected = Some(
      App(
        Lam(List("a", "b"),
          Begin(
            List(
              Set_!("a",IntLit(3)),
              Set_!("b",Var("a")),
              App(Var("add"),List(Var("a"), Var("b")))
            )
          )
        ),
      List(Void(), Void()))
    )
    assert(actual == expected)
  }

  // Test 2: Test implicit
  def test2() = {
    val actual = LargeSchemeParser("(add a b) (add a b)")
    val expected = Some(
      Begin(List(
        App(Var("add"), List(Var("a"), Var("b"))),
        App(Var("add"), List(Var("a"), Var("b")))
      ))
    )
    assert(actual == expected)
  }

  def test3() = {
    val actual = LargeSchemeParser("(+ (- a b) (* (/ 2 3) 4))")
    val expected = Some(
      App(Var("+"), List(
        App(Var("-"), List(Var("a"), Var("b"))),
        App(Var("*"), List(App(Var("/"), List(IntLit(2), IntLit(3))), IntLit(4)))))
    )
    assert(actual == expected)
  }

  def test4() = {
    val actual = LargeSchemeParser("(define (f a b) (+ a b))")
    val expected = Some(Define("f", Lam(List("a", "b"), App(Var("+"), List(Var("a"), Var("b"))))))
    assert(actual == expected)
  }
}
