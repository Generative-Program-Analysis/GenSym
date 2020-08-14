package sai.lmsx.smt

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

import sai.lmsx._

object SATTest extends App {
  /*
   def testExprBuilder(): Unit = {
   val sat = new SMTLib2ExprBuilder {
   import SyntaxSAT._
   val x = boolVar("x")
   val y = boolVar("y")
   assert(x ⇔ y)
   assert(x == true)
   assert(! x)
   assert(x ==> y)
   //assert(y ≡ false)
   }
   println(sat.getModel)
   println(sat.print_debug)
   }
   */

  def testSAT(): CppSAIDriver[Int, Unit] = new CppSAIDriver[Int, Unit] {
    def build(expect: Boolean)(e: => Rep[Boolean]): Unit = {
      push
      val res = e
      unchecked("vc_printAsserts(vc, 1);")
      println(res)
      if (expect) unchecked("assert(", res, ")")
      else unchecked("assert(!", res, ")")
      pop
    }

    def snippet(x: Rep[Int]) = {
      // A few sanity checks
      // p valid <=> ¬p unsat
      // p invalid <=> ¬p sat
      // p sat <=> ¬p invalid
      // p unsat <=> ¬p valid
      val p = boolVar("p")
      val q = boolVar("q")
      val r = boolVar("r")
      build(false) {
        isValid(or(p, q))
      }
      build(true) {
        assert(p)
        isValid(or(p, q))
      }
      build(true) {
        assert(p)
        assert(q)
        isValid(and(p, q))
      }
      build(false) {
        isValid(p)
      }
      build(true) {
        assert(q)
        isValid(imply(p, q))
      }
      build(false) {
        assert(q)
        isValid(imply(q, p))
      }
      build(false) {
        assert(not(p))
        isValid(p)
      }
      build(true) {
        assert(p)
        val np = not(p)
        assert(np)
        isValid(and(p, np))
      }
      build(false) {
        assert(p)
        val np = not(p)
        assert(np)
        isSat(and(p, np))
      }
      build(false) {
        isSat(lit(false))
      }
      build(false) {
        assert(p)
        isSat(lit(false))
      }
      build(true) {
        isSat(lit(true))
      }
      build(false) {
        assert(p)
        assert(not(q))
        isSat(and(p, q))
      }
      build(true) {
        assert(not(p))
        isSat(imply(q, p))
      }
      println("Done")
    }
  }

  def testSMT(): CppSAIDriver[Int, Unit] = new CppSAIDriver[Int, Unit] {
    def snippet(x: Rep[Int]) = {
      {
        import SyntaxSMT._
        import SyntaxSAT._
        implicit val bw: Int = 32
        val c = bvVar("c")
        val a: Rep[BV] = 5
        val b: Rep[BV] = 6
        handle(query((a + b) ≠ c))
      }
      println("Done")
    }
  }

  val code = testSAT()
  print(code.code)
  code.eval(0)
}

