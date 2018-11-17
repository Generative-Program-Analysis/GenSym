package sai

import scala.io.Source

import sai.utils._
import sai.direct.large.ai._
import sai.evaluation.TestPrograms._
import sai.direct.large.parser._

object Evaluation extends AbsLamCalTrait {
  def compare(e: Expr) {
    /*
    val ex4fold = new SnippetEx4Fold()
    val t5 = run(n, { ex4fold() })
    println(s"0CFA Staged (fold) average time: $t5")
    */
    val t1 = Utils.time { evalUnstaged(e) }
    println(s"Unstaged time - ${t1._2}")

    val code = specialize(e)
    code.precompile
    println("finished precompile")
    val code_str = code.code
    val writer = new java.io.PrintWriter(new java.io.File("CodeGen.scala.ignore"))
    writer.write(code_str)
    writer.close()

    val t2 = Utils.time { code.eval(()) }
    println("-------------------------------------------")
    println(s"Unstaged time - ${t1._2}")
    println(s"Staged time - ${t2._2}")
  }

  def main(args: Array[String]) {
    val prog = church
    compare(prog)
  }
}
