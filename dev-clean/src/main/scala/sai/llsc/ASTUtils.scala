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
