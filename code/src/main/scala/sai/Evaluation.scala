package sai

import scala.io.Source

import sai.utils._
import sai.direct.large.ai._
import sai.evaluation.TestPrograms._
import sai.direct.large.parser._

object Evaluation extends AbsLamCalTrait {

  def main(args: Array[String]) {
    val prog = blur
    println(prog)
    compare(prog)
  }

  def run[R](n: Int, block: => R): Timing = {
    /* warm up*/
    //for (i <- 1 to n) block
    var tsum: List[Double] = Nil
    for (i <- 1 to n) {
      val (result, t) = Utils.time(block)
      tsum = t::tsum
    }
    val dropN = (0.0 * n).toInt
    val dropTs = tsum.sorted.drop(dropN).take(n - dropN*2)
    Timing(dropTs)
  }

  def compare(e: Expr) {
    /*
    val ex4fold = new SnippetEx4Fold()
    val t5 = run(n, { ex4fold() })
    println(s"0CFA Staged (fold) average time: $t5")
    */
    val t1 = run(10, { evalUnstaged(e) })
    println(s"Unstaged time - ${t1}")

    val code = specialize(e)
    code.precompile
    println("finished precompile")
    // val code_str = code.code
    // val writer = new java.io.PrintWriter(new java.io.File("CodeGen.scala.ignore"))
    // writer.write(code_str)
    // writer.close()
    println("start running")

    val t2 = run(10, { code.eval(()) })

    println("-------------------------------------------")
    println(s"Unstaged time - ${t1}")
    println(s"Staged time - ${t2}")
  }
}
