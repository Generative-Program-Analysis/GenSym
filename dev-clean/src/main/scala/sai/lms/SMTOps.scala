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

// Stage polymorphic interface
trait StagePolySMT extends StagePolySAT { op =>
  val bw32: Int = 32
  type BV
  // TODO: how to represent different kind of SMT expression (and how can we compose different theories/logics
  // TODO: how to specify size of BVs
  // about size of BVs
  // a) use hard code bw
  // b) create a node that retrieve the width

  def bvConstExprFromInt(v: Int, bitWidth: Int): R[BV]
  def bvConstExprFromStr(s: String, bitWidth: Int): R[BV]
  // TODO: variable?
  def bvVar(s: String, bitWidth: Int): R[BV]

  // bv arith
  // DLL_PUBLIC Expr vc_bvPlusExpr(VC vc, int bitWidth, Expr left, Expr right);
  def bvPlus(x: R[BV], y: R[BV]): R[BV]
  def bvMul(x: R[BV], y: R[BV]): R[BV]
  def bvDiv(x: R[BV], y: R[BV]): R[BV]
  def bvMinus(x: R[BV], y: R[BV]): R[BV]
  def bvMod(x: R[BV], y: R[BV]): R[BV]
  def bvNeg(x: R[BV]): R[BV]
  
  // bv compare
  def bvLt(x: R[BV], y: R[BV]): R[SATBool]
  def bvLe(x: R[BV], y: R[BV]): R[SATBool]
  def bvGt(x: R[BV], y: R[BV]): R[SATBool]
  def bvGe(x: R[BV], y: R[BV]): R[SATBool]
  def eq(x: R[BV], y: R[BV]): R[SATBool]

  // bv bitwise
  def bvAnd(x: R[BV], y: R[BV]): R[BV]
  def bvOr(x: R[BV], y: R[BV]): R[BV]
  def bvXor(x: R[BV], y: R[BV]): R[BV]
  def bvNot(x: R[BV]): R[BV]

  object SyntaxSMT {
    implicit def __int(n: Int): R[BV] = bvConstExprFromInt(n, bw32)
    implicit class BVOps(x: R[BV]) {
      // compare
      def ≡(y: R[BV]): R[SATBool] = op.eq(x, y)
      def >(y: R[BV]): R[SATBool] = op.bvGt(x, y)
      def <(y: R[BV]): R[SATBool] = op.bvLt(x, y)
      def >=(y: R[BV]): R[SATBool] = op.bvGe(x, y)
      def <=(y: R[BV]): R[SATBool] = op.bvLe(x, y)
      def ≠(y: R[BV]): R[SATBool] = op.not(op.eq(x, y))
      // doesn't work
      //def !=(y: R[BV]): R[SATBool] = op.not(op.eq(x, y))

      def +(y: R[BV]) = op.bvPlus(x, y)
      def -(y: R[BV]) = op.bvMinus(x, y)
      def *(y: R[BV]) = op.bvMul(x, y)
      def /(y: R[BV]) = op.bvDiv(x, y)
      def %(y: R[BV]) = op.bvMod(x, y)

      def &(y: R[BV]) = op.bvAnd(x, y)
      def |(y: R[BV]) = op.bvAnd(x, y)
      def ⊕(y: R[BV]) = op.bvXor(x, y)
      // TODO not, neg
    }
  }
}

trait SMTStagedOps extends Base with Equal with StagedSATOps with StagePolySMT {
  trait BV extends SMTExpr

  def bvConstExprFromInt(v: Int, bitWidth: Int = bw32): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-const-expr-int", Backend.Const(v), Backend.Const(bitWidth)))
  def bvConstExprFromStr(s: String, bitWidth: Int = bw32): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-const-expr-str", Backend.Const(s), Backend.Const(bitWidth)))
  // TODO: variable?
  def bvVar(s: String, bitWidth: Int = bw32): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-expr-var", Backend.Const(s), Backend.Const(bitWidth)))

  // bv arith
  // DLL_PUBLIC Expr vc_bvPlusExpr(VC vc, int bitWidth, Expr left, Expr right);
  def bvPlus(x: R[BV], y: R[BV]): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-plus", Unwrap(x), Unwrap(y)))
  def bvMul(x: R[BV], y: R[BV]): R[BV] = 
    Wrap[BV](Adapter.g.reflect("bv-mul", Unwrap(x), Unwrap(y)))
  def bvDiv(x: R[BV], y: R[BV]): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-div", Unwrap(x), Unwrap(y)))
  def bvMinus(x: R[BV], y: R[BV]): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-minus", Unwrap(x), Unwrap(y)))
  def bvMod(x: R[BV], y: R[BV]): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-mod", Unwrap(x), Unwrap(y)))
  def bvNeg(x: R[BV]): R[BV]=
    Wrap[BV](Adapter.g.reflect("bv-neg", Unwrap(x)))
  
  // bv compare
  def bvLt(x: R[BV], y: R[BV]): R[SATBool] =
    Wrap[SATBool](Adapter.g.reflect("bv-lt", Unwrap(x), Unwrap(y)))
  def bvLe(x: R[BV], y: R[BV]): R[SATBool] =
    Wrap[SATBool](Adapter.g.reflect("bv-le", Unwrap(x), Unwrap(y)))
  def bvGt(x: R[BV], y: R[BV]): R[SATBool] =
    Wrap[SATBool](Adapter.g.reflect("bv-gt", Unwrap(x), Unwrap(y)))
  def bvGe(x: R[BV], y: R[BV]): R[SATBool] =
    Wrap[SATBool](Adapter.g.reflect("bv-ge", Unwrap(x), Unwrap(y)))

  // bv bitwise
  def bvAnd(x: R[BV], y: R[BV]): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-and", Unwrap(x), Unwrap(y)))
  def bvOr(x: R[BV], y: R[BV]): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-or", Unwrap(x), Unwrap(y)))
  def bvXor(x: R[BV], y: R[BV]): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-xor", Unwrap(x), Unwrap(y)))
  def bvNot(x: R[BV]): R[BV] =
    Wrap[BV](Adapter.g.reflect("bv-not", Unwrap(x)))

  def eq(x: R[BV], y: R[BV]): R[SATBool] =
    Wrap[SATBool](Adapter.g.reflect("bv-eq", Unwrap(x), Unwrap(y)))

  
}


trait STPCodeGen_SMT extends ExtendedCCodeGen {
  // TODO register header
  // TODO remap SATBool => Expr
  // vc???

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, name, _, _) if name.startsWith("bv") => false
    case _ => super.mayInline(n)
  }

  override def remap(m: Manifest[_]): String = {

    if (m.runtimeClass.getName.contains("BV")) {
      "Expr"
    }  else {
      super.remap(m)
    }
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "bv-const-expr-int", List(Const(i), Const(bw)), _) =>
      emit(s"vc_bvConstExprFromInt(vc, $bw, $i)")
    case Node(s, "bv-const-expr-str", List(Const(str), Const(bw)), _) =>
      ???
    case Node(s, "bv-expr-var", List(Const(name), Const(bw)), _) =>
      emit(s"""vc_varExpr(vc, \"$name\", vc_bvType(vc, $bw))""")

    case Node(s, "bv-eq", List(x, y), _) =>
      emit("vc_eqExpr(vc, "); shallow(x); emit(", "); shallow(y); emit(")")
    case Node(s, "bv-lt", List(x, y), _) =>
      emit("vc_bvLtExpr(vc, "); shallow(x); emit(", "); shallow(y); emit(")")
    case Node(s, "bv-gt", List(x, y), _) =>
      emit("vc_bvGtExpr(vc, "); shallow(x); emit(", "); shallow(y); emit(")")
    case Node(s, "bv-le", List(x, y), _) =>
      emit("vc_bvLeExpr(vc, "); shallow(x); emit(", "); shallow(y); emit(")")
    case Node(s, "bv-ge", List(x, y), _) =>
      emit("vc_bvGeExpr(vc, "); shallow(x); emit(", "); shallow(y); emit(")")

      // FIXME: Pass real bitwidth!
    case Node(s, "bv-plus", List(x, y), _) =>
      emit("vc_bvPlusExpr(vc, 32,"); shallow(x); emit(", "); shallow(y); emit(")")
    case Node(s, "bv-minus", List(x, y), _) =>
      emit("vc_bvMinusExpr(vc, 32,"); shallow(x); emit(", "); shallow(y); emit(")")
    case Node(s, "bv-mul", List(x, y), _) =>
      emit("vc_bvMultExpr(vc, 32,"); shallow(x); emit(", "); shallow(y); emit(")")
    case Node(s, "bv-div", List(x, y), _) =>
      emit("vc_bvDivExpr(vc, 32,"); shallow(x); emit(", "); shallow(y); emit(");")
    case Node(s, "bv-mod", List(x, y), _) =>
      emit("vc_bvModExpr(vc, 32,"); shallow(x); emit(", "); shallow(y); emit(")")
    case Node(s, "bv-neg", List(x), _) => ???

    
    case _ => super.shallow(n)
      
  }
}

