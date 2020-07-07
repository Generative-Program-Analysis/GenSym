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

trait SMTExpr

// Stage polymorphic interface/syntax of SAT solving
// TODO refactor variable for bools
trait StagePolySAT { op =>
  type R[_]
  type SATBool
  type Model

  def lit(b: Boolean): R[SATBool]
  def boolVar(x: String): R[SATBool]
  // FIXME: allow eq?
  //def eq(x: R[SATBool], y: R[SATBool]): R[SATBool]
  def or(x: R[SATBool], y: R[SATBool]): R[SATBool]
  def not(x: R[SATBool]): R[SATBool]
  def and(x: R[SATBool], y: R[SATBool]): R[SATBool]
  def xor(x: R[SATBool], y: R[SATBool]): R[SATBool]
  def ite(cnd: R[SATBool], thn: R[SATBool], els: R[SATBool]): R[SATBool]
  def iff(x: R[SATBool], y: R[SATBool]): R[SATBool]
  def implies(x: R[SATBool], y: R[SATBool]): R[SATBool]

  def push: R[Unit]
  def pop: R[Unit]
  def assert(x: R[SATBool]): R[Unit]

  def query(x: R[SATBool]): R[Int]
  def check: R[Boolean]
  def check(x: String): R[(Boolean, Boolean)]
  def getModel: R[Model]

  object SyntaxSAT {
    implicit def __lit(b: Boolean): R[SATBool] = lit(b)
    // implicit def __var(x: String): R[SATBool] = boolVar(x)
    implicit class BOps(x: R[SATBool]) {
      def ==(y: R[SATBool]): R[SATBool] = op.iff(x, y)
      def ≡(y: R[SATBool]): R[SATBool] = op.iff(x, y)
      def or(y: R[SATBool]): R[SATBool] = op.or(x, y)
      def unary_!(): R[SATBool] = op.not(x)
      def and(y: R[SATBool]): R[SATBool] = op.and(x, y)
      def xor(y: R[SATBool]): R[SATBool] = op.xor(x, y)
      def ⇔(y: R[SATBool]): R[SATBool] = op.iff(x, y)
      def ==>(y: R[SATBool]): R[SATBool] = op.implies(x, y)
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
  trait SATBool extends SMTExpr
  trait Model
  // TODO: LMS stuff, generate C/STP code
  def lit(b: Boolean): R[SATBool] =
    Wrap[SATBool](Adapter.g.reflect("sat-bool-lit", Backend.Const(b)))
  def boolVar(x: String): R[SATBool] = 
    Wrap[SATBool](Adapter.g.reflect("sat-bool-var", Backend.Const(x)))
  //def eq(x: R[SATBool], y: R[SATBool]): R[SATBool] =
    //Wrap[SATBool](Adapter.g.reflect("sat-eq", Unwrap(x), Unwrap(y)))
  def or(x: R[SATBool], y: R[SATBool]): R[SATBool] =
    Wrap[SATBool](Adapter.g.reflect("sat-or", Unwrap(x), Unwrap(y)))
  def and(x: R[SATBool], y: R[SATBool]): R[SATBool] =
    Wrap[SATBool](Adapter.g.reflect("sat-and", Unwrap(x), Unwrap(y)))
  def xor(x: R[SATBool], y: R[SATBool]): R[SATBool] =
    Wrap[SATBool](Adapter.g.reflect("sat-xor", Unwrap(x), Unwrap(y)))
  def implies(x: R[SATBool], y: R[SATBool]): R[SATBool] =
    Wrap[SATBool](Adapter.g.reflect("sat-implies", Unwrap(x), Unwrap(y)))
  def iff(x: R[SATBool], y: R[SATBool]): R[SATBool] =
    Wrap[SATBool](Adapter.g.reflect("sat-iff", Unwrap(x), Unwrap(y)))
  def not(x: R[SATBool]): R[SATBool] =
    Wrap[SATBool](Adapter.g.reflect("sat-not", Unwrap(x)))
  def ite(cnd: R[SATBool], thn: R[SATBool], els: R[SATBool]): R[SATBool] =
    Wrap[SATBool](Adapter.g.reflect("sat-ite", Unwrap(cnd), Unwrap(thn), Unwrap(els)))

  def assert(x: R[SATBool]): R[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("sat-assert", Unwrap(x))(Backend.Const("CTRL")))
  def check: R[Boolean] =
    Wrap[Boolean](Adapter.g.reflectWrite("sat-check")(Backend.Const("CTRL")))
  def check(x: String): R[(Boolean, Boolean)] = ???

  def printAsserts: R[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("sat-printAsserts")(Backend.Const("CTRL")))
  def query(x: R[SATBool]): R[Int] =
    Wrap[Int](Adapter.g.reflectWrite("sat-query", Unwrap(x))(Backend.Const("CTRL")))
  def handle(n: R[Int]): R[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("sat-handle", Unwrap(n))(Backend.Const("CTRL")))


  def push: R[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("sat-push")(Backend.Const("CTRL")))
  def pop: R[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("sat-pop")(Backend.Const("CTRL")))
  def getModel: R[Model] =
    Wrap[Model](Adapter.g.reflect("sat-getModel"))
}

trait STPCodeGen_SAT extends CppSAICodeGenBase {
  registerHeader("../stp/include", "<stp/c_interface.h>")
  registerHeader("./headers", "<stp_handle.hpp>")

  override def remap(m: Manifest[_]): String = {
    if (m.runtimeClass.getName.contains("SATBool")) {
      "Expr"
    } else { super.remap(m) }
  }

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, name, _, _) if name.startsWith("sat") => false
    case _ => super.mayInline(n)
  }

  override def traverse(n: Node): Unit = n match {
    case Node(s, "sat-assert", List(x), _) => 
      emit("vc_assertFormula(vc, "); shallow(x); emitln(");")
    case Node(s, "sat-printAsserts", _, _) => 
      emitln("vc_printAsserts(vc, 1);")
    case Node(s, "sat-handle", List(x), _) =>
      emit("handle(vc, "); shallow(x); emitln(");");
    case _ => super.traverse(n)
  }

  override def shallow(n: Node) = n match {
    case Node(s, "sat-bool-lit", Const(b: Boolean)::_, _) if b => emit("vc_trueExpr(vc)")
    case Node(s, "sat-bool-lit", Const(b: Boolean)::_, _) if !b => emit("vc_falseExpr(vc)")
    case Node(s, "sat-bool-var", Const(ident: String)::_, _) => 
      emit(s"""vc_varExpr(vc, \"$ident\", vc_boolType(vc))""");
    // ??? eq may not work as expected
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
    case Node(s, "sat-check", _, _) =>
      ???
      // emit("vc_query(vc, vc_trueExpr(vc))")
    case Node(s, "sat-query", List(x), _) => 
      emit("vc_query(vc, "); shallow(x); emit(")")
    case Node(s, "sat-pop", _, _) => ???
    case Node(s, "sat-push", _, _) => ???
    case _ => super.shallow(n)
  }
}

// A SMTLib2 expression builder, use Z3 as backend
trait SMTLib2ExprBuilder extends UnstagedSAT {
  private val varSet = HashSet[String]()
  private val prelude = new StringBuilder()
  private val constraints = new StringBuilder()

  val filename = "constraints.smt2"

  type SATBool = String
  type Model = String

  def lit(b: Boolean): SATBool = b.toString
  def boolVar(x: String): SATBool = {
    varSet += x
    x
  }
  def eq(x: SATBool, y: SATBool): SATBool = s"(= $x $y)"
  def or(x: SATBool, y: SATBool): SATBool = s"(or $x $y)"
  def not(x: SATBool): SATBool = s"(not $x)"
  def and(x: SATBool, y: SATBool): SATBool = s"(and $x $y)"
  def xor(x: SATBool, y: SATBool): SATBool = s"(xor $x $y)"
  def ite(cnd: SATBool, thn: SATBool, els: SATBool): SATBool = s"(ite $cnd $thn $els)"
  def iff(x: SATBool, y: SATBool): SATBool = and(implies(x, y), implies(y, x))
  def implies(x: SATBool, y: SATBool): SATBool = s"(=> $x $y)"

  def push: Unit = constraints ++= "(push)\n"
  def pop: Unit = constraints ++= "(pop)\n"
  def assert(x: String): Unit = constraints ++= s"(assert $x)\n"
  def query(x: String): Int = ???

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
}