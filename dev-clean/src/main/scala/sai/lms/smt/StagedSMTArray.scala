package sai.lmsx.smt

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

trait SMTArrayOps extends StagedSMTBase with SMTBitVecInterface {
  // (length, index_bit_width)
  val lengthMap = collection.mutable.Map[Rep[SMTArray], (Int, Int)]()
  
  def arrayCreate(s: String, indexBW: Int, valueBW: Int, length: Int = 0): Rep[SMTArray] = {
    val array = Wrap[SMTArray](Adapter.g.reflect("smt-array-create-var", Backend.Const(s), Backend.Const(indexBW), Backend.Const(valueBW)))
    lengthMap(array) = (length, indexBW)
    array
  }

  def arrayRead(array: Rep[SMTArray], index: Rep[BV]): Rep[BV] =
    Wrap[BV](Adapter.g.reflect("smt-array-read", Unwrap(array), Unwrap(index)))

  def arrayWrite(array: Rep[SMTArray], index: Rep[BV], value: Rep[BV]): Rep[SMTArray] = {
    val newArray = Wrap[SMTArray](Adapter.g.reflect("smt-array-write", Unwrap(array), Unwrap(index), Unwrap(value)))
    lengthMap(newArray) = lengthMap.get(array) match {
      case Some(value) => value
      case None => throw new Exception("Not find array")
    }
    newArray
  }

  implicit class SMTArrayOps(array: Rep[SMTArray]) {
    val (length, indexBW) = lengthMap.get(array) match {
      case Some(value) => value
      case None => throw new Exception("Not find array")
    }
    implicit val bw = indexBW

    def apply(i: Rep[BV]): Rep[BV] =
      arrayRead(array, i)
    def apply(i: Rep[BV])(j: Rep[BV]): Rep[BV] = {
      import SyntaxSMT._
      arrayRead(array, i * lit(length) + j)
    }

    def apply(i: Int): Rep[BV] =
      arrayRead(array, lit(i))
    def apply(i: Int)(j: Int): Rep[BV] =
      arrayRead(array, lit(i * length + j))
    
    def write(i: Rep[BV])(v: Rep[BV]): Rep[SMTArray] = 
      arrayWrite(array, i, v)
    def write(i: Rep[BV], j: Rep[BV])(v: Rep[BV]): Rep[SMTArray] = {
      import SyntaxSMT._
      arrayWrite(array, i * lit(length) + j, v)
    }

    def write(i: Int)(v: Rep[BV]): Rep[SMTArray] = 
      arrayWrite(array, lit(i), v)
    def write(i: Int, j: Int)(v: Rep[BV]): Rep[SMTArray] =
      arrayWrite(array, lit(i * length + j), v)
  }
}

trait STPCodeGen_SMTArray extends ExtendedCPPCodeGen {
  registerHeader("../stp/build/include", "<stp/c_interface.h>")
  registerHeader("./headers", "<stp_handle.hpp>")
  registerLibrary("-lstp")

  override def remap(m: Manifest[_]): String = {
    val name = m.runtimeClass.getName
    if (name.endsWith("SMTArray")) "Expr"
    else super.remap(m)
  }

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, name, _, _) if name.startsWith("smt-") => false
    case _ => super.mayInline(n)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "smt-array-create-var", List(Const(name), Const(ibw), Const(vbw)), _) =>
      val indexBW = s"vc_bvType(vc, $ibw)"
      val valueBW = s"vc_bvType(vc, $vbw)"
      emit(s"""vc_varExpr(vc, \"$name\", vc_arrayType(vc, $indexBW, $valueBW))""")
    case Node(s, "smt-array-read", List(array, i), _) =>
      emit(s"vc_readExpr(vc, "); shallow(array); emit(", "); shallow(i); emit(")")
    case Node(s, "smt-array-write", List(array, i, v), _) =>
      emit(s"vc_writeExpr(vc, "); shallow(array);
      emit(", "); shallow(i);
      emit(", "); shallow(v); emit(")")
    case _ => super.shallow(n)
  }

}