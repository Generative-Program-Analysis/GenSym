package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._

object ASTUtils {
  def flattenTypedList(xs: List[TypedConst]) = 
    xs.map(typC => typC.const).foldRight(List[Constant]())((con, ls) => flattenAS(con) ++ ls)

  def flattenAS(cst: Constant): List[Constant] = cst match {
    case ArrayConst(xs) =>
      flattenTypedList(xs)
    case StructConst(xs) =>
      flattenTypedList(xs)
    case _ => List(cst)
  }
}
