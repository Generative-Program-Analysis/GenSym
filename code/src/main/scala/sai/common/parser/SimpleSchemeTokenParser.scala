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

  def LISTLPAREN = "'("
  def VECLPAREN = "#("
  
  def DEF: Parser[String] = "define"
  def LAMBDA: Parser[String] = "lambda"
  def IDENT: Parser[String] = """[a-zA-Z!$%*/:<=>?~_^][a-zA-Z0-9!$%*/:<=>?~_^.+-@]*""".r

  def IF: Parser[String] = "if"
  def IF0: Parser[String] = "if0"
  def COND: Parser[String] = "cond"
  def ELSE: Parser[String] = "else"

  def LET: Parser[String] = "let"
  def LETSTAR: Parser[String] = "let*"
  def LETREC: Parser[String] = "letrec"

  def LIST: Parser[String] = "list"
  def CAR: Parser[String] = "car"
  def CDR: Parser[String] = "cdr"
  def CONS: Parser[String] = "cons"

  def VECTOR: Parser[String] = "vector"

  def TRUE: Parser[Boolean] = "#t" ^^ { _ => true }
  def FALSE: Parser[Boolean] = "#f" ^^ { _ => false }

  def DIGIT10: Parser[String] = """[0-9]+""".r
  def INT10: Parser[Int] = DIGIT10 ^^ { _.mkString.toInt }

  def PRIMOP: Parser[String] = "+" | "-" | "*" | "/" | "=" | "eq?"
}
