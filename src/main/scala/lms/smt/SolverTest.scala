package gensym.lmsx.smt

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

import gensym.lmsx._

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

  def kenken(): CppSAIDriver[Int, Unit] =
    new CppSAIDriver[Int, Unit] with STPTestBuilder {
      /* ----------
       * |3+   |? |
       * |--------|
       * |3 |4+   |
       * |------  |
       * |5+   |  |
       * |--------|
       */
      @virtualize
      def snippet(x: Rep[Int]) = {
        import SyntaxSMT._
        import SyntaxSAT._
        implicit val bw: Int = 32
        val x11 = bvVar("x11")
        val x12 = bvVar("x12")
        val x13 = bvVar("x13")
        val x21 = bvVar("x21")
        val x22 = bvVar("x22")
        val x23 = bvVar("x23")
        val x31 = bvVar("x31")
        val x32 = bvVar("x32")
        val x33 = bvVar("x33")

        assert((x11 ≥ 1) ∧ (x11 ≤ 3))
        assert((x12 ≥ 1) ∧ (x12 ≤ 3))
        assert((x13 ≥ 1) ∧ (x13 ≤ 3))
        assert((x21 ≥ 1) ∧ (x21 ≤ 3))
        assert((x22 ≥ 1) ∧ (x22 ≤ 3))
        assert((x23 ≥ 1) ∧ (x23 ≤ 3))
        assert((x31 ≥ 1) ∧ (x31 ≤ 3))
        assert((x32 ≥ 1) ∧ (x32 ≤ 3))
        assert((x33 ≥ 1) ∧ (x33 ≤ 3))

        assert((x11 + x12 + x13) ≡ 6)
        assert((x21 + x22 + x23) ≡ 6)
        assert((x31 + x32 + x33) ≡ 6)

        assert((x11 + x21 + x31) ≡ 6)
        assert((x12 + x22 + x32) ≡ 6)
        assert((x13 + x23 + x33) ≡ 6)

        assert((x11 + x12) ≡ 3)
        assert(x21 ≡ 3)
        assert((x22 + x23 + x33) ≡ 4)
        assert((x31 + x32) ≡ 5)

        val r = isSat(true)
        unchecked("assert(", r, ")")

        val x11_v = bvToInt(getCounterEx(x11))
        unchecked(s"assert(1 == ", x11_v, ")")

        val x12_v = bvToInt(getCounterEx(x12))
        unchecked(s"assert(2 == ", x12_v, ")")

        val x13_v = bvToInt(getCounterEx(x13))
        unchecked(s"assert(3 == ", x13_v, ")")

        val x22_v = bvToInt(getCounterEx(x22))
        unchecked(s"assert(1 == ", x22_v, ")")

        val x23_v = bvToInt(getCounterEx(x23))
        unchecked(s"assert(2 == ", x23_v, ")")

        val x31_v = bvToInt(getCounterEx(x31))
        unchecked(s"assert(2 == ", x31_v, ")")

        val x32_v = bvToInt(getCounterEx(x32))
        unchecked(s"assert(3 == ", x32_v, ")")

        val x33_v = bvToInt(getCounterEx(x33))
        unchecked(s"assert(1 == ", x33_v, ")")

        println("Done")
      }
    }

  def testArray(): CppSAIDriver[Int, Unit] =
    new CppSAIDriver[Int, Unit] with STPTestBuilder {
      @virtualize
      def snippet(x: Rep[Int]) = {
        import SyntaxSMT._
        import SyntaxSAT._

        implicit val bw: Int = 32
        // 1d array
        val array1 = arrayCreate("a", 32, 32)
        // 2d array each cell has 10 elements
        val array2 = arrayCreate("b", 32, 32, 10)

        build(true) {
          val a = (array1(2) = lit(3))
          val b = a(2)
          isValid(b ≡ lit(3))
        }

        build(false) {
          val a = (array1(2) = lit(3))
          val b = array1(2)
          isValid(b ≡ lit(3))
        }

        build(true) {
          val a = (array1(2) = lit(3))
          val b = (a(2) = lit(10))
          val c = b(2)
          val d = a(2)
          isValid((c ≡ lit(10)) ∧ (d ≡ lit(3)))
        }

        build(true) {
          val a = (array2(3, 5) = (lit(20)))
          val b = a(3, 5)
          isValid(b ≡ lit(20))
        }

        build(true) {
          val a = (array2(3, 5) = lit(20))
          val b = (a(5, 2) = lit(1))
          val c = b(3, 5)
          val d = b(5, 2)
          isValid((c + d) ≡ lit(21))
        }

        build(true) {
          val a = arrayConstCreate(collection.immutable.List(lit(3), lit(5), lit(8)), 32, 32)
          isValid((a(0) + a(1) + a(2)) ≡ lit(16))
        }

        println("Done")
      }
    }

  def kenkenArray(): CppSAIDriver[Int, Unit] =
    new CppSAIDriver[Int, Unit] with STPTestBuilder {
      @virtualize
      def snippet(x: Rep[Int]) = {
        import SyntaxSMT._
        import SyntaxSAT._

        implicit val bw: Int = 32
        val kenken = arrayCreate("kenken", 32, 32, 3)
        val x11 = kenken(0, 0)
        val x12 = kenken(0, 1)
        val x13 = kenken(0, 2)
        val x21 = kenken(1, 0)
        val x22 = kenken(1, 1)
        val x23 = kenken(1, 2)
        val x31 = kenken(2, 0)
        val x32 = kenken(2, 1)
        val x33 = kenken(2, 2)

        assert((x11 ≥ 1) ∧ (x11 ≤ 3))
        assert((x12 ≥ 1) ∧ (x12 ≤ 3))
        assert((x13 ≥ 1) ∧ (x13 ≤ 3))
        assert((x21 ≥ 1) ∧ (x21 ≤ 3))
        assert((x22 ≥ 1) ∧ (x22 ≤ 3))
        assert((x23 ≥ 1) ∧ (x23 ≤ 3))
        assert((x31 ≥ 1) ∧ (x31 ≤ 3))
        assert((x32 ≥ 1) ∧ (x32 ≤ 3))
        assert((x33 ≥ 1) ∧ (x33 ≤ 3))

        assert((x11 + x12 + x13) ≡ 6)
        assert((x21 + x22 + x23) ≡ 6)
        assert((x31 + x32 + x33) ≡ 6)

        assert((x11 + x21 + x31) ≡ 6)
        assert((x12 + x22 + x32) ≡ 6)
        assert((x13 + x23 + x33) ≡ 6)

        assert((x11 + x12) ≡ 3)
        assert(x21 ≡ 3)
        assert((x22 + x23 + x33) ≡ 4)
        assert((x31 + x32) ≡ 5)

        val r = isSat(true)
        unchecked("assert(", r, ")")

        val x11_v = bvToInt(getCounterEx(x11))
        unchecked(s"assert(1 == ", x11_v, ")")

        val x12_v = bvToInt(getCounterEx(x12))
        unchecked(s"assert(2 == ", x12_v, ")")

        val x13_v = bvToInt(getCounterEx(x13))
        unchecked(s"assert(3 == ", x13_v, ")")

        val x22_v = bvToInt(getCounterEx(x22))
        unchecked(s"assert(1 == ", x22_v, ")")

        val x23_v = bvToInt(getCounterEx(x23))
        unchecked(s"assert(2 == ", x23_v, ")")

        val x31_v = bvToInt(getCounterEx(x31))
        unchecked(s"assert(2 == ", x31_v, ")")

        val x32_v = bvToInt(getCounterEx(x32))
        unchecked(s"assert(3 == ", x32_v, ")")

        val x33_v = bvToInt(getCounterEx(x33))
        unchecked(s"assert(1 == ", x33_v, ")")

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
  {
    val code = kenken()
    print(code.code)
    code.eval(0)
  }
  {
    val code = testArray()
    print(code.code)
    code.eval(0)
  }
  {
    val code = kenkenArray()
    print(code.code)
    code.eval(0)
  }
}
