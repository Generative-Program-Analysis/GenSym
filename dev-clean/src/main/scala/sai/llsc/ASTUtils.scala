package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._

object ASTUtils {
  def flattenTypedList(xs: List[TypedConst]) = xs.map(c => flattenAS(c.const)).flatten

  def flattenAS(cst: Constant): List[Constant] = cst match {
    case ArrayConst(xs) =>
      flattenTypedList(xs)
    case StructConst(xs) =>
      flattenTypedList(xs)
    case _ => List(cst)
  }

  def flattenTy(ty: LLVMType): List[LLVMType] = ty match {
    case Struct(types) =>
      types.flatMap(flattenTy(_))
    case ArrayType(size, ety) =>
      List.fill(size)(flattenTy(ety)).flatten
    case _ => List(ty)
  }
}

object Constants {
  final val BYTE_SIZE: Int = 8
  final val DEFAULT_INT_BW: Int = BYTE_SIZE * 4
  final val DEFAULT_ADDR_BW: Int = BYTE_SIZE * 8
  final val DEFAULT_INDEX_BW: Int = BYTE_SIZE * 8
  final val ARCH_WORD_SIZE: Int = 64
}
