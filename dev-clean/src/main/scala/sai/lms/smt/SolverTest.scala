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

  trait STPTestBuilder { self: CppSAIDriver[_, _] =>
    def build(expect: Boolean)(e: => Rep[Boolean]): Unit = {
      push
      val res = e
      // unchecked("vc_printAsserts(vc, 1)")
      // println(res)
      if (expect) unchecked("assert(", res, ")")
      else unchecked("assert(!", res, ")")
      pop
    }

    def build(expect: Boolean, value: Int)(e: => (Rep[Boolean], Rep[SMTExpr])): Unit = {
      push
      val (res, v) = e
      if (expect) unchecked("assert(", res, ")")
      else unchecked("assert(!", res, ")")
      val ce = getCounterEx(v)
      val ce_int = bvToInt(ce.asInstanceOf[Rep[BV]])
      unchecked(s"assert($value == ", ce_int, ")")
      pop
    }
  }

  def testSAT(): CppSAIDriver[Int, Unit] =
    new CppSAIDriver[Int, Unit] with STPTestBuilder {
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
        build(true) {
          assert(lit(false))
          isValid(lit(false))
        }
        build(false) {
          isValid(lit(false))
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

  def testBV(): CppSAIDriver[Int, Unit] = new CppSAIDriver[Int, Unit] with STPTestBuilder {
    def snippet(x: Rep[Int]) = {
      import SyntaxSMT._
      import SyntaxSAT._
      implicit val bw: Int = 32
      val a: Rep[BV] = 5
      val b: Rep[BV] = 6
      val c = bvVar("c")
      build(false) {
        isValid((a + b) ≠ c)
      }
      build(true) {
        assert((a + b) ≡ 11)
        isValid(lit(true))
      }
      build(false) {
        assert((a + b) ≡ 12)
        isSat(lit(true))
      }
      build(true, 5) {
        val x: Rep[BV] = 3
        val y: Rep[BV] = 4
        assert(((x*x) + (y*y)) ≡ (c*c))
        (isSat(true), c)
      }
      build(true) {
        val eq = a ≡ b
        val d = ite(eq, 15: Rep[BV], 16: Rep[BV])
        assert(d ≡ (16: Rep[BV]))
        isSat(true)
      }
      build(false) {
        val eq = a ≡ b
        val d = ite(eq, 15: Rep[BV], 16: Rep[BV])
        assert(d ≡ (15: Rep[BV]))
        isSat(true)
      }
      build(true, 16) {
        val eq = a ≡ b
        val d = ite(eq, 15: Rep[BV], 16: Rep[BV])
        (isSat(true), d)
      }
      build(true) {
        val x = bvFromStr("00000000000000000000000000000101")
        assert(x ≡ a)
        isValid(true)
      }
      build(true) {
        val t = bvFromBool(true)
        isValid(t ≡ lit(1)(1))
      }
      build(true) {
        val t = bvFromBool(false)
        isValid(t ≡ lit(0)(1))
      }
      println("Done")
    }
  }

  {
    val code = testSAT()
    println(code.code)
    code.eval(0)
  }
  {
    val code = testBV()
    print(code.code)
    code.eval(0)
  }
}

