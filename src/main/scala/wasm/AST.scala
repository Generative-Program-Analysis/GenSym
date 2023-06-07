package gensym.wasm.ast

import gensym.wasm.eval.ModuleInstance
import gensym.wasm.source._

abstract class WIR

case class Module(name: Option[String], definitions: Seq[Definition]) extends WIR
 
abstract class Definition extends WIR
case class FuncDef(name: Option[String], f: FuncField) extends Definition
case class TypeDef(id: Option[String], tipe: FuncType) extends Definition
// FIXME: missing top-level module fields, see WatParser.g4

abstract class FuncField extends WIR
case class FuncBodyDef(tipe: FuncType, localNames: Seq[String], locals: Seq[ValueType], body: Seq[Instr]) extends FuncField
case class InlineImport(mod: String, name: String, typeUse: Option[Int], imports: Any/*FIXME*/) extends FuncField
case class InlineExport(fd: Seq[FuncDef]) extends FuncField

abstract class Instr extends WIR
case object Unreachable extends Instr
case object Nop extends Instr
case object Drop extends Instr
case class Select(ty: Option[Seq[ValueType]]) extends Instr
case class Block(ty: BlockType, instrs: Seq[Instr]) extends Instr
case class Loop(ty: BlockType, instrs: Seq[Instr]) extends Instr
case class If(ty: BlockType, thenInstrs: Seq[Instr], elseInstrs: Seq[Instr]) extends Instr
// FIXME: labelId can be string?
case class Br(labelId: Int) extends Instr
case class BrIf(labelId: Int) extends Instr
case class BrTable(labels: Seq[Int], default: Int) extends Instr
case object Return extends Instr
case class Call(func: Int) extends Instr
// case class CallIndirect(ty: Int, table: Int) extends Instr
case class LocalGet(local: Int) extends Instr
case class LocalSet(local: Int) extends Instr
case class LocalTee(local: Int) extends Instr
case class GlobalGet(global: Int) extends Instr
case class GlobalSet(global: Int) extends Instr
// case class TableGet(table: Int) extends Instr
// case class TableSet(table: Int) extends Instr
// case class TableSize(table: Int) extends Instr
// case class TableGrow(table: Int) extends Instr
// case class TableFill(table: Int) extends Instr
// case class TableCopy(dst: Int, src: Int) extends Instr
// case class TableInit(seg: Int, table: Int) extends Instr
// case class ElemDrop(seg: Int) extends Instr
case class Load(op: LoadOp) extends Instr
case class Store(op: StoreOp) extends Instr
// case class VecLoad(op: VecLoadOp) extends Instr
// case class VecStore(op: VecStoreOp) extends Instr
// case class VecLoadLane(op: VecLaneOp) extends Instr
// case class VecStoreLane(op: VecLaneOp) extends Instr
case object MemorySize extends Instr
case object MemoryGrow extends Instr
case object MemoryFill extends Instr
case object MemoryCopy extends Instr
case class MemoryInit(seg: Int) extends Instr
// case class DataDrop(seg: Int) extends Instr
// case class RefNull(ty: RefType) extends Instr
// case class RefFunc(func: Int) extends Instr
// case object RefIsNull extends Instr
case class Const(num: Num) extends Instr
case class Test(op: TestOp) extends Instr
case class Compare(op: RelOp) extends Instr
case class Unary(op: UnaryOp) extends Instr
case class Binary(op: BinOp) extends Instr
// case class Convert(op: CvtOp) extends Instr
// case class VecConst(vec: Vec) extends Instr
// case class VecTest(op: VecTestOp) extends Instr
// case class VecCompare(op: VecRelOp) extends Instr
// case class VecUnary(op: VecUnOp) extends Instr
// case class VecBinary(op: VecBinOp) extends Instr
// case class VecConvert(op: VecCvtOp) extends Instr
// case class VecShift(op: VecShiftOp) extends Instr
// case class VecBitmask(op: VecBitmaskOp) extends Instr
// case class VecTestBits(op: VecVTestOp) extends Instr
// case class VecUnaryBits(op: VecVUnOp) extends Instr
// case class VecBinaryBits(op: VecVBinOp) extends Instr
// case class VecTernaryBits(op: VecVTernOp) extends Instr
// case class VecSplat(op: VecSplatOp) extends Instr
// case class VecExtract(op: VecExtractOp) extends Instr
// case class VecReplace(op: VecReplaceOp) extends Instr

trait Unresolved
case class CallUnresolved(name: String) extends Instr with Unresolved

abstract class IntOp extends WIR

abstract class UnaryIntOp extends IntOp
abstract class BinaryIntOp extends IntOp
abstract class RelIntOp extends IntOp

case object Clz extends UnaryIntOp
case object Ctz extends UnaryIntOp
case object Popcnt extends UnaryIntOp
case object ExtendS extends UnaryIntOp

case object Add extends BinaryIntOp
case object Sub extends BinaryIntOp
case object Mul extends BinaryIntOp
case object DivS extends BinaryIntOp
case object DivU extends BinaryIntOp
case object RemS extends BinaryIntOp
case object RemU extends BinaryIntOp
case object And extends BinaryIntOp
case object Or extends BinaryIntOp
case object Xor extends BinaryIntOp
case object Shl extends BinaryIntOp
case object ShrS extends BinaryIntOp
case object ShrU extends BinaryIntOp
case object Rotl extends BinaryIntOp
case object Rotr extends BinaryIntOp

case object Eq extends RelIntOp
case object Ne extends RelIntOp
case object LtS extends RelIntOp
case object LeS extends RelIntOp
case object LtU extends RelIntOp
case object LeU extends RelIntOp
case object GtS extends RelIntOp
case object GeS extends RelIntOp
case object GtU extends RelIntOp
case object GeU extends RelIntOp

abstract class IntTestOp extends WIR
case object Eqz extends IntTestOp

abstract class BinOp extends WIR
object BinOp {
  case class Int(op: BinaryIntOp) extends BinOp
}

abstract class TestOp extends WIR
object TestOp {
  case class Int(op: IntTestOp) extends TestOp
}

abstract class RelOp extends WIR
object RelOp {
  case class Int(op: RelIntOp) extends RelOp
}

abstract class UnaryOp extends WIR
object UnaryOp {
  case class Int(op: UnaryIntOp) extends UnaryOp
}

abstract class PackSize extends WIR
case object Pack8 extends PackSize
case object Pack16 extends PackSize
case object Pack32 extends PackSize
case object Pack64 extends PackSize

abstract class Extension extends WIR
case object SX extends Extension
case object ZX extends Extension

abstract class MemOp(align: Int, offset: Int) extends WIR
case class StoreOp(align: Int, offset: Int, tipe: NumType, pack_size: Option[PackSize]) extends MemOp(align, offset)
case class LoadOp(align: Int, offset: Int, tipe: NumType, pack_size: Option[PackSize], extension: Option[Extension]) extends MemOp(align, offset)

// Types

abstract class NumKind extends WIR
case object I32Type extends NumKind
case object I64Type extends NumKind
case object F32Type extends NumKind
case object F64Type extends NumKind

abstract class VecKind extends WIR
case object V128Type extends VecKind

abstract class RefKind extends WIR
case object FuncRefType extends RefKind
case object ExternRefType extends RefKind

abstract class WasmType extends WIR

abstract class ValueType extends WasmType
case class NumType(kind: NumKind) extends ValueType
case class VecType(kind: VecKind) extends ValueType
case class RefType(kind: RefKind) extends ValueType

case class FuncType(names/*optional*/: Seq[String], inps: Seq[ValueType], out: Seq[ValueType]) extends WasmType

abstract class BlockType extends WIR {
  def toFuncType(moduleInst: ModuleInstance): FuncType
}

case class VarBlockType(vR: Int) extends BlockType {
  def toFuncType(moduleInst: ModuleInstance): FuncType = ???
}

case class ValBlockType(tipe: Option[ValueType]) extends BlockType {
  def toFuncType(moduleInst: ModuleInstance): FuncType = tipe match {
    case Some(t) => FuncType(Seq(), Seq(), Seq(t))
    case None => FuncType(Seq(), Seq(), Seq())
  }
}

// Globals

case class GlobalType(valueType: ValueType, mutable: Boolean)
case class Global(tipe: GlobalType, var value: Value)

// Values

abstract class Value extends WIR {
  def tipe: ValueType
}

abstract class Num extends Value {
  def tipe: ValueType = NumType(this match {
    case I32(_) => I32Type
    case I64(_) => I64Type
    case F32(_) => F32Type
    case F64(_) => F64Type
  })
}

case class I32(value: Int) extends Num
case class I64(value: Long) extends Num
case class F32(value: Float) extends Num
case class F64(value: Double) extends Num
