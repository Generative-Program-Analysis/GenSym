package sai.lmsx.smt

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

trait SMTArrayOps extends StagedSMTBase with SMTBitVecInterface {
  //TODO: How to pass parameter to arrayCreate?
  def arrayCreate(s: String)(indexBW: Int)(valueBW: Int): Rep[SMTArray] =
    Wrap[SMTArray](Adapter.g.reflect("smt-array-create-var", Backend.Const(s), Backend.Const(indexBW), Backend.Const(valueBW)));
  def arrayRead(array: Rep[SMTArray], index: Rep[BV]): Rep[BV] =
    Wrap[BV](Adapter.g.reflectRead("smt-array-read", Unwrap(array), Unwrap(index))(Adapter.CTRL))
  // type of array write?
  def arrayWrite(array: Rep[SMTArray], index: Rep[BV], value: Rep[BV]): Rep[SMTExpr] =
    Wrap[SMTExpr](Adapter.g.reflectWrite("smt-array-write", Unwrap(array), Unwrap(index), Unwrap(value))(Adapter.CTRL))
}

trait STPCodeGen_SMTArray extends ExtendedCPPCodeGen {
  registerHeader("../stp/build/include", "<stp/c_interface.h>")
  registerHeader("./headers", "<stp_handle.hpp>")
  registerLibrary("-lstp")

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, name, _, _) if name.startsWith("smt-") => false
    case _ => super.mayInline(n)
  }

  override def traverse(n: Node): Unit = n match {
    case Node(s, "smt-array-write", List(array, i, v), _) =>
      emit(s"vc_writeExpr(vc, "); shallow(array);
      emit(","); shallow(i);
      emit(","); shallow(v); emit(")")
    case _ => super.shallow(n)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "smt-array-create-var", List(Const(name), Const(ibw), Const(vbw)), _) =>
      val indexBW = s"vc_bvType(vc, $ibw)"
      val valueBW = s"vc_bvType(vc, $vbw)"
      emit(s"""vc_varExpr(vc, \"$name\", vc_arrayType(vc, $indexBW, $valueBW))""")
    case Node(s, "smt-array-read", List(array, i), _) =>
      emit(s"vc_readExpr(vc, "); shallow(array); emit(","); shallow(i); emit(")")
    case _ => super.shallow(n)
  }

}