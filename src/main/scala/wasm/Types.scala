package gensym.wasm.types

import gensym.wasm.eval.ModuleInstance

abstract class NumKind
case object I32Type extends NumKind
case object I64Type extends NumKind
case object F32Type extends NumKind
case object F64Type extends NumKind

abstract class VecKind
case object V128Type extends VecKind

abstract class RefKind
case object FuncRefType extends RefKind
case object ExternRefType extends RefKind

abstract class ValueType
case class NumType(kind: NumKind) extends ValueType
case class VecType(kind: VecKind) extends ValueType
case class RefType(kind: RefKind) extends ValueType

case class FuncType(inps: Seq[ValueType], out: Seq[ValueType]) extends ValueType

abstract class BlockType {
  def toFuncType(moduleInst: ModuleInstance): FuncType
}

case class VarBlockType(vR: Int) extends BlockType {
  def toFuncType(moduleInst: ModuleInstance): FuncType = ???
}

case class ValBlockType(tipe: Option[ValueType]) extends BlockType {
  def toFuncType(moduleInst: ModuleInstance): FuncType = tipe match {
    case Some(t) => FuncType(Seq(), Seq(t))
    case None => FuncType(Seq(), Seq())
  }
}
