package sai.evaluation

import scala.io.Source

import sai.lms._
import sai.evaluation.utils._
import sai.evaluation.parser._
import sai.evaluation.TestPrograms._

object Evaluation {

  def main(args: Array[String]) {
    val progs = List[(Expr, String)](
      //boyer
      //euclid,
      //euclid_imp
      //kcfa32
      (kcfa16, "kcfa16")
    )
    progs foreach { case (e, id) => compare(e, id) }
  }

  def run[R](n: Int, block: => R): Timing = {
    /* warm up*/
    //for (i <- 1 to n) block
    var tsum: List[Double] = Nil
    for (i <- 1 to n) {
      val (result, t) = Utils.time(block)
      println(s"iteration #$i took ${t}s")
      tsum = t::tsum
    }
    val dropN = (0.0 * n).toInt
    val dropTs = tsum.sorted.drop(dropN).take(n - dropN*2)
    Timing(dropTs)
  }

  def evalUnstaged(e: Expr): Unit = {
    val res = UnstagedSchemeAnalyzer.run(e)
    //println(s"Number of values: ${res._1.size}")
    //println(s"Size of cache: ${res._2.size}")
  }

  def specialize(e: Expr): DslDriver[Unit, Unit] = {
    ???
  }

  def compare(e: Expr, id: String) {
    val t1 =  run(10, { evalUnstaged(e) })
    println(s"[$id] [unstaged] - ${t1}s")
  
    /*
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
    */
  }
}
