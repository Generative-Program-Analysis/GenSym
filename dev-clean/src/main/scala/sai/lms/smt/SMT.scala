package sai.lmsx.smt

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
trait SMTBool extends SMTExpr
trait SMTBitVec extends SMTExpr

// Stage-polymorphic interfaces and operations

trait SMTBaseInterface { op =>
  type R[+T]

  def boolVar(x: String): R[SMTBool]
  def lit(b: Boolean): R[SMTBool]
  def not(x: R[SMTBool]): R[SMTBool]
  def or(x: R[SMTBool], y: R[SMTBool]): R[SMTBool]
  def and(x: R[SMTBool], y: R[SMTBool]): R[SMTBool]
  def xor(x: R[SMTBool], y: R[SMTBool]): R[SMTBool]
  def iff(x: R[SMTBool], y: R[SMTBool]): R[SMTBool]
  def ite(x: R[SMTBool], y: R[SMTBool], z: R[SMTBool]): R[SMTBool]
  def imply(x: R[SMTBool], y: R[SMTBool]): R[SMTBool]

  def eq(x: R[SMTExpr], y: R[SMTExpr]): R[SMTBool]

  def push: R[Unit]
  def pop: R[Unit]
  def assert(x: R[SMTBool]): R[Unit]
  def isValid(x: R[SMTBool]): R[Int]
  def checkSat: R[Boolean]
  def getCounterEx(x: R[SMTExpr]): R[SMTExpr]

  object SyntaxSAT {
    implicit def __lit(b: Boolean): R[SMTBool] = lit(b)
    implicit class BOps(x: R[SMTBool]) {
      def ==(y: R[SMTBool]): R[SMTBool] = op.eq(x, y) // of iff?
      def ≡(y: R[SMTBool]): R[SMTBool] = op.iff(x, y)
      def or(y: R[SMTBool]): R[SMTBool] = op.or(x, y)
      def unary_!(): R[SMTBool] = op.not(x)
      def and(y: R[SMTBool]): R[SMTBool] = op.and(x, y)
      def xor(y: R[SMTBool]): R[SMTBool] = op.xor(x, y)
      def ⇔(y: R[SMTBool]): R[SMTBool] = op.iff(x, y)
      def ==>(y: R[SMTBool]): R[SMTBool] = op.imply(x, y)
    }
  }
}

trait SMTBitVecInterface extends SMTBaseInterface { op =>
  type BV = SMTBitVec

  def lit(i: Int)(implicit width: Int): R[BV]
  def lit(i: R[Int])(implicit width: Int): R[BV]

  def bvConstExprFromStr(s: String)(implicit bitWidth: Int): R[BV] //TODO: why need this?
  def bvVar(s: String)(implicit bitWidth: Int): R[BV]

  // bv arith
  def bvPlus(x: R[BV], y: R[BV])(implicit bitWidth: Int): R[BV]
  def bvMul(x: R[BV], y: R[BV])(implicit bitWidth: Int): R[BV]
  def bvDiv(x: R[BV], y: R[BV])(implicit bitWidth: Int): R[BV]
  def bvMinus(x: R[BV], y: R[BV])(implicit bitWidth: Int): R[BV]
  def bvMod(x: R[BV], y: R[BV])(implicit bitWidth: Int): R[BV]
  def bvNeg(x: R[BV])(implicit bitWidth: Int): R[BV]
  
  // bv compare
  def bvLt(x: R[BV], y: R[BV]): R[SMTBool]
  def bvLe(x: R[BV], y: R[BV]): R[SMTBool]
  def bvGt(x: R[BV], y: R[BV]): R[SMTBool]
  def bvGe(x: R[BV], y: R[BV]): R[SMTBool]

  // bv bitwise
  def bvAnd(x: R[BV], y: R[BV]): R[BV]
  def bvOr(x: R[BV], y: R[BV]): R[BV]
  def bvXor(x: R[BV], y: R[BV]): R[BV]
  def bvNot(x: R[BV]): R[BV]

  object SyntaxSMT {
    implicit def __int(n: Int)(implicit bitWidth: Int): R[BV] = lit(n)(bitWidth)

    implicit class BVOps(x: R[BV]) {
      // compare
      def ≡(y: R[BV]): R[SMTBool] = op.eq(x, y)
      def >(y: R[BV]): R[SMTBool] = op.bvGt(x, y)
      def <(y: R[BV]): R[SMTBool] = op.bvLt(x, y)
      def >=(y: R[BV]): R[SMTBool] = op.bvGe(x, y)
      def <=(y: R[BV]): R[SMTBool] = op.bvLe(x, y)
      def ≠(y: R[BV]): R[SMTBool] = op.not(op.eq(x, y))
      // doesn't work
      //def !=(y: R[BV]): R[SMTBool] = op.not(op.eq(x, y))

      def +(y: R[BV])(implicit bitWidth: Int) = op.bvPlus(x, y)(bitWidth)
      def -(y: R[BV])(implicit bitWidth: Int) = op.bvMinus(x, y)(bitWidth)
      def *(y: R[BV])(implicit bitWidth: Int) = op.bvMul(x, y)(bitWidth)
      def /(y: R[BV])(implicit bitWidth: Int) = op.bvDiv(x, y)(bitWidth)
      def %(y: R[BV])(implicit bitWidth: Int) = op.bvMod(x, y)(bitWidth)

      def &(y: R[BV]) = op.bvAnd(x, y)
      def |(y: R[BV]) = op.bvOr(x, y)
      def ⊕(y: R[BV]) = op.bvXor(x, y)
      // TODO not, neg
    }
  }
}

// Staged interfaces and operations

trait StagedSMTBase extends Base { self: SMTBaseInterface =>
  type R[+T] = Rep[T]
}

@virtualize
trait SMTBaseOps extends StagedSMTBase with SMTBaseInterface {
  final val BOOL = Backend.Const("bool")

  def boolVar(x: String): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-var", Backend.Const(x), BOOL))
  def lit(b: Boolean): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-lit", Backend.Const(b)))
  def lit(b: Rep[Boolean]): Rep[SMTBool] =
    if (b) lit(true) else lit(false)
  def not(x: Rep[SMTBool]): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-not", Unwrap(x)))
  def or(x: Rep[SMTBool], y: Rep[SMTBool]): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-or", Unwrap(x), Unwrap(y)))
  def and(x: Rep[SMTBool], y: Rep[SMTBool]): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-and", Unwrap(x), Unwrap(y)))
  def xor(x: Rep[SMTBool], y: Rep[SMTBool]): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-xor", Unwrap(x), Unwrap(y)))
  def iff(x: Rep[SMTBool], y: Rep[SMTBool]): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-iff", Unwrap(x), Unwrap(y)))
  def ite(cnd: Rep[SMTBool], thn: Rep[SMTBool], els: Rep[SMTBool]): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-ite", Unwrap(cnd), Unwrap(thn), Unwrap(els)))
  def imply(x: Rep[SMTBool], y: Rep[SMTBool]): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-imply", Unwrap(x), Unwrap(y)))

  def eq(x: Rep[SMTExpr], y: Rep[SMTExpr]): Rep[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("smt-eq", Unwrap(x), Unwrap(y)))

  def push: Rep[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("smt-push")(Adapter.CTRL))
  def pop: Rep[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("smt-pop")(Adapter.CTRL))
  def assert(x: Rep[SMTBool]): Rep[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("smt-assert", Unwrap(x))(Adapter.CTRL))
  def isValid(x: Rep[SMTBool]): Rep[Int] =
    Wrap[Int](Adapter.g.reflectWrite("smt-query", Unwrap(x))(Adapter.CTRL))
  def checkSat: Rep[Boolean] =
    Wrap[Boolean](Adapter.g.reflectWrite("smt-check")(Adapter.CTRL))
  def getCounterEx(x: Rep[SMTExpr]): Rep[SMTExpr] =
    // TODO: what's the expression should be given here?
    Wrap[SMTExpr](Adapter.g.reflectWrite("smt-get-cex", Unwrap(x))(Adapter.CTRL))

  def handle(n: R[Int]): R[Unit] =
    Wrap[Unit](Adapter.g.reflectWrite("smt-handle", Unwrap(n))(Adapter.CTRL))
}

trait STPCodeGen_SMTBase extends ExtendedCPPCodeGen {
  registerHeader("../stp/build/include", "<stp/c_interface.h>")
  registerHeader("./headers", "<stp_handle.hpp>")
  registerLibrary("-lstp")

  override def remap(m: Manifest[_]): String = {
    val name = m.runtimeClass.getName
    if (name.endsWith("SMTBool")) "Expr"
    else super.remap(m)
  }

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, name, _, _) if name.startsWith("smt-") => false
    case _ => super.mayInline(n)
  }

  override def traverse(n: Node): Unit = n match {
    case Node(s, "smt-assert", List(x), _) => 
      emit("vc_assertFormula(vc, "); shallow(x); emitln(");")
    case Node(s, "smt-push", _, _) =>
      emitln("vc_push(vc);")
    case Node(s, "smt-pop", _, _) =>
      emitln("vc_pop(vc);")
    case Node(s, "sat-handle", List(x), _) =>
      emit("handle(vc, "); shallow(x); emitln(");");
    case _ => super.traverse(n)
  }

  override def shallow(n: Node) = n match {
    case Node(s, "smt-var", Const(id: String)::Const("bool")::Nil, _) => 
      emit(s"""vc_varExpr(vc, \"$id\", vc_boolType(vc))""")
    case Node(s, "smt-lit", Const(true)::Nil, _) =>
      emit("vc_trueExpr(vc)")
    case Node(s, "smt-lit", Const(false)::Nil, _) =>
      emit("vc_falseExpr(vc)")
    case Node(s, "smt-not", List(x), _) => 
      emit("vc_notExpr(vc, "); shallow(x); emit(")")
    case Node(s, "smt-or", List(l, r), _) =>
      emit("vc_orExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "smt-and", List(l, r), _) =>
      emit("vc_andExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "smt-xor", List(l, r), _) =>
      emit("vc_xorExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "smt-iff", List(l, r), _) =>
      emit("vc_iffExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "smt-ite", List(c, t, e), _) =>
      emit("vc_iteExpr(vc, "); shallow(c); emit(", "); shallow(t); emit(", "); shallow(e); emit(")")
    case Node(s, "smt-imply", List(l, r), _) =>
      emit("vc_impliesExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "smt-eq", List(l, r), _) => 
      // TODO: eq may not work as expected (GW: why?)
      emit("vc_eqExpr(vc, "); shallow(l); emit(", "); shallow(r); emit(")")
    case Node(s, "smt-check", _, _) =>
      // TODO: Does it work?
      emit("vc_query(vc, vc_trueExpr(vc))")
    case Node(s, "smt-query", List(x), _) => 
      emit("vc_query(vc, "); shallow(x); emit(")")
    case Node(s, "smt-get-cex", List(x), _) =>
      emit("vc_getCounterExample(vc, "); shallow(x); emit(")");
    case _ => super.shallow(n)
  }
}

trait SMTBitVecOps extends StagedSMTBase with SMTBitVecInterface {
  def lit(i: Int)(implicit width: Int): R[BV] = lit(unit(i))
  def lit(i: R[Int])(implicit width: Int): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-const-expr-int", Unwrap(i), Backend.Const(width)))

  def bvConstExprFromStr(s: String)(implicit bitWidth: Int): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-const-expr-str", Backend.Const(s), Backend.Const(bitWidth)))

  // TODO: variable?
  def bvVar(s: String)(implicit bitWidth: Int): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-expr-var", Backend.Const(s), Backend.Const(bitWidth)))

  // bv arith
  def bvPlus(x: R[BV], y: R[BV])(implicit bitWidth: Int): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-plus", Unwrap(x), Unwrap(y), Backend.Const(bitWidth)))
  def bvMul(x: R[BV], y: R[BV])(implicit bitWidth: Int): R[BV] = 
    Wrap[BV](Adapter.g.reflect("bv-mul", Unwrap(x), Unwrap(y), Backend.Const(bitWidth)))
  def bvDiv(x: R[BV], y: R[BV])(implicit bitWidth: Int): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-div", Unwrap(x), Unwrap(y), Backend.Const(bitWidth)))
  def bvMinus(x: R[BV], y: R[BV])(implicit bitWidth: Int): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-minus", Unwrap(x), Unwrap(y), Backend.Const(bitWidth)))
  def bvMod(x: R[BV], y: R[BV])(implicit bitWidth: Int): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-mod", Unwrap(x), Unwrap(y), Backend.Const(bitWidth)))
  def bvNeg(x: R[BV])(implicit bitWidth: Int): R[BV]=
    Wrap[BV](Adapter.g.reflect("bv-neg", Unwrap(x), Backend.Const(bitWidth)))
  
  // bv compare
  def bvLt(x: R[BV], y: R[BV]): R[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("bv-lt", Unwrap(x), Unwrap(y)))
  def bvLe(x: R[BV], y: R[BV]): R[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("bv-le", Unwrap(x), Unwrap(y)))
  def bvGt(x: R[BV], y: R[BV]): R[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("bv-gt", Unwrap(x), Unwrap(y)))
  def bvGe(x: R[BV], y: R[BV]): R[SMTBool] =
    Wrap[SMTBool](Adapter.g.reflect("bv-ge", Unwrap(x), Unwrap(y)))

  // bv bitwise
  def bvAnd(x: R[BV], y: R[BV]): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-and", Unwrap(x), Unwrap(y)))
  def bvOr(x: R[BV], y: R[BV]): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-or", Unwrap(x), Unwrap(y)))
  def bvXor(x: R[BV], y: R[BV]): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-xor", Unwrap(x), Unwrap(y)))
  def bvNot(x: R[BV]): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-not", Unwrap(x)))
}

trait STPCodeGen_SMTBV extends ExtendedCPPCodeGen {
  registerHeader("../stp/build/include", "<stp/c_interface.h>")
  registerHeader("./headers", "<stp_handle.hpp>")
  registerLibrary("-lstp")

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, name, _, _) if name.startsWith("bv-") => false
    case _ => super.mayInline(n)
  }

  override def remap(m: Manifest[_]): String = {
    val name = m.runtimeClass.getName
    if (name.endsWith("SMTBitVec")) "Expr"
    else super.remap(m)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "bv-const-expr-int", List(i, Const(bw)), _) =>
      emit(s"vc_bvConstExprFromInt(vc, $bw, ")
      shallow(i)
      emit(")")
    case Node(s, "bv-const-expr-str", List(Const(str), Const(bw)), _) =>
      ???
    case Node(s, "bv-expr-var", List(Const(name), Const(bw)), _) =>
      emit(s"""vc_varExpr(vc, \"$name\", vc_bvType(vc, $bw))""")

    // case Node(s, "bv-eq", List(x, y), _) =>
    //  emit("vc_eqExpr(vc, "); shallow(x); emit(", "); shallow(y); emit(")")
    case Node(s, "bv-lt", List(x, y), _) =>
      emit("vc_bvLtExpr(vc, "); shallow(x); emit(", "); shallow(y); emit(")")
    case Node(s, "bv-gt", List(x, y), _) =>
      emit("vc_bvGtExpr(vc, "); shallow(x); emit(", "); shallow(y); emit(")")
    case Node(s, "bv-le", List(x, y), _) =>
      emit("vc_bvLeExpr(vc, "); shallow(x); emit(", "); shallow(y); emit(")")
    case Node(s, "bv-ge", List(x, y), _) =>
      emit("vc_bvGeExpr(vc, "); shallow(x); emit(", "); shallow(y); emit(")")

    // FIXME: Pass real bitwidth!
    // fixed?
    case Node(s, "bv-plus", List(x, y, len), _) =>
      emit("vc_bvPlusExpr(vc, "); shallow(len); emit(", "); shallow(x); emit(", "); shallow(y); emit(")")
    case Node(s, "bv-minus", List(x, y, len), _) =>
      emit("vc_bvMinusExpr(vc, "); shallow(len); emit(", "); shallow(x); emit(", "); shallow(y); emit(")")
    case Node(s, "bv-mul", List(x, y, len), _) =>
      emit("vc_bvMultExpr(vc, "); shallow(len); emit(", "); shallow(x); emit(", "); shallow(y); emit(")")
    case Node(s, "bv-div", List(x, y, len), _) =>
      emit("vc_bvDivExpr(vc, "); shallow(len); emit(", "); shallow(x); emit(", "); shallow(y); emit(");")
    case Node(s, "bv-mod", List(x, y, len), _) =>
      emit("vc_bvModExpr(vc, "); shallow(len); emit(", "); shallow(x); emit(", "); shallow(y); emit(")")
    case Node(s, "bv-neg", List(x, len), _) => ???
    case _ => super.shallow(n)
  }
}

// Unstaged interfaces and operations

trait UnstagedSMTBase { self: SMTBaseInterface =>
  type R[+T] = T
}

trait UnstagedSMTBaseOps extends UnstagedSMTBase with SMTBaseInterface {
  /*
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
   */
}

trait UnstagedSMTBitVecOps extends UnstagedSMTBase with SMTBitVecInterface {

}
