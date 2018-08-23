package sai.parser

import scala.util.parsing.combinator._

/** Syntax reference:
  * TSPL 4th: https://www.scheme.com/tspl4/grammar.html
  * Boucher and Feeley (1996): http://www.iro.umontreal.ca/~feeley/papers/BoucherFeeleyCC96.pdf
  */

trait SimpleSchemeTokenParser extends RegexParsers {
  override def skipWhitespace = true

  def LPAREN = "[\\(\\{\\[]".r
  def RPAREN = "[\\)\\}\\]]".r

  def IF: Parser[String] = "if"
  def LET: Parser[String] = "let"
  def LAMBDA: Parser[String] = "lambda"
  def LETREC: Parser[String] = "letrec"

  def TRUE: Parser[Boolean] = "#t" ^^ { _ => true }
  def FALSE: Parser[Boolean] = "#f" ^^ { _ => false }

  def IDENT: Parser[String] = """[a-zA-Z!$%*/:<=>?~_^][a-zA-Z0-9!$%*/:<=>?~_^.+-@]*""".r

  def DIGIT10: Parser[String] = """[0-9]""".r
  def INT10: Parser[Int] = DIGIT10.+ ^^ { _.mkString.toInt }

  def PRIMOP: Parser[String] = "+" | "-" | "*" | "/" | "="
}
