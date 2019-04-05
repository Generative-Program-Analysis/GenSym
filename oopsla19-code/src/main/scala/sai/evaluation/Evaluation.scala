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
      //(kcfa16, "kcfa16"),
      //(kcfa32, "kcfa32"),
      //(kcfa64, "kcfa64"),
      //(church, "church")
      //(fermat, "fermat"),
      //(rsa, "rsa"),
      //(mbrotZ, "mbrotZ"),
      //(lattice, "lattice"),
      //(blur, "blur"), // 56
      //(sat, "sat"), // 73
      //(solovay, "solovay")
      //(metacirc, "meta")
      (regex, "regex"),
      //(scheme2java, "scheme2java")
      /********************/
      //(matrix, "matrix"),
      //(letloop, "letloop")
      //(euclid, "euclid"),
      //(euclid_imp, "euclid_imp"),
      //(fact5, "fact5"),
      //(kcfa3, "kcfa3"),
      //(kcfa256, "kcfa256"),
      //(omega, "omega")
      //(fib, "fib"), // 36
      //(boyer, "boyer"),
      //(earley, "earley"),
      //(dynamic, "dynamic"),
      //(graphs, "graphs"),
      //(nbody, "nbody"),
      //(nucleic, "nucleic"),
    )
    //progs foreach { case (e, id) => compare(e, id) }
    progs foreach { case (e, id) => 
      println(s"$id - ${size(e)}")
      println(e)
      println(ASTUtils.exprToString(e))
      println(ASTUtils.free(e).map("\"" + _ + "\""))
      println(ASTUtils.free(e).map("\"" + _ + "\" =>\n"))
      val t1 =  run(1, { evalUnstaged(e) })
      println(s"[$id] [unstaged] - ${t1}s")
    }
    /*
    progs foreach { case (e, id) =>
      val code = specialize(e)
      code.precompile
      val outfile = output(id)
      println(s"[$id] [staged] Finished precompile, writing code to ${outfile}")
      writeTo(outfile, code.code)
      code.eval(())
    }
    */
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
    //println(s"Values: ${res._1}")
    println(s"Number of values: ${res._1.size}")
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
  def output(id: String): String = s"CodeGen_$id.out"

  def compare(e: Expr, id: String) {
    println(s"Running $id, AST size: ${size(e)}")
    val N = 20
    val t1 =  run(N, { evalUnstaged(e) })
    println(s"[$id] [unstaged] - ${t1}s")
    
    val code = specialize(e)
    code.precompile
    val outfile = output(id)
    println(s"[$id] [staged] Finished precompile, writing code to ${outfile}")
    writeTo(outfile, code.code)

    val t2 = run(N, { code.eval(()) })
    println(s"[$id] [staged] - ${t2}s")
  }
}
