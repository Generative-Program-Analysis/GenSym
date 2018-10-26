package sai.common.parser

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

  def QUOTE = "'"
  def SYMBOL = """\'[^ \(\{\[\)\}\]]+""".r

  def LISTLPAREN = "'("
  def VECLPAREN = "#("

  def LAMBDA: Parser[String] = "lambda"
  def IDENT: Parser[String] = """[a-zA-Z!$%*/:<=>?~_^.+\-@][a-zA-Z0-9!$%*/:<=>?~_^.+\-@]*""".r

  def DEF: Parser[String] = "define"
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

  def TRUE: Parser[Boolean] = "#t" ^^ { _ => true }
  def FALSE: Parser[Boolean] = "#f" ^^ { _ => false }

  def DIGIT10: Parser[String] = """\-?[0-9]+""".r
  def INT10: Parser[Int] = DIGIT10 ^^ { _.mkString.toInt }
  def STRINGLIT: Parser[String] = """"(\\"|[^"])*\"""".r
  def CHARLIT: Parser[String] = """#\\.""".r

  def PRIMOP: Parser[String] = "+" | "-" | "*" | "/" | "=" | "eq?"
}
