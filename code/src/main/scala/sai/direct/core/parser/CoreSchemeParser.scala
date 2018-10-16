package sai.direct.core.parser

import scala.util.parsing.combinator._
import sai.common.parser._
import sai.common.parser.Read._

trait CoreSchemeParserTrait extends SchemeTokenParser {
  implicit def variable: Parser[Var] = IDENT ^^ { Var(_) }

  implicit def app: Parser[App] = LPAREN ~> expr ~ expr <~ RPAREN ^^ {
    case e1 ~ e2 => App(e1, e2)
  }

  implicit def lam: Parser[Lam] = LPAREN ~> LAMBDA ~> (LPAREN ~> IDENT <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case arg ~ body => Lam(arg, body)
  }

  implicit def bind: Parser[Bind] = LPAREN ~> IDENT ~ expr <~ RPAREN ^^ {
    case id ~ e => Bind(id, e)
  }

  implicit def let: Parser[Expr] = LPAREN ~> LET ~> (LPAREN ~> (LPAREN ~> IDENT ~ expr <~ RPAREN) <~ RPAREN) ~ expr <~ RPAREN ^^ {
    //case id ~ e ~ body => Let(id, e, body)
    case id ~ e ~ body => App(Lam(id, body), e)
  }

  implicit def letrec: Parser[Lrc] = LPAREN ~> LETREC ~> (LPAREN ~> bind.+ <~ RPAREN) ~ expr <~ RPAREN ^^ {
    case binds ~ body => Lrc(binds, body)
  }

  implicit def lit: Parser[Lit] = INT10 ^^ { Lit(_) }

  implicit def if0: Parser[If0] = LPAREN ~> IF0 ~> expr ~ expr ~ expr <~ RPAREN ^^ {
    case cond ~ th ~ el => If0(cond, th, el)
  }

  implicit def aop: Parser[AOp] = LPAREN ~> PRIMOP ~ expr ~ expr <~ RPAREN ^^ {
    case op ~ e1 ~ e2 => AOp(Symbol(op), e1, e2)
  }

  def expr: Parser[Expr] = lit | aop | app | if0 | lam | let | letrec | variable
}

object CoreSchemeParser extends CoreSchemeParserTrait {
  val debug = false

  def apply(input: String): Option[Expr] = apply(expr, input)

  def apply[T](implicit pattern: Parser[T], input: String): Option[T] = parse(pattern, input) match {
    case Success(matched, _) => Some(matched)
    case e => if (debug) println(e); None
  }

  implicit val ReadStringExpr: Read[Expr] = new Read[Expr] {
    def read(s: String): Option[Expr] = CoreSchemeParser(s)
  }

  implicit def ReadString[T <: Expr](implicit pattern: Parser[T]): Read[T] = new Read[T] {
    def read(s: String): Option[T] = CoreSchemeParser(pattern, s)
  }
}

object TestCoreSchemeParser {
  import CoreSchemeParser._

  def main(args: Array[String]) = {
    assert("1".read[Var] == None)
    assert("a".read[Lit] == None)
    assert("2".read[Lit] == Some(Lit(2)))
    assert("(if0 a b c)".read[If0] == Some(If0(Var("a"), Var("b"), Var("c"))))
    assert("(if0 1 2 3)".read[If0] == Some(If0(Lit(1), Lit(2), Lit(3))))
    assert("(+ 1 2)".read[AOp] == Some(AOp('+, Lit(1), Lit(2))))
    assert("(+ 1 2)".read[Expr] == Some(AOp('+, Lit(1), Lit(2))))

    val fact5 = "(letrec ([fact (lambda (n) (if0 n 1 (* n (fact (- n 1)))))]) (fact 5))"
    assert(fact5.read[Expr].get ==
             Lrc(List(Bind("fact",
                           Lam("n",If0(Var("n"),
                                       Lit(1),
                                       AOp('*,
                                           Var("n"),
                                           App(Var("fact"),
                                               AOp('-,Var("n"),Lit(1)))))))),
                 App(Var("fact"),Lit(5))))
  }
}
