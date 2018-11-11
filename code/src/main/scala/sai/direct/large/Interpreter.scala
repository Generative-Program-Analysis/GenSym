package sai.direct.large

import sai.direct.large.parser._
import sai.direct.large.concrete._

object LargeSchemeInterpreter {
  def apply(s: String) = {
    val ast = LargeSchemeParser(s) match {
      case Some(expr) => LargeSchemeASTDesugar(expr)
    }
    BigStepCES.eval(ast)
  }

  def main(args: Array[String]) = {
    if (!args.isEmpty) {
      println(apply(args(0))._1)
    }
  }
}
