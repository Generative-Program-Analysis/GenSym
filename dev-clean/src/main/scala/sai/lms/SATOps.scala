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

trait SATBoolRep

// Staged SAT solver
trait StagedSATOps extends Base with Equal with StagePolySAT { 
  type R[T] = Rep[T]
  trait SATBool
  trait Model
  // TODO: LMS stuff, generate C/STP code
  def lit(b: Boolean): R[B] =
    Wrap[B](Adapter.g.reflectWrite("sat-bool-lit", Backend.Const(b))(Backend.Const("CTRL")))
  def variable(x: String): R[B] = 
    Wrap[B](Adapter.g.reflectWrite("sat-bool-var", Backend.Const(x))(Backend.Const("CTRL")))
  def eq(x: R[B], y: R[B]): R[B] =
    Wrap[B](Adapter.g.reflect("sat-eq", Unwrap(x), Unwrap(y)))
  def or(x: R[B], y: R[B]): R[B] =
    Wrap[B](Adapter.g.reflect("sat-or", Unwrap(x), Unwrap(y)))
  def and(x: R[B], y: R[B]): R[B] =
    Wrap[B](Adapter.g.reflect("sat-and", Unwrap(x), Unwrap(y)))
  def xor(x: R[B], y: R[B]): R[B] =
    Wrap[B](Adapter.g.reflect("sat-xor", Unwrap(x), Unwrap(y)))
  def implies(x: R[B], y: R[B]): R[B] =
    Wrap[B](Adapter.g.reflect("sat-implies", Unwrap(x), Unwrap(y)))
  def iff(x: R[B], y: R[B]): R[B] =
    Wrap[B](Adapter.g.reflect("sat-iff", Unwrap(x), Unwrap(y)))
  def not(x: R[B]): R[B] =
    Wrap[B](Adapter.g.reflect("sat-not", Unwrap(x)))
  def ite(cnd: R[B], thn: R[B], els: R[B]): R[B] =
    Wrap[B](Adapter.g.reflect("sat-ite", Unwrap(cnd), Unwrap(thn), Unwrap(els)))

  def assert(x: R[B]): R[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("sat-assert", Unwrap(x))(Backend.Const("CTRL")))
  def check: R[Boolean] =
    Wrap[Boolean](Adapter.g.reflect("sat-check"))
  def check(x: String): R[(Boolean, Boolean)] = ???

  def push: R[Unit] =
    Wrap[Unit](Adapter.g.reflect("sat-push"))
  def pop: R[Unit] =
    Wrap[Unit](Adapter.g.reflect("sat-pop"))
  def getModel: R[Model] =
    Wrap[Model](Adapter.g.reflect("sat-getModel"))
}

trait STPCodeGen_SAT extends ExtendedCCodeGen {
  // register header

  override def remap(m: Manifest[_]): String = {
    if (m.runtimeClass.getName.contains("SATBool")) {
      "Expr"
    } else { super.remap(m) }
  }

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, name, _, _) if name.startsWith("sat") => false
    case _ => super.mayInline(n)
  }

  override def shallow(n: Node) = n match {
    case Node(s, "sat-bool-lit", Const(b: Boolean)::_, _) if b => emit("vc_trueExpr(vc)")
    case Node(s, "sat-bool-lit", Const(b: Boolean)::_, _) if !b => emit("vc_trueFalse(vc)")
    case Node(s, "sat-bool-var", Const(ident: String)::_, _) => 
      emit(s"""vc_varExpr(vc, \"$ident\", vc_boolType(vc))""");
    case Node(s, "sat-eq", List(l, r), _) => 
      emit("vc_eqExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "sat-or", List(l, r), _) =>
      emit("vc_orExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "sat-and", List(l, r), _) =>
      emit("vc_andExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "sat-xor", List(l, r), _) =>
      emit("vc_xorExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "sat-not", List(x), _) => 
      emit("vc_notExpr(vc, "); shallow(x); emit(")")
    case Node(s, "sat-implies", List(l, r), _) =>
      emit("vc_impliesExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "sat-iff", List(l, r), _) =>
      emit("vc_iffExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "sat-ite", List(c, t, e), _) =>
      emit("vc_iffExpr(vc, "); shallow(c); emit(", "); shallow(t); emit(", "); shallow(e); emit(")")
    case Node(s, "sat-assert", List(x), _) => 
      emit("vc_assertFormula(vc, "); shallow(x); emit(")")
    // TODO check
    case _ => super.shallow(n)
  }
}

trait STPCodeGen extends ExtendedCCodeGen {
  // TODO register header
  // TODO remap SATBool => Expr
  // vc???
  override def shallow(n: Node): Unit = ???
}

// Stage polymorphic interface
trait StagePolySMT extends StagePolySAT {
  type BV
  // TODO: how to represent different kind of SMT expression (and how can we compose different theories/logics
  // TODO: how to specify size of BVs
  // TODO: operations on BV
  val bw32: Int = 32

  def bvConstExprFromInt(v: Int, bitWidth: Int = bw32): R[BV]
  def bvConstExprFromStr(s: String, bitWidth: Int = bw32): R[BV]
  // TODO: variable?
  def bvVariable(s: String, bitWidth: Int = bw32): R[BV]

  // bv arith
  // DLL_PUBLIC Expr vc_bvPlusExpr(VC vc, int bitWidth, Expr left, Expr right);
  def bvPlus(x: R[BV], y: R[BV]): R[BV]
  def bvMul(x: R[BV], y: R[BV]): R[BV]
  def bvDiv(x: R[BV], y: R[BV]): R[BV]
  def bvMinus(x: R[BV], y: R[BV]): R[BV]
  def bvMod(x: R[BV], y: R[BV]): R[BV]
  def bvNeg(x: R[BV])
  
  // bv compare
  def bvLt(x: R[BV], y: R[BV]): R[BV]
  def bvLe(x: R[BV], y: R[BV]): R[BV]
  def bvGt(x: R[BV], y: R[BV]): R[BV]
  def bvGe(x: R[BV], y: R[BV]): R[BV]
  def bvEq(x: R[BV], y: R[BV]): R[BV]

  // bv bitwise
  def bvAnd(x: R[BV], y: R[BV]): R[BV]
  def bvOr(x: R[BV], y: R[BV]): R[BV]
  def bvXor(x: R[BV], y: R[BV]): R[BV]
  def bvNot(x: R[BV]): R[BV]
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

  def print_debug: Unit = {
    varSet.foreach { x =>
      prelude ++= s"(declare-const $x Bool)\n"
    }
    println(prelude)
    println(constraints)
  }
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
    println(sat.print_debug)
  }
}