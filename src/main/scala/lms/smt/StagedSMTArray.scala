package gensym.lmsx.smt

import gensym.utils.symbol.Symbol

import lms.core._
import lms.util._
import lms.core.stub._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext

trait SMTArrayOps extends SMTArrayInterface with StagedSMTBase with SMTBitVecOps {
  // TODO: support arrays of arbitrary dimensions

  // lengthMap maps array representations to pairs of its 2nd-dim length and index domain bit-width.
  val lengthMap = collection.mutable.Map[Rep[SMTArray], (Int, Int)]()

  def arrayCreate(s: String, indexBW: Int, valueBW: Int, length: Int = 0): Rep[SMTArray] = {
    val array = Wrap[SMTArray](Adapter.g.reflect("smt-array-create-var",
      Backend.Const(s), Backend.Const(indexBW), Backend.Const(valueBW)))
    lengthMap(array) = (length, indexBW)
    array
  }

  def arrayConstCreate(arr: List[Rep[BV]], indexBW: Int, valueBW: Int): Rep[SMTArray] = {
    var smtArr = arrayCreate(Symbol.freshName("carr"), indexBW, valueBW, arr.length)
    for {
      i <- 0 to arr.length - 1
    } {
      smtArr = (smtArr(i) = arr(i))
    }
    smtArr
  }

  def arrayRead(array: Rep[SMTArray], index: Rep[BV]): Rep[BV] =
    Wrap[BV](Adapter.g.reflect("smt-array-read", Unwrap(array), Unwrap(index)))

  def arrayWrite(array: Rep[SMTArray], index: Rep[BV], value: Rep[BV]): Rep[SMTArray] = {
    val newArray = Wrap[SMTArray](Adapter.g.reflect("smt-array-write", Unwrap(array), Unwrap(index), Unwrap(value)))
    lengthMap(newArray) = lengthMap(array)
    newArray
  }

  implicit class SMTArrayOps(array: Rep[SMTArray]) {
    val (length, indexBW) = lengthMap(array)
    implicit val bw = indexBW

    def apply(i: Rep[BV]): Rep[BV] =
      arrayRead(array, i)
    def apply(i: Rep[BV], j: Rep[BV]): Rep[BV] = {
      import SyntaxSMT._
      arrayRead(array, i * lit(length) + j)
    }

    def apply(i: Int): Rep[BV] =
      arrayRead(array, lit(i))
    def apply(i: Int, j: Int): Rep[BV] =
      arrayRead(array, lit(i * length + j))

    def update(i: Rep[BV], v: Rep[BV]): Rep[SMTArray] =
      arrayWrite(array, i, v)
    def update(i: Rep[BV], j: Rep[BV])(v: Rep[BV]): Rep[SMTArray] = {
      import SyntaxSMT._
      arrayWrite(array, i * lit(length) + j, v)
    }

    def update(i: Int, v: Rep[BV]): Rep[SMTArray] =
      arrayWrite(array, lit(i), v)
    def update(i: Int, j: Int, v: Rep[BV]): Rep[SMTArray] =
      arrayWrite(array, lit(i * length + j), v)
  }
}

trait STPCodeGen_SMTArray extends ExtendedCPPCodeGen {
  //registerHeader("<stp/c_interface.h>")
  //registerHeader("./headers", "<stp_handle.hpp>")
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
