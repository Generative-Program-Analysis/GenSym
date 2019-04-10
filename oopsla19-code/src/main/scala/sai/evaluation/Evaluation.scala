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
import sai.evaluation.parser.ASTUtils._
import sai.evaluation.TestPrograms._

object Evaluation {

  def main(args: Array[String]) {
    val progs = List[(Expr, String)](
      (fib, "fib"),
      (rsa, "rsa"),
      //(church, "church"),
      (fermat, "fermat"),
      (mbrotZ, "mbrotZ"),
      (lattice, "lattice"),
      (kcfa16, "kcfa16"),
      (kcfa32, "kcfa32"),
      (kcfa64, "kcfa64"),
      (solovay, "solovay")
      //(regex, "regex"),    //okay for sw
      //(matrix, "matrix"),  //okay for sw
      //(blur, "blur"), // 56
      //(sat, "sat"), // 73
      //(metacirc, "meta")
      //(scheme2java, "scheme2java")
      /********************/
      //(letloop, "letloop")
      //(euclid, "euclid"),
      //(euclid_imp, "euclid_imp"),
      //(fact5, "fact5"),
      //(kcfa3, "kcfa3"),
      //(kcfa256, "kcfa256"),
      //(omega, "omega")
      //(boyer, "boyer"),
      //(earley, "earley"),
      //(dynamic, "dynamic"),
      //(graphs, "graphs"),
      //(nbody, "nbody"),
      //(nucleic, "nucleic"),
    )
    progs foreach { case (e, id) => compareWO_SW(e, id) }
    //progs foreach { case (e, id) => compareSW(e, id) }
  }

  def evalUnstaged(e: Expr): Unit = {
    val res = UnstagedSchemeAnalyzer.run(e)
    println(s"Number of values: ${res._1.size}")
  }

  def evalUnstagedSW(e: Expr): Unit = {
    val res = SWUnstagedSchemeAnalyzer.run(e)
    println(s"Number of values: ${res._1._1.size}")
  }

  def specialize(e: Expr): DslDriver[Unit, Unit] = new StagedSchemeAnalyzerDriver {
    @virtualize
    def snippet(u: Rep[Unit]): Rep[Unit] = {
      val res = run(e)
      print("Number of values: ") 
      println(res._1.size)
    }
  }

  def specializeSW(e: Expr): DslDriver[Unit, Unit] = new SWStagedSchemeAnalyzerDriver {
    @virtualize
    def snippet(u: Rep[Unit]): Rep[Unit] = {
      val res = run(e)
      println(s"Number of values:" + res._1)
    }
  }

  def writeTo(filename: String, content: String): Unit = {
    val writer = new java.io.PrintWriter(new java.io.File(filename))
    writer.write(content)
    writer.close()
  }

  val output = "CodeGen.out"
  def output(id: String): String = s"CodeGen_$id.out"

  val N = 20
  val sw = "sw"
  val wo_sw = "wo_sw"

  def compare(mode: String)(e: Expr, id: String): Unit = {
    println(s"Running [$mode] $id, AST size: ${size(e)}")
    val t1 = if (mode == sw) run(N, { evalUnstagedSW(e) }) else run(N, { evalUnstaged(e) })
    println(s"[$id] [unstaged] - ${t1}s")
    
    val code = if (mode == sw) specializeSW(e) else specialize(e)
    code.precompile
    val outfile = output(id)
    println(s"[$id] [staged] Finished precompile, writing code to ${outfile}")
    writeTo(outfile, code.code)

    val t2 = run(N, { code.eval(()) })
    println(s"[$id] [staged] - ${t2}s")
  }

  def compareSW(e: Expr, id: String) = compare(sw)(e, id)

  def compareWO_SW(e: Expr, id: String) = compare(wo_sw)(e, id)

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

}
