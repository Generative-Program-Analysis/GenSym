package sai
package evaluation

import scala.io.Source

import sai.utils._
import sai.direct.large.ai._
import sai.evaluation.TestPrograms._
import sai.direct.large.parser._

object Evaluation extends AbsLamCalTrait {

  def main(args: Array[String]) {
    val progs = List(
      //boyer
      //euclid,
      //euclid_imp
      //kcfa32
    )
    progs foreach { compare(_) }
  }

  def run[R](n: Int, block: => R): Timing = {
    /* warm up*/
    //for (i <- 1 to n) block
    var tsum: List[Double] = Nil
    for (i <- 1 to n) {
      val (result, t) = Utils.time(block)
      println(s"iteration #$i took $t")
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
    val t1 =  run(20, { evalUnstaged(e) })
    println(s"Unstaged time - ${t1}")

    val code = specialize(e)
    code.precompile
    println("Finished precompile, writing code to CodeGen.scala.ignore")
    val writer = new java.io.PrintWriter(new java.io.File("CodeGen.scala.ignore"))
    writer.write(code.code)
    writer.close()
    println("Start running compiled version")

    val t2 = run(20, { code.eval(()) })

    println("-------------------------------------------")
    println(s"Unstaged time - ${t1}")
    println(s"Staged time - ${t2}")
  }
}
