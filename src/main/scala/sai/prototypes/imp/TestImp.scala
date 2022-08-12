package sai.imp

import sai.lang._

import sai.lmsx._
import sai.lmsx.smt._

import lms.core._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

object TestImp {
  import sai.imp._
  import ImpLang._
  import ImpLang.Examples._

  // Testing concrete evaluation of expressions
  @virtualize
  def specializeExpr(e: Expr): CppSAIDriver[Int, Int] =
    new CppStagedImpDriver[Int, Int] {
      def snippet(u: Rep[Int]) = {
        val v: Rep[Value] = eval(e, Map())
        println(v)
        v
      }
    }

  // Testing concrete execution of statements
  @virtualize
  def specialize(s: Stmt): CppSAIDriver[Int, Int] =
    new CppStagedImpDriver[Int, Int] {
      def snippet(u: Rep[Int]) = {
        val v = exec(s)(Map("x" -> IntV(3), "z" -> IntV(4))).run
        v._2("fact")
      }
    }

  // Testing symbolic execution of statements using `StagedSymMonad`
  @virtualize
  def specSym(s: Stmt): SAIDriver[Unit, Unit] =
    new SymStagedImpDriver[Unit, Unit] {
      def snippet(u: Rep[Unit]) = {
        //val init: Rep[Ans] = (Map("x" -> IntV(3), "z" -> IntV(4), "y" -> SymV("y")),
        //                      Set[Expr]())
        val init: Rep[Ans] = (Map("n" -> SymVBV("n")), Set[SMTBool]())
        val v = exec(s)(init).run
        println(v)
        println("path number: ")
        println(v.size)
      }
    }

  // Testing symbolic execution of statements using `StagedSymMonad`
  @virtualize
  def specSymCpp(s: Stmt): CppSAIDriver[Int, Unit] =
    new CppSymStagedImpDriver[Int, Unit] {
      def snippet(u: Rep[Int]) = {
        //val init: Rep[Ans] = (Map("n" -> SymV("n")), Set[Expr]())
        val init: Rep[Ans] = (Map("y" -> IntV(3)), Set[SMTBool]())
        val v: Rep[List[(Unit, Ans)]] = exec(s)(init).run
        //println(v)
        //println("path number: ")

        v.foreach(l => handle(query(not(l._2._2.foldLeft(lit(true))(and(_, _))))))
        println(v.size)
      }
    }

  def main(args: Array[String]): Unit = {
    /*
    val code = specializeExpr(Op2("+", Lit(1), Lit(2)))
    println(code.code)
    code.eval(0)
     */

    /* Concrete execution */
    /*
    val code = specialize(fact5)
    println(code.code)
    code.eval(0)
     */

    /* Symbolic execution */
    /*
    val code = specSym(condAssert)
    println(code.code)
    code.eval(0)
     */

    /* Generating impure code using a single state thread */
    ImpureStagedImpTest.test
  }
}
