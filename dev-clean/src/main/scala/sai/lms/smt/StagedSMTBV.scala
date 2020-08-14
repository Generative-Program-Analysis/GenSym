package sai.lmsx.smt

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

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
