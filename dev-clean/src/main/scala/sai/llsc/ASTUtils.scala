package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._

object ASTUtils {
  def getTySize(vt: LLVMType, align: Int = 1): Int = vt match {
    case ArrayType(size, ety) =>
      val rawSize = size * getTySize(ety, align)
      if (rawSize % align == 0) rawSize
      else (rawSize / align + 1) * align
    case _ => 1
  }

  def flattenArray(cst: Constant): List[Constant] = cst match {
    case ArrayConst(xs) =>
      xs.map(typC => typC.const).foldRight(List[Constant]())((con, ls) => flattenArray(con) ++ ls)
    case _ => List(cst)
  }
}
