package sai.evaluation.parser

import scala.util.parsing.combinator._

/** Syntax reference:
  * TSPL 4th: https://www.scheme.com/tspl4/grammar.html
  * Boucher and Feeley (1996): http://www.iro.umontreal.ca/~feeley/papers/BoucherFeeleyCC96.pdf
  */

trait SchemeTokenParser extends RegexParsers {
  override def skipWhitespace = true
  override protected val whiteSpace = """(\s|;.*)+""".r

  def LPAREN = "[\\(\\{\\[]".r
  def RPAREN = "[\\)\\}\\]]".r

  def QUASIQUOTE: Parser[String] = "`" | "'"
  def QUOTE: Parser[String] = "quote"
  def UNQUOTE: Parser[String] = ","
  def SYMBOL: Parser[String] = """[^ \t\n\(\{\[\)\}\]]+""".r

  def LISTLPAREN: Parser[String] = "'("
  def VECLPAREN: Parser[String] = "#("

  def keyword = LAMBDA | DEF | VOID | BEGIN | SET | IF | IF0 | COND | ELSE | CASE | LET | LETSTAR | LETREC
  def LAMBDA: Parser[String] = "lambda"
  def IDENT: Parser[String] = """[a-zA-Z!$%*/:<=>?~_^.+\-@][a-zA-Z0-9!$%*/:<=>?~_^.+\-@]*""".r

  def DEF: Parser[String] = "define\\s".r
  def VOID: Parser[String] = "void"
  def BEGIN: Parser[String] = "begin"
  def SET: Parser[String] = "set!"

  def IF: Parser[String] = "if"
  def IF0: Parser[String] = "if0"
  def COND: Parser[String] = "cond"
  def RARROW: Parser[String] = "=>"
  def ELSE: Parser[String] = "else"
  def CASE: Parser[String] = "case"

  def LET: Parser[String] = "let"
  def REC: Parser[String] = "rec"
  def LETSTAR: Parser[String] = "let*"
  def LETREC: Parser[String] = "letrec"

  def TRUE: Parser[Boolean] = ("#t"|"#T") ^^ { _ => true }
  def FALSE: Parser[Boolean] = ("#f"|"#F") ^^ { _ => false }

  def DIGIT10: Parser[String] = """\-?[0-9]+""".r
  def DECIMAL: Parser[String] = """(\+|\-)?[0-9]+\.[0-9]+""".r
  def INT10: Parser[Int] = DIGIT10 ^^ { _.mkString.toInt }
  def FLOAT: Parser[Double] = DECIMAL ^^ { _.mkString.toDouble }
  def STRINGLIT: Parser[String] = """"(\\"|[^"])*\"""".r
  def CHARLIT: Parser[String] = """#\\.""".r

  def PRIMOP: Parser[String] = "+" | "-" | "*" | "/" | "=" | "eq?"
}
