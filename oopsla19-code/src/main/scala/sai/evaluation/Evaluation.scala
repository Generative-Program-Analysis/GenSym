package sai.evaluation

import scala.virtualization.lms.internal.GenericNestedCodegen
import scala.virtualization.lms.common.{
  SetOps => _, SetOpsExp => _, ScalaGenSetOps => _,
  ListOps => _, ListOpsExp => _, ScalaGenListOps => _,
  _}
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

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
      //(fact5, "fact5")
      //(kcfa16, "kcfa16"),
      //(kcfa32, "kcfa32"),
      //(kcfa64, "kcfa64"),
      (kcfa256, "kcfa256")
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

  def specialize(e: Expr): DslDriver[Unit, Unit] = new StagedSchemeAnalyzerDriver {
    @virtualize
    def snippet(u: Rep[Unit]): Rep[Unit] = {
      val res = run(e)
      //println(s"Number of values:" + res._1)
      //println(s"Size of cache:" + res._2.size)
      //println(res._1)
      //println(res._2.size)
    }
  }

  def writeTo(filename: String, content: String): Unit = {
    val writer = new java.io.PrintWriter(new java.io.File(filename))
    writer.write(content)
    writer.close()
  }

  val output = "CodeGen.out"

  def compare(e: Expr, id: String) {
    val N = 20

    val t1 =  run(N, { evalUnstaged(e) })
    println(s"[$id] [unstaged] - ${t1}s")
  
    val code = specialize(e)
    code.precompile
    println(s"[$id] [staged] Finished precompile, writing code to ${output}")
    writeTo(output, code.code)

    val t2 = run(N, { code.eval(()) })
    println(s"[$id] [staged] - ${t2}s")

    //println("Result:")
    //println(s"  Unstaged time - ${t1}")
    //println(s"  Staged time - ${t2}")
  }
}
