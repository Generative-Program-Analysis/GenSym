package sai.evaluation

import lms.core._
import lms.core.stub.{Timing => _, _}
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

import sai.lmsx._
import sai.evaluation.utils._
import sai.evaluation.parser._
import sai.evaluation.TestPrograms._
import sai.evaluation.parser.ASTUtils._

import scala.io.Source

object Evaluation {

  type Progs = List[(Expr, String)]

  abstract class Option
  case class WithStoreWidening(progs: Progs) extends Option
  case class WithoutStoreWidening(progs: Progs) extends Option

  def output(id: String): String = s"CodeGen_$id.out"

  val N = 20
  val sw = "sw"
  val wo_sw = "wo_sw"

  def progs_wo_sw: Progs = List[(Expr, String)](
    //(map, "map")
    (fib, "fib"),
    (rsa, "rsa"),
    (church, "church"),
    (fermat, "fermat"),
    (mbrotZ, "mbrotZ"),
    (lattice, "lattice"),
    (kcfa16, "kcfa16"),
    (kcfa32, "kcfa32"),
    (kcfa64, "kcfa64"),
    (solovay, "solovay")
  )

  def progs_w_sw: Progs = progs_wo_sw ++ List[(Expr, String)](
    (regex, "regex"),
    //(matrix, "matrix")
  )

  def progs_all: Progs = progs_w_sw ++ List[(Expr, String)](
    (blur, "blur"),
    (sat, "sat"),
    (metacirc, "meta"),
    (scheme2java, "scheme2java"),
    (letloop, "letloop"),
    (euclid, "euclid"),
    (euclid_imp, "euclid_imp"),
    (fact5, "fact5"),
    (kcfa3, "kcfa3"),
    (kcfa256, "kcfa256"),
    (omega, "omega"),
    (boyer, "boyer"),
    (earley, "earley"),
    (dynamic, "dynamic"),
    (graphs, "graphs"),
    (nbody, "nbody"),
    (nucleic, "nucleic")
  )

  def main(args: Array[String]) {
    //runEvaluation(WithoutStoreWidening(progs_wo_sw))
    println("\n********************************************\n")
    runEvaluation(WithStoreWidening(progs_w_sw))
  }

  def runEvaluation(opt: Option) = opt match {
    case WithoutStoreWidening(progs) =>
      println("Start running evaluation for CFA without store-widening")
      progs foreach { case (e, id) =>
        compare(evalUnstaged, specialize)(e, id + "_0cfa")
        println("============================================")
      }
      println("End running evaluation for CFA without store-widening")
    case WithStoreWidening(progs) =>
      println("Start running evaluation for CFA with store-widening")
      progs foreach {case (e, id) =>
        compare(evalUnstagedSW, specializeSW)(e, id + "_0cfa-sw")
        println("============================================")
      }
      println("End running evaluation for CFA with store-widening")
  }

  def specialize(e: Expr): SAIDriver[Unit, Unit] = new StagedSchemeAnalyzerDriver {
    @virtualize
    def snippet(u: Rep[Unit]): Rep[Unit] = {
      val res = run_once(e)
      val store = res._1
      println(store.size)
    }
  }

  def specializeSW(e: Expr): SAIDriver[Unit, Unit] = new SWStagedSchemeAnalyzerDriver {
    @virtualize
    def snippet(u: Rep[Unit]): Rep[Unit] = {
      val res = run_once(e)
      val vs = res._1
      val vals = vs._1
      println(vals.size)
    }
  }

  def evalUnstaged(e: Expr): Unit = {
    val res = UnstagedSchemeAnalyzer.run_once(e)
      val store = res._1
      println(store.size)
  }

  def evalUnstagedSW(e: Expr): Unit = {
    val res = SWUnstagedSchemeAnalyzer.run_once(e)
      val vs = res._1
      val vals = vs._1
      println(vals.size)
  }

  def writeTo(filename: String, content: String): Unit = {
    val writer = new java.io.PrintWriter(new java.io.File(filename))
    writer.write(content)
    writer.close()
  }

  def compare(eval: Expr => Unit, spec: Expr => SAIDriver[Unit, Unit])(e: Expr, id: String): Unit = {
    println(s"Running evaluation for $id, AST size: ${size(e)}")
    val (res1, t1) = run(N, { eval(e) })
    println(s"[$id] [unstaged] - ${t1}s")

    val code = spec(e)
    val outfile = output(id)
    println(s"[$id] [staged] Finished precompile, writing code to ${outfile}")
    writeTo(outfile, code.code)
    code.precompile
    val (res2, t2) = run(N, { code.eval(()) })
    println(s"[$id] [staged] - ${t2}s")
    println(s"[$id] Median speedup - ${t2.median_speedup(t1)}")
  }

  def run[R](n: Int, block: => R): (R, Timing) = {
    val res_time: List[(R, Double)] =
      (1 to n).toList.map({ i =>
        val (result, t) = Utils.time(block)
        println(s"  Iteration #$i took ${t}s")
        (result, t)
      })
    val ress = res_time.map(_._1)
    val times = res_time.map(_._2)
    val dropN = (0.0 * n).toInt // No dropout
    val dropTs = times.sorted.drop(dropN).take(n - dropN*2)
    (ress(0), Timing(dropTs))
  }

}
