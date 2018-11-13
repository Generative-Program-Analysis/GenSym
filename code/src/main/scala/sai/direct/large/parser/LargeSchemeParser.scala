package sai.direct.large.parser

import scala.util.parsing.combinator._
import scala.io.Source
import sai.utils.TestTrait
import sai.common.parser._

trait LargeSchemeParserTrait extends SchemeTokenParser {
  def id[T](x: T) = x

  implicit def variable: Parser[Var] = IDENT ^^ { Var(_) }

  implicit def app: Parser[App] = LPAREN ~> expr ~ expr.* <~ RPAREN ^^ {
    case e ~ param => App(e, param)
  }

  implicit def lam: Parser[Lam] = LPAREN ~> LAMBDA ~> (LPAREN ~> IDENT.* <~ RPAREN) ~ implicit_begin <~ RPAREN ^^ {
    case args ~ body => Lam(args, body)
  }

  implicit def bind: Parser[Bind] = LPAREN ~> IDENT ~ implicit_begin <~ RPAREN ^^ {
    case id ~ e => Bind(id, e)
  }

  implicit def lets: Parser[Expr] = let | letstar | letrec | letproc

  implicit def let: Parser[App] = LPAREN ~> LET ~> (LPAREN ~> bind.+ <~ RPAREN) ~ implicit_begin <~ RPAREN ^^ {
    case binds ~ body => Let(binds, body).toApp
  }

  implicit def letproc: Parser[App] = LPAREN ~> LET ~> IDENT ~ (LPAREN ~> bind.+ <~ RPAREN) ~ implicit_begin <~ RPAREN ^^ {
    case ident ~ binds ~ body =>
      Lrc(List(Bind(ident, Lam(binds.map(_.x), body))), App(Var(ident), binds.map(_.e))).toApp
  }

  implicit def letstar: Parser[App] = LPAREN ~> LETSTAR ~> (LPAREN ~> bind.+ <~ RPAREN) ~ implicit_begin <~ RPAREN ^^ {
    case binds ~ body =>
      binds.dropRight(1).foldRight (Let(List(binds.last), body).toApp) { case (bd, e) => Let(List(bd), e).toApp }
  }

  implicit def letrec: Parser[App] = LPAREN ~> LETREC ~> (LPAREN ~> bind.+ <~ RPAREN) ~ implicit_begin <~ RPAREN ^^ {
    case binds ~ body => Lrc(binds, body).toApp
  }

  implicit def intlit: Parser[IntLit] = INT10 ^^ { IntLit(_) }
  implicit def floatlit: Parser[FloatLit] = FLOAT ^^ { FloatLit(_) }
  implicit def boollit: Parser[BoolLit] = (TRUE | FALSE) ^^ { BoolLit(_) }
  implicit def charlit: Parser[CharLit] = CHARLIT ^^ {
    case s => CharLit(s.charAt(2))
  }
  implicit def stringlit: Parser[App] = STRINGLIT ^^ {
    case str =>
      val elements: List[CharLit] = str.toCharArray map { CharLit(_) } toList;
      App(Var("vector"), elements.drop(1).dropRight(1))
  }

  implicit def vecsugar: Parser[App] = VECLPAREN ~> expr.* <~ RPAREN ^^ {
    case elements => App(Var("vector"), elements)
  }

  implicit def literals = floatlit | intlit | charlit | boollit | stringlit | vecsugar

  implicit def ifthel: Parser[If] = LPAREN ~> IF ~> expr ~ expr ~ expr <~ RPAREN ^^ {
    case cond ~ thn ~ els => If(cond, thn, els)
  }

  implicit def condBranch: Parser[CondBr] = LPAREN ~> expr ~ implicit_begin <~ RPAREN ^^ {
    case cond ~ thn => CondBr(cond, thn)
  }
  implicit def condElseBranch: Parser[CondBr] = LPAREN ~> ELSE ~> implicit_begin <~ RPAREN ^^ {
    case thn => CondBr(BoolLit(true), thn)
  }
  implicit def condProcBranch: Parser[CondProcBr] = LPAREN ~> expr ~ (RARROW ~> implicit_begin) <~ RPAREN ^^ {
    case cond ~ proc => CondProcBr(cond, proc)
  }
  implicit def condBranches = condElseBranch | condBranch | condProcBranch
  implicit def cond: Parser[Cond] = LPAREN ~> COND ~> condBranches.* <~ RPAREN ^^ {
    case branches => Cond(branches)
  }

  implicit def caseBranch: Parser[CaseBranch] = LPAREN ~> (LPAREN ~> expr.* <~ RPAREN) ~ implicit_begin <~ RPAREN ^^ {
    case cases ~ thn => CaseBranch(cases, thn)
  }
  implicit def caseElseBranch: Parser[CaseBranch] = LPAREN ~> ELSE ~> implicit_begin <~ RPAREN ^^ {
    case thn => CaseBranch(List(), thn)
  }
  implicit def cas: Parser[Case] = LPAREN ~> CASE ~> implicit_begin ~ (caseElseBranch | caseBranch).* <~ RPAREN ^^ {
    case ev ~ branches => Case(ev, branches)
  }

  implicit def dispatch = ifthel | cond | cas

  implicit def void: Parser[Void] = LPAREN ~> VOID <~ RPAREN ^^ { _ => Void() }

  implicit def define: Parser[Define] = LPAREN ~> DEF ~> IDENT ~ implicit_begin <~ RPAREN ^^ {
    case id ~ e => Define(id, e)
  }

  implicit def definefunc: Parser[Define] = LPAREN ~> DEF ~> (LPAREN ~> IDENT.+ <~ RPAREN) ~ implicit_begin <~ RPAREN ^^ {
    case idents ~ e => Define(idents.head, Lam(idents.tail, e))
  }

  implicit def set: Parser[Set_!] = LPAREN ~> SET ~> IDENT ~ implicit_begin <~ RPAREN ^^ {
    case id ~ e => Set_!(id, e)
  }

  implicit def begin: Parser[Begin] = LPAREN ~> BEGIN ~> expr.* <~ RPAREN ^^ {
    case exps => Begin(exps)
  }

  implicit def implicit_begin: Parser[Expr] = expr.+ ^^ {
    case e :: Nil => e
    case exps @ (e :: es) => Begin(exps)
  }

  implicit def imp_structure: Parser[Expr] = void | define | definefunc | set | begin

  implicit def quasiquote: Parser[Expr] = QUASIQUOTE ~> quasiterm ^^ id

  implicit def symbol: Parser[Sym] = SYMBOL ^^ { Sym(_) }

  implicit def list: Parser[App] = LPAREN ~> quasiterm.* <~ RPAREN ^^ {
    case terms => App(Var("list"), terms)
  }
  implicit def unquote: Parser[Expr] = UNQUOTE ~> expr ^^ id

  implicit def quasiterm = literals | unquote | list | symbol

  def expr: Parser[Expr] = literals | quasiquote | variable | lam | lets | dispatch | imp_structure | app

  def program = implicit_begin
}

object LargeSchemeParser extends LargeSchemeParserTrait {
  def apply(input: String): Option[Expr] = apply(program, input)

  def apply[T](pattern: Parser[T], input: String): Option[T] = parse(pattern, input) match {
    case Success(matched, _) => Some(matched)
    case e => println(e); None
  }
}

object TestSimpleDirectLargeSchemeParser extends TestTrait {
  def main(args: Array[String]) = {
    if (args.isEmpty) {
      runtest()
    } else {
      if (args(0) == "-f") {
        println(LargeSchemeParser(Source.fromFile(args(1)).mkString))
      } else if (args(0) == "-t") {
        runtest(args(1))
      } else {
        println(LargeSchemeParser(args(0)))
      }
    }
  }

  override def testall() = {
    test("quasiquote") {
      assert(LargeSchemeParser("'xxxx") == Some(Sym("xxxx")))
      assert(LargeSchemeParser("'(a 1 ,(a b))")
        == Some(App(Var("list"),List(Sym("a"), IntLit(1), App(Var("a"),List(Var("b")))))))
    }

    test("letrec_to_set") {
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
        List(Void(), Void())))
      assert(actual == expected)
    }

    test("implicit") {
      val actual = LargeSchemeParser("(add a b) (add a b)")
      val expected = Some(
        Begin(List(
          App(Var("add"), List(Var("a"), Var("b"))),
          App(Var("add"), List(Var("a"), Var("b")))
        ))
      )
      assert(actual == expected)
    }

    test("arith") {
      val actual = LargeSchemeParser("(+ (- a b) (* (/ 2 3) 4))")
      val expected = Some(
        App(Var("+"), List(
          App(Var("-"), List(Var("a"), Var("b"))),
          App(Var("*"), List(App(Var("/"), List(IntLit(2), IntLit(3))), IntLit(4)))))
      )
      assert(actual == expected)
    }

    test("define_proc") {
      val actual = LargeSchemeParser("(define (f a b) (+ a b))")
      val expected = Some(Define("f", Lam(List("a", "b"), App(Var("+"), List(Var("a"), Var("b"))))))
      assert(actual == expected)
    }

    test("cond") {
      val actual = LargeSchemeParser(
        """(cond
            [(positive? -5) (error 1)]
            [(zero? -5) (error 2)]
            [(positive? 5) 'here])""")
      val expected = Some(Cond(List(
        CondBr(App(Var("positive?"),List(IntLit(-5))),App(Var("error"),List(IntLit(1)))),
        CondBr(App(Var("zero?"),List(IntLit(-5))),App(Var("error"),List(IntLit(2)))),
        CondBr(App(Var("positive?"),List(IntLit(5))),Sym("here")))))
      assert(actual == expected)
    }

    test("toplas98_boyer") {
      val fileName = "benchmarks/toplas98/boyer.sch"
      val program = Source.fromFile(fileName).mkString
      assert(LargeSchemeParser(program) != None)
    }

    test("toplas98_nbody_comments") {
      val fileName1 = "benchmarks/toplas98/nbody.sch"
      val fileName2 = "benchmarks/toplas98/nbody-processed.sch"
      val program1 = Source.fromFile(fileName1).mkString
      val program2 = Source.fromFile(fileName2).mkString

      assert(LargeSchemeParser(program1) == LargeSchemeParser(program2))
    }

    test("toplas98_lattice_comments") {
      val fileName1 = "benchmarks/toplas98/lattice.scm"
      val fileName2 = "benchmarks/toplas98/lattice-processed.scm"
      val program1 = Source.fromFile(fileName1).mkString
      val program2 = Source.fromFile(fileName2).mkString

      assert(LargeSchemeParser(program1) == LargeSchemeParser(program2))
    }

    test("bool") {
      val t = "#t"
      val T = "#T"
      val f = "#f"
      val F = "#F"
      assert(LargeSchemeParser(t) == Some(BoolLit(true)))
      assert(LargeSchemeParser(T) == Some(BoolLit(true)))
      assert(LargeSchemeParser(f) == Some(BoolLit(false)))
      assert(LargeSchemeParser(F) == Some(BoolLit(false)))
    }

    test("float") {
      assert(LargeSchemeParser("3.14") == Some(FloatLit(3.14)))
      assert(LargeSchemeParser("-3.14") == Some(FloatLit(-3.14)))
      assert(LargeSchemeParser("0.00000") == Some(FloatLit(0.0)))
    }
  }
}
