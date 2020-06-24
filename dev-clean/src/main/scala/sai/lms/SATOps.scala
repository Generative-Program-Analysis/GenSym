package sai
package lmsx

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.core.utils.time
import lms.macros.SourceContext

import sys.process._

import java.io.PrintStream

import scala.collection.mutable.HashSet
import scala.collection.mutable.StringBuilder

// Stage polymorphic interface/syntax of SAT solving
trait StagePolySAT { op =>
  type R[_]
  type SATBool
  type Model

  type B = SATBool

  def lit(b: Boolean): R[B]
  def variable(x: String): R[B]
  def eq(x: R[B], y: R[B]): R[B]
  def or(x: R[B], y: R[B]): R[B]
  def not(x: R[B]): R[B]
  def and(x: R[B], y: R[B]): R[B]
  def xor(x: R[B], y: R[B]): R[B]
  def ite(cnd: R[B], thn: R[B], els: R[B]): R[B]
  def iff(x: R[B], y: R[B]): R[B]
  def implies(x: R[B], y: R[B]): R[B]

  def push: R[Unit]
  def pop: R[Unit]
  def assert(x: R[B]): R[Unit]

  def check: R[Boolean]
  def check(x: String): R[(Boolean, Boolean)]
  def getModel: R[Model]

  object Syntax {
    implicit def __lit(b: Boolean): R[B] = lit(b)
    implicit def __var(x: String): R[B] = variable(x)
    implicit class BOps(x: R[B]) {
      def ≡(y: R[B]): R[B] = op.eq(x, y)
      def or(y: R[B]): R[B] = op.or(x, y)
      def unary_!(): R[B] = op.not(x)
      def and(y: R[B]): R[B] = op.and(x, y)
      def xor(y: R[B]): R[B] = op.xor(x, y)
      def ⇔(y: R[B]): R[B] = op.iff(x, y)
      def ==>(y: R[B]): R[B] = op.implies(x, y)
    }
  }
}

// Unstaged SAT solver
trait UnstagedSAT extends StagePolySAT {
  type R[T] = T
}

// Staged SAT solver
trait StagedSATOps extends StagePolySAT with Base {
  type R[T] = Rep[T]
  // TODO: LMS stuff, generate C/STP code
}

// A SMTLib2 expression builder, use Z3 as backend
trait SMTLib2ExprBuilder extends UnstagedSAT {
  private val varSet = HashSet[String]()
  private val prelude = new StringBuilder()
  private val constraints = new StringBuilder()

  val filename = "constraints.smt2"

  type SATBool = String
  type Model = String

  def lit(b: Boolean): B = b.toString
  def variable(x: String): B = {
    varSet += x
    x
  }
  def eq(x: B, y: B): B = s"(= $x $y)"
  def or(x: B, y: B): B = s"(or $x $y)"
  def not(x: B): B = s"(not $x)"
  def and(x: B, y: B): B = s"(and $x $y)"
  def xor(x: B, y: B): B = s"(xor $x $y)"
  def ite(cnd: B, thn: B, els: B): B = s"(ite $cnd $thn $els)"
  def iff(x: B, y: B): B = and(implies(x, y), implies(y, x))
  def implies(x: B, y: B): B = s"(=> $x $y)"

  def push: Unit = constraints ++= "(push)\n"
  def pop: Unit = constraints ++= "(pop)\n"
  def assert(x: String): Unit = constraints ++= s"(assert $x)\n"

  private def build(f: PrintStream => Unit): Unit = {
    varSet.foreach { x =>
      prelude ++= s"(declare-const $x Bool)\n"
    }
    val out = new PrintStream(filename)
    out.println(prelude)
    out.println(constraints)
    f(out)
    out.close
  }

  def check: Boolean = {
    build { ps => ps.println("(check-sat)") }
    val output: String = (s"z3 ./${filename}": ProcessBuilder).!!
    output == "sat\n"
  }
  def check(x: String): (Boolean, Boolean) = {
    // TODO: extract the model of `x`
    ???
  }
  def getModel: Model = {
    build { ps =>
      ps.println("(check-sat)")
      ps.println("(get-model)")
    }
    // TODO: properly represent unknown and timeout
    var sat: String = "unknown"
    val model = new StringBuilder()
    val r = (s"z3 ./${filename}": ProcessBuilder)! ProcessLogger { line =>
      line match {
        case "sat" => sat = line
        case "unsat" => sat = line
        case _ if sat == "sat" =>
          model ++= (line ++ "\n")
        case _ =>
      }
    }
    if (sat == "sat") model.toString else sat
  }
}

// Stage polymorphic interface
trait StagePolySMT extends StagePolySAT {
  type BV
  // TODO: how to represent different kind of SMT expression (and how can we compose different theories/logics
  // TODO: how to specify size of BVs
  // TODO: operations on BV
}

object SMTTest {
  def main(args: Array[String]): Unit = {
    val sat = new SMTLib2ExprBuilder {
      import Syntax._
      val x = variable("x")
      val y = variable("y")
      assert(x ⇔ y)
      assert(x ≡ true)
      assert(! x)
      assert(x ==> y)
      //assert(y ≡ false)
    }
    println(sat.getModel)
    //println(sat.check)
  }
}
